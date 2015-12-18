package org.themassacre.tpmp.packets;

public class ServerMessageFactory {
	public static Message createNotificationMessage(String text) {
		return new Message(PayloadType.TEXT, ServerCommand.NOTIFY, text);
	}
	
	public static Message createTextMessage(String sender, String text) {
		return new Message(PayloadType.TEXT, ServerCommand.TEXT, sender + "\n" + text);
	}
	
	public static Message createUserlistMessage(String[] names) {
		StringBuilder s = new StringBuilder();
		for(String n: names)
			s.append(n + "\n");
		return new Message(PayloadType.TEXT, ServerCommand.USERLIST, s.toString().trim());
	}
}
