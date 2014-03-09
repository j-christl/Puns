package me.inplex.puns.net.server;

import me.inplex.puns.Game;


public class ServerNet {
	
	public static int port = 13337;
	public static ServerThreadListen threadListen;
	public static ServerThreadReceive threadReceive;
	public static Client client;
	
	public static void host() {
		Game.game.isHost = true;
		threadListen = new ServerThreadListen();
		threadListen.start();
		// Receive gets started in Listen Thread
	}
	
}