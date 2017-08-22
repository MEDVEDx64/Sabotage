package org.themassacre.tpmp.packets;

import java.nio.charset.StandardCharsets;

import org.themassacre.tpmp.generic.*;

public class Message {
	private int payloadType;
	private int command;
	private String payload;
	
	private int plLength = -1;
	
	public static final int HEADER_LENGTH = 3;
	public static final int MAX_PAYLOAD_LENGTH = 0x3fff;
	
	public Message(byte[] buffer) throws TpmpException {
		if(buffer.length < HEADER_LENGTH)
			throw new TpmpException("Message is too short");
		
		payloadType = buffer[0]&1;
		command = buffer[0] >> 1;
		int length = buffer[2];
		length |= (buffer[1] << 8);
		if(length < 0) {
			length += 256;
		}

		if(length > buffer.length - HEADER_LENGTH)
			throw new TruncatedMessageException("Message seems to be not received fully");
		
		if(length == 0) {
			payload = "";
			return;
		}
		
		byte[] pl = new byte[length];
		System.arraycopy(buffer, HEADER_LENGTH, pl, 0, length);
		payload = new String(pl, StandardCharsets.UTF_8);
		plLength = length;
	}
	
	public Message(int payloadType, int command, String payload) {
		this.payload = payload;
		this.payloadType = payloadType;
		this.command = command;
	}
	
	public String getPayload() {
		return payload;
	}
	
	public int getPayloadType() {
		return payloadType;
	}
	
	public int getCommand() {
		return command;
	}
	
	private byte[] createByteArray() {
		return payload.getBytes(StandardCharsets.UTF_8);
	}
	
	public byte[] toByteArray() throws TpmpException {
		byte[] pl = createByteArray();
		if(pl.length > MAX_PAYLOAD_LENGTH)
			throw new TpmpException("Message payload is too long");
		
		byte[] buffer = new byte[HEADER_LENGTH + pl.length];
		buffer[0] = (byte) ((command << 1) | (payloadType&1));
		if(pl.length > 0) {
			System.arraycopy(pl, 0, buffer, HEADER_LENGTH, pl.length);
			buffer[1] = (byte) (pl.length >> 8);
			buffer[2] = (byte) pl.length;
		}
		
		return buffer;
	}
	
	public int getPayloadLength() {
		if(plLength == -1)
			plLength = createByteArray().length;
		return plLength;
	}
}
