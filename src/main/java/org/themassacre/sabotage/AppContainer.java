package org.themassacre.sabotage;

import java.lang.reflect.Constructor;

import org.apache.logging.log4j.*;
import org.themassacre.sabotage.apps.Application;

public class AppContainer {
	final static Logger logger = LogManager.getLogger(AppContainer.class);
	
	Application app;
	String clName;
	String dsName;
	boolean crit = false;
	
	public AppContainer(String className, String displayName) throws AppInstantiationException {
		clName = className;
		dsName = displayName;
		
		instantiate();
	}
	
	public AppContainer(String className, String displayName, boolean crit) throws AppInstantiationException {
		clName = className;
		dsName = displayName;
		this.crit = crit;
		
		instantiate();
	}
	
	public void instantiate() throws AppInstantiationException {
		logger.debug("Instantiating " + clName);
		
		try {
			Class<?> cl = Class.forName(clName);
			Constructor<?> ctor = cl.getConstructor();
			app = (Application)ctor.newInstance();
		}
		
		catch(Exception e) {
			throw new AppInstantiationException(e);
		}
	}
	
	public Application getApplication() {
		return app;
	}
	
	public String getName() {
		return dsName;
	}
	
	public boolean isCritical() {
		return crit;
	}
}
