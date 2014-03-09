package me.inplex.puns.net.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import me.inplex.puns.Game;
import me.inplex.puns.entity.Block;
import me.inplex.puns.entity.BlockType;
import me.inplex.puns.entity.Point;

public class ClientThreadReceive extends Thread {
	
	private String ip;
	
	public ClientThreadReceive(String ip) {
		this.ip = ip;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Connecting to " + ip + " with port " + ClientNet.port);
			@SuppressWarnings("resource")
			Socket s = new Socket(ip, ClientNet.port);
			System.out.println("Connected!");
			ClientNet.inputStream = new DataInputStream(s.getInputStream());
			ClientNet.outputStream = new DataOutputStream(s.getOutputStream());
			System.out.println("Handshaking ..");
			ClientNet.outputStream.writeInt(0x01);
			System.out.println("Sent Handshake!");
			int response = ClientNet.inputStream.readInt();
			if(response == 0x01) {
				System.out.println("Got wrong Handshake response!");
				return;
			}
			System.out.println("Got Handshake!");
			while(true) {
				if(ClientNet.inputStream.available() > 0) {
					int id = ClientNet.inputStream.readInt();
					System.out.println("Got Packet ID: " + id);
					if(id == 0x02) { // Set Position
						int x = ClientNet.inputStream.readInt();
						int y = ClientNet.inputStream.readInt();
						ClientNet.serverX = x;
						ClientNet.serverY = y;
					} else if (id == 0x03) { // World Set						
						int x = ClientNet.inputStream.readInt();
						int y = ClientNet.inputStream.readInt();
						BlockType type = BlockType.values()[ClientNet.inputStream.readInt()];
						int health = ClientNet.inputStream.readInt();
						
						if (health <= 0) {
							if (Game.game.getWorld().getMap().containsKey(new Point(x, y)))
								Game.game.getWorld().getMap().remove(new Point(x, y));
						} else {
							Block b = new Block(type);
							b.setHealth(health);
							Game.game.getWorld().getMap().put(new Point(x, y), b);
						}
					} else if (id == 0x04) { // World Send Start
						System.out.println("Starting Getting World!");
					} else if (id == 0x05) { // World Send Finished
						Game.game.inMenu = false;
						System.out.println("Finished Getting World!");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
}