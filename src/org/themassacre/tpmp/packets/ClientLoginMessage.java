package org.themassacre.tpmp.packets;

import org.themassacre.tpmp.generic.PayloadFormatException;

public class ClientLoginMessage extends DecodedMessageBase {
	public ClientLoginMessage(Message m) throws PayloadFormatException {
		super(m);
		
		String pl = m.getPayload();
		if(pl.length() == 0)
			throw new PayloadFormatException("Missing nickname");
		for(int i = 0; i < pl.length(); i++) {
			if(pl.charAt(i) < 0x20)
				throw new PayloadFormatException("Nickname contains illegal characters");
		}
	}

	public String getNickname() {
		return message.getPayload();
	}
}
