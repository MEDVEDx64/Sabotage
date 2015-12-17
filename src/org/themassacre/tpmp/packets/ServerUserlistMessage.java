package org.themassacre.tpmp.packets;

import java.util.*;

public class ServerUserlistMessage extends DecodedMessageBase {
	public ServerUserlistMessage(Message m) {
		super(m);
		
		String pl = m.getPayload();
		if(pl.length() == 0) {
			names = new String[0];
			return;
		}
		
		String[] n = pl.split("\n");
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < n.length; i++) {
			if(n[i].length() == 0)
				continue;
			list.add(n[i]);
		}
		
		names = (String[])list.toArray();
	}

	private String[] names;
	
	public String[] getNames() {
		return names;
	}
}
