package org.themassacre.tpmp.packets;

import org.themassacre.tpmp.generic.PayloadFormatException;

public class ClientUserlistMessage extends DecodedMessageBase {
	public ClientUserlistMessage(Message m) throws PayloadFormatException {
		super(m);
		if(m.getPayload().length() > 0)
			throw new PayloadFormatException("Unexcepted extra data");
	}
}
