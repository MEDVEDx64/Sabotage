package org.themassacre.sabotage.apps.core;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.themassacre.sabotage.apps.FailsafeApplication;

public class ServerCoreApplication extends FailsafeApplication implements ServerInstance {
	final static Logger logger = LogManager.getLogger(ServerCoreApplication.class);
	
	private JSONObject settings;
	private List<ClientInstance> clients = new ArrayList<ClientInstance>();

	@Override
	protected void executePayload() throws Exception {
		settings = getConfiguration();
		int port = settings.getInt("port");
		
		try(ServerSocket ss = new ServerSocket(port)) {
			logger.info("Listening on port " + port);
			while(true) {
				Socket s = ss.accept();
				logger.info("Accepted connection from " + s);
				ClientInstance client;
				try {
					client = new ClientInstance(s, this);
				} catch(Exception e) {
					logger.error("Failed to create client instance: " + e);
					continue;
				}
				
				clients.add(client);
				client.start();
			}
		}
	}

	@Override
	public List<ClientInstance> getClients() {
		return clients;
	}

	@Override
	public void broadcast(byte[] bytes) throws IOException {
		for(ClientInstance c: clients) {
			c.send(bytes);
		}
	}

}
