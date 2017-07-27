package org.themassacre.tpmp.packets;

/*
 * Message command constants
 * - Determines the action should be taken,
 * only valid for server responses.
 */

public class ServerCommand {
	public static final int NOTIFY = 32; // System notification
	public static final int TEXT = 33; // Text message with "sender" field
	public static final int USERLIST = 34; // Userlist response
}
