package org.themassacre.tpmp.packets;

public class ServerTextMessage extends NamedMessage {
	public ServerTextMessage(Message m) throws PayloadFormatException {
		super(m);
	}

	public String getSender() {
		return nickname;
	}
}
