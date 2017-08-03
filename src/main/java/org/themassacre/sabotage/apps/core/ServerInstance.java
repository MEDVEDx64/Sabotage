package org.themassacre.sabotage.apps.core;

import java.io.IOException;
import java.util.List;

public interface ServerInstance {
	List<ClientInstance> getClients();
	void broadcast(byte[] bytes) throws IOException;
}
