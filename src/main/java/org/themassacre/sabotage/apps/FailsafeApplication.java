package org.themassacre.sabotage.apps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.themassacre.sabotage.Launcher;

public abstract class FailsafeApplication extends Thread implements Application {
	final static Logger logger = LogManager.getLogger(Launcher.class);
	
	@Override
	public boolean up() {
		start();
		return true;
	}

	@Override
	public void down() {
		interrupt();
	}
	
	protected abstract void executePayload() throws InterruptedException;
	
	@Override
	public final void run() {
		while(true) {
			if(interrupted()) {
				logger.info("Failsafe application has been interrupted");
				break;
			}
			
			try {
				executePayload();
			} catch(InterruptedException eInt) {
			}
			
			catch(Throwable e) {
				logger.error("Failsafe application has encountered an error: " + e);
				try {
					sleep(1000);
				} catch (InterruptedException eInt) {
				}
			}
		}
	}
}
