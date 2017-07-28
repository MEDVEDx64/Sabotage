package org.themassacre.sabotage.apps;

public class FailsafeApplication extends Thread implements Application {

	@Override
	public boolean up() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void down() {
		// TODO Auto-generated method stub
	}
}
