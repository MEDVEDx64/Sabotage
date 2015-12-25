package org.themassacre.tpmp.packets;

import org.themassacre.tpmp.generic.TpmpException;

public class TruncatedMessageException extends TpmpException {
	public TruncatedMessageException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = -7877733030856749236L;
}
