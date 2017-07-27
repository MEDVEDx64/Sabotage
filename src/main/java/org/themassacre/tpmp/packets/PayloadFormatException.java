package org.themassacre.tpmp.packets;

import org.themassacre.tpmp.generic.TpmpException;

public class PayloadFormatException extends TpmpException {
	public PayloadFormatException(String msg) {
		super(msg);
	}
	
	private static final long serialVersionUID = -8061071836280891145L;
}
