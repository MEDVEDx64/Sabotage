package org.themassacre.sabotage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.*;
import org.json.*;

public class Launcher extends Thread {
	final static Logger logger = LogManager.getLogger(Launcher.class);
	
	boolean running = true;
	JSONObject config = null;
	List<AppContainer> applications = new ArrayList<AppContainer>();

	public Launcher() {
		logger.info("Sabotage TPMP server is starting");
		
		try(InputStream cfRes = Launcher.class.getResourceAsStream("config.json")) {
			config = new JSONObject(cfRes);
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
		}
	}
	
	void down() {
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
