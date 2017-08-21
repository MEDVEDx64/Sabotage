package org.themassacre.sabotage.apps.core;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.themassacre.sabotage.utils.CaseInsensitiveComparator;
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
		setName("client thread - " + s.getInetAddress().getHostAddress() + ":" + s.getPort());
		
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
				server.broadcast(getUserList().toByteArray());
				if(nickName != null) {
					server.broadcast(ServerMessageFactory
							.createNotificationMessage(nickName + " left").toByteArray());
				}
				
				logger.info("Disconnecting");
				socket.close();
			} catch(Exception e) {
				logger.warn("User was not disconnected gracefully: " + e);
			}
		}
	}
	
	private void handleMessage(Message msg) throws IOException, TpmpException {
		switch(msg.getCommand()) {
		case ClientCommand.LOGIN: {
			String _nickName = new ClientLoginMessage(msg).getNickname();
			if(_nickName.equals(nickName)) break;
			
			if(find(_nickName) != null) {
				send(ServerMessageFactory.createNotificationMessage("Nickname already in use").toByteArray());
				break;
			}
			
			if(nickName == null) {
				send(ServerMessageFactory.createNotificationMessage("Welcome, " + _nickName).toByteArray());
				server.broadcast(ServerMessageFactory
						.createNotificationMessage(_nickName + " joined").toByteArray());
			} else if(nickName != _nickName) {
				server.broadcast(ServerMessageFactory
						.createNotificationMessage(nickName + " is now known as " + _nickName).toByteArray());
			}
			
			nickName = _nickName;
			server.broadcast(getUserList().toByteArray());
			logger.info("Setting own nickname to " + nickName);
			break;
		}
		
		case ClientCommand.NO_ACTION: {
			if(nickName == null) {
				send(ServerMessageFactory.createNotificationMessage("Please log in").toByteArray());
				break;
			}
			
			server.broadcast(ServerMessageFactory.createTextMessage(nickName, msg.getPayload()).toByteArray());
			break;
		}
		
		case ClientCommand.PRIVATE: {
			if(nickName == null) {
				send(ServerMessageFactory.createNotificationMessage("Please log in").toByteArray());
				break;
			}
			
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
			send(getUserList().toByteArray());
			break;
		}
		}
	}
	
	public ClientInstance find(String nickName) {
		for(ClientInstance c: server.getClients()) {
			if(nickName.equals(c.getNickName())) {
				return c;
			}
		}
		
		return null;
	}
	
	public void send(byte[] bytes) throws IOException {
		out.write(bytes);
	}
	
	private Message getUserList() {
		List<String> names = new ArrayList<String>();
		for(ClientInstance c: server.getClients()) {
			String _nickName = c.getNickName();
			if(_nickName != null) {
				names.add(_nickName);
			}
		}
		
		Collections.sort(names, new CaseInsensitiveComparator());
		return ServerMessageFactory.createUserlistMessage(names.toArray(new String[0]));
	}
	
	public String getNickName() {
		return nickName;
	}
}
