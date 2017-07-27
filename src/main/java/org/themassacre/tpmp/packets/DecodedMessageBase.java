package org.themassacre.tpmp.packets;

// Base class for message payload schemes

public class DecodedMessageBase {
	protected Message message;
	
	public DecodedMessageBase(Message m) {
		message = m;
	}
	
	public Message getMessage() {
		return message;
	}
}
