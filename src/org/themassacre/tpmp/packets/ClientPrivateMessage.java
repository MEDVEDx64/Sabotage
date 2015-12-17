package org.themassacre.tpmp.packets;

import org.themassacre.tpmp.generic.PayloadFormatException;

public class ClientPrivateMessage extends NamedMessage {
	public ClientPrivateMessage(Message m) throws PayloadFormatException {
		super(m);
	}
	
	public String getRecipient() {
		return nickname;
	}
}
