package me.inplex.puns.net.server;

import java.io.IOException;

import me.inplex.puns.Game;
import me.inplex.puns.entity.Block;
import me.inplex.puns.entity.BlockType;
import me.inplex.puns.entity.Point;

public class ServerThreadReceive extends Thread {

	@Override
	public void run() {
		while (true) {
			try {
				if (ServerNet.client == null)
					continue;
				if (ServerNet.client.getInputStream().available() > 0) {
					int id = ServerNet.client.getInputStream().readInt();
					if (id == 0x02) { // Position
						int x = ServerNet.client.getInputStream().readInt();
						int y = ServerNet.client.getInputStream().readInt();
						ServerNet.client.setX(x);
						ServerNet.client.setY(y);
					} else if (id == 0x03) { // World Set
						int x = ServerNet.client.getInputStream().readInt();
						int y = ServerNet.client.getInputStream().readInt();
						BlockType type = BlockType.values()[ServerNet.client.getInputStream().readInt()];
						int health = ServerNet.client.getInputStream().readInt();

						if (health <= 0) {
							if (Game.game.getWorld().getMap().containsKey(new Point(x, y)))
								Game.game.getWorld().getMap().remove(new Point(x, y));
						} else {
							Block b = new Block(type);
							b.setHealth(health);
							Game.game.getWorld().getMap().put(new Point(x, y), b);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}