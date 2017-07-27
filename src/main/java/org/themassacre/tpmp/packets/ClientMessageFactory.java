package org.themassacre.tpmp.packets;

public class ClientMessageFactory {
	public static Message createTextMessage(String text) {
		return new Message(PayloadType.TEXT, ClientCommand.NO_ACTION, text);
	}
	
	public static Message createLoginMessage(String nickname) throws MessageFactoryException {
		if(nickname.length() == 0)
			throw new MessageFactoryException("Nickname is too short");
		
		for(int i = 0; i < nickname.length(); i++) {
			if(nickname.charAt(i) < 0x20)
				throw new MessageFactoryException("Nickname contains illegal characters");
		}
		
		return new Message(PayloadType.TEXT, ClientCommand.LOGIN, nickname);
	}
	
	public static Message createPrivateMessage(String recipient, String text) {
		return new Message(PayloadType.TEXT, ClientCommand.PRIVATE,
				recipient + "\n" + text);
	}
	
	public static Message createUserlistMessage() {
		return new Message(PayloadType.TEXT, ClientCommand.USERLIST, "");
	}
}
