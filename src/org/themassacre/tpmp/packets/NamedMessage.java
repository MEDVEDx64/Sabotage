package org.themassacre.tpmp.packets;

import org.themassacre.tpmp.generic.PayloadFormatException;

class NamedMessage extends DecodedMessageBase {
	public NamedMessage(Message m) throws PayloadFormatException {
		super(m);
		
		String pl = m.getPayload();
		int idx = pl.indexOf('\n');
		if(idx < 0)
			throw new PayloadFormatException("No text field given");
		
		nickname = pl.substring(0, idx);
		try {
			text = pl.substring(idx + 1);
		} catch(StringIndexOutOfBoundsException e) {
			text = "";
		}
	}
	
	protected String nickname;
	protected String text;
	
	public String getText() {
		return text;
	}
}
