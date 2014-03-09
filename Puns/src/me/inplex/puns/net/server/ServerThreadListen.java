package me.inplex.puns.net.server;

import java.net.ServerSocket;
import java.net.Socket;

import me.inplex.puns.Game;
import me.inplex.puns.entity.Block;
import me.inplex.puns.entity.Point;

public class ServerThreadListen extends Thread {

	public ServerThreadListen() {

	}

	@Override
	public void run() {
		try {
			System.out.println("Hosting with port " + ServerNet.port);
			@SuppressWarnings("resource")
			ServerSocket srv = new ServerSocket(ServerNet.port);
			System.out.println("Hosted!");
			System.out.println("Waiting for Connection!");
			while (true) {
				Socket s = srv.accept();
				ServerNet.client = new Client(s);
				System.out.println("new Connection!");
				sleep(200);
				int id = ServerNet.client.getInputStream().readInt();
				if (id == 0x01) {
					System.out.println("Got Handshake from Client! Now sending World!");
					ServerNet.client.getOutputStream().writeInt(0x04); // Start Send World
					ServerNet.client.getOutputStream().flush();
					for(Point p : Game.game.getWorld().getMap().keySet()) {
						Block b = Game.game.getWorld().getMap().get(p);
						ServerNet.client.getOutputStream().writeInt(0x03); // World Set Packet
						ServerNet.client.getOutputStream().writeInt(p.x);
						ServerNet.client.getOutputStream().writeInt(p.y);
						ServerNet.client.getOutputStream().writeInt(b.getType().ordinal());
						ServerNet.client.getOutputStream().writeInt(b.getHealth());
						ServerNet.client.getOutputStream().flush();
					}
					ServerNet.client.getOutputStream().flush();
					ServerNet.client.getOutputStream().writeInt(0x05); // Finished Send World
					ServerNet.client.getOutputStream().flush();
					System.out.println("Finished Sending World!");
					ServerNet.threadReceive = new ServerThreadReceive();
					ServerNet.threadReceive.start();
					System.out.println("Started Receive Thread!");
				} else {
					System.out.println("Got wrong Handshake from Client!");
				}
			}

		} catch (Exception e) {
			System.out.println("Failed: " + e.getMessage());
		}

	}
}