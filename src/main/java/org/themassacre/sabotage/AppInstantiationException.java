package org.themassacre.sabotage;

public class AppInstantiationException extends Exception {
	private static final long serialVersionUID = -9194088368252866921L;
	
	Exception inner = null;
	
	public AppInstantiationException(Exception inner) {
		super();
		this.inner = inner;
	}
	
	@Override public Throwable getCause() {
		return inner;
	}
	
}
