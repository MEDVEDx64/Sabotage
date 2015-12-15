package org.themassacre.tpmp.packets;

/*
 * Message command constants
 * - Determines the action should be taken,
 * only valid for client requests.
 */

public class ClientCommand {
	public static final int NO_ACTION = 0; // Just a text message
	public static final int LOGIN = 1; // Login message with nickname
	public static final int PRIVATE = 2; // Single-recepient message
	public static final int USERLIST = 3; // Userlist request without payload
}
