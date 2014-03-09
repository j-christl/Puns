package me.inplex.puns.net.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import me.inplex.puns.Game;

public class ClientNet {
	
	public static int port = 13337;
	public static ClientThreadReceive threadReceive;
	public static DataInputStream inputStream;
	public static DataOutputStream outputStream;
	
	public static int serverX = 0, serverY = 0;
	
	public static void connect(String ip) {
		Game.game.isHost = false;
		Game.game.getWorld().getMap().clear();
		threadReceive = new ClientThreadReceive(ip);
		threadReceive.start();
	}
	
}