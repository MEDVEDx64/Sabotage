package org.themassacre.sabotage.apps;

import org.json.JSONObject;

public interface Application {
	boolean up();
	void down();
	void configure(JSONObject obj);
}
