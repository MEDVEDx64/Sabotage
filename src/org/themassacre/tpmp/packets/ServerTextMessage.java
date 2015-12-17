package org.themassacre.tpmp.packets;

import org.themassacre.tpmp.generic.PayloadFormatException;

public class ServerTextMessage extends NamedMessage {
	public ServerTextMessage(Message m) throws PayloadFormatException {
		super(m);
	}

	public String getSender() {
		return nickname;
	}
}
