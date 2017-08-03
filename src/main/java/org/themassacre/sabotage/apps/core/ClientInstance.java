package org.themassacre.sabotage.apps.core;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.themassacre.tpmp.generic.TpmpException;
import org.themassacre.tpmp.io.MessageReader;
import org.themassacre.tpmp.packets.*;

public class ClientInstance extends Thread {
	final static Logger logger = LogManager.getLogger(ClientInstance.class);
	
	private MessageReader in;
	private OutputStream out;
	
	private Socket socket;
	private ServerInstance server;
	private String nickName = null;
	
	public ClientInstance(Socket s, ServerInstance srv) throws IOException {
		socket = s;
		server = srv;
		setName("client thread " + s);
		
		in = new MessageReader(socket.getInputStream());
		out = socket.getOutputStream();
	}
	
	@Override
	public void run() {
		try {
			while(in.read()) {
				for(Message msg: in.collect()) {
					handleMessage(msg);
				}
			}
		}
		
		catch(Exception e) {
			logger.error("Dropping client connection due to error: " + e + " - " + e.getMessage());
		}
		
		finally {
			server.getClients().remove(this);
			
			try {
				logger.info("Disconnecting");
				socket.close();
			} catch(IOException eIO) {
			}
		}
	}
	
	private void handleMessage(Message msg) throws IOException, TpmpException {
		switch(msg.getCommand()) {
		case ClientCommand.LOGIN: {
			nickName = new ClientLoginMessage(msg).getNickname();
			break;
		}
		
		case ClientCommand.NO_ACTION: {
			server.broadcast(ServerMessageFactory.createTextMessage(nickName, msg.getPayload()).toByteArray());
			break;
		}
		
		case ClientCommand.PRIVATE: {
			ClientPrivateMessage dMsg = new ClientPrivateMessage(msg);
			ClientInstance recipient = find(dMsg.getRecipient());
			if(recipient == null) {
				send(ServerMessageFactory.createNotificationMessage("No such nick").toByteArray());
			} else {
				byte[] bytes = ServerMessageFactory.createTextMessage(nickName, dMsg.getText()).toByteArray();
				send(bytes);
				recipient.send(bytes);
			}
			
			break;
		}
		
		case ClientCommand.USERLIST: {
			List<String> names = new ArrayList<String>();
			for(ClientInstance c: server.getClients()) {
				names.add(c.getNickName());
			}
			
			send(ServerMessageFactory.createUserlistMessage(names.toArray(new String[0])).toByteArray());
			break;
		}
		}
	}
	
	public ClientInstance find(String nickName) {
		for(ClientInstance c: server.getClients()) {
			if(c.getNickName() == nickName) {
				return c;
			}
		}
		
		return null;
	}
	
	public void send(byte[] bytes) throws IOException {
		out.write(bytes);
	}
	
	public String getNickName() {
		return nickName;
	}
}
