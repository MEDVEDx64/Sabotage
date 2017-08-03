package org.themassacre.sabotage.apps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.themassacre.sabotage.Launcher;

public abstract class FailsafeApplication extends Thread implements Application {
	final static Logger logger = LogManager.getLogger(Launcher.class);
	
	private JSONObject config = null;
	
	@Override
	public boolean up() {
		start();
		return true;
	}

	@Override
	public void down() {
		interrupt();
	}
	
	@Override
	public void configure(JSONObject obj) {
		config = obj;
	}
	
	protected JSONObject getConfiguration() {
		return config;
	}
	
	protected abstract void executePayload() throws InterruptedException;
	
	@Override
	public final void run() {
		while(true) {
			if(interrupted()) {
				logger.info("Application has been interrupted");
				break;
			}
			
			try {
				executePayload();
				break;
			} catch(InterruptedException eInt) {
			}
			
			catch(Throwable e) {
				logger.error("Application has encountered an error: " + e);
				try {
					sleep(1000);
				} catch (InterruptedException eInt) {
				}
			}
		}
	}
}
