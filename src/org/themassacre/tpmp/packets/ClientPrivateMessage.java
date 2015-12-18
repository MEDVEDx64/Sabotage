package org.themassacre.tpmp.packets;

public class ClientPrivateMessage extends NamedMessage {
	public ClientPrivateMessage(Message m) throws PayloadFormatException {
		super(m);
	}
	
	public String getRecipient() {
		return nickname;
	}
}
