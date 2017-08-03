package org.themassacre.sabotage;

import java.lang.reflect.Constructor;

import org.apache.logging.log4j.*;
import org.themassacre.sabotage.apps.Application;

public class AppContainer {
	final static Logger logger = LogManager.getLogger(AppContainer.class);
	
	private Application app;
	private String clName;
	private String dsName;
	
	public AppContainer(String className, String displayName) throws AppInstantiationException {
		clName = className;
		dsName = displayName;
		
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
}
