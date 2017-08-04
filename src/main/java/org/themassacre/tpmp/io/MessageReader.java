package org.themassacre.tpmp.io;

import java.io.*;
import java.util.*;

import org.themassacre.tpmp.generic.TpmpException;
import org.themassacre.tpmp.packets.*;

public class MessageReader {
	InputStream stream;
	List<Message> pool = new ArrayList<Message>();
	byte[] buffer = new byte[Message.HEADER_LENGTH + Message.MAX_PAYLOAD_LENGTH];
	byte[] leftover = null;
	
	public MessageReader(InputStream stream) {
		this.stream = stream;
	}
	
	// Returns false on EOF
	public boolean read() throws IOException, TpmpException {
		int bytesRead;
		if(leftover == null)
			bytesRead = stream.read(buffer);
		else {
			System.arraycopy(leftover, 0, buffer, 0, leftover.length);
			bytesRead = stream.read(buffer, leftover.length, buffer.length - leftover.length);
			leftover = null;
		}
		if(bytesRead <= 0)
			return false;
		
		try {
			Message m = new Message(buffer);
			pool.add(m);
		}
		catch(TruncatedMessageException e) {
			leftover = new byte[bytesRead];
			System.arraycopy(buffer, buffer.length - bytesRead, leftover, 0, leftover.length);
		}
		
		return true;
	}
	
	public Message[] collect() {
		Message[] arr = pool.toArray(new Message[0]);
		pool.clear();
		return arr;
	}
	
	public int count() {
		return pool.size();
	}
}
