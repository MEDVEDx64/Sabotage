package org.themassacre.sabotage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.*;
import org.json.*;
import org.themassacre.sabotage.apps.Application;

public class Launcher extends Thread {
	final static Logger logger = LogManager.getLogger(Launcher.class);
	
	boolean running = true;
	JSONObject config = null;
	List<AppContainer> applications = new ArrayList<AppContainer>();

	public Launcher() {
		try(BufferedReader cfRes = new BufferedReader(
				new InputStreamReader(Launcher.class.getResourceAsStream("/config.json")))) {
			StringBuffer buf = new StringBuffer();
			String line;
			while(true) {
				line = cfRes.readLine();
				if(line == null) break;
				buf.append(line);
			}
			
			config = new JSONObject(buf.toString());
		}
		
		catch(Exception e) {
			logger.fatal("Error while reading the configuration file");
			logger.fatal(e);
			System.exit(-1);
		}
		
		Runtime.getRuntime().addShutdownHook(this);
		
		try {
			up();
		}
		
		catch(Exception e) {
			logger.fatal(e);
			System.exit(-1);
		}
		
		while(running) {
			try {
				Thread.sleep(1000);
			}
			
			catch(InterruptedException eInt) {				
			}
		}
		
		down();
		logger.info("Shutdown.");
	}
	
	void up() {
		JSONArray appList = config.getJSONArray("applications");
		for(int i = 0; i < appList.length(); i++) {
			JSONObject appObject = appList.getJSONObject(i);
			if(!appObject.getBoolean("enabled")) continue;
			boolean critical = appObject.getBoolean("critical");
			String name = appObject.getString("name");
			logger.info("Starting " + name);
			
			try {
				AppContainer container = new AppContainer(appObject.getString("class"), name);
				Application app = container.getApplication();
				app.configure(appObject.getJSONObject("settings"));
				if(!app.up()) {
					throw new Exception("Entry point invocation returned error status");
				}
				
				applications.add(container);
				
			} catch(Exception e) {
				if(critical) {
					logger.fatal("Failed to instantiate mission-critical application " + name + ": " + e);
					System.exit(-1);
				} else {
					logger.error("Failed to instantiate application " + name + ": " + e);
				}
			}
		}
	}
	
	void down() {
		for(AppContainer container: applications) {
			logger.info("Stopping " + container.getName());
			container.getApplication().down();
		}
		
		applications.clear();
	}
	
	// The shutdown hook
	@Override public void run() {
		logger.info("Received termination signal");
		running = false;
	}
	
	public static void main(String[] args) {
		new Launcher();
	}

}
