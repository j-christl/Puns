package me.inplex.puns.entity;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import me.inplex.puns.Game;
import me.inplex.puns.net.client.ClientNet;
import me.inplex.puns.net.server.ServerNet;

public class World {

	private int size;
	private HashMap<Point, Block> map;
	private HashSet<Drop> drops;

	public World(int size) {
		this.size = size;
		this.map = new HashMap<Point, Block>();
		this.drops = new HashSet<Drop>();
		generate();
	}

	private void generate() {
		int caveBase = 0;
		int grassBase = 0;
		for (int x = -(size / 2); x < (size / 2); x += 20) {

			if (new Random().nextInt(10) == 1 && grassBase < 15)
				grassBase++;
			else if (new Random().nextInt(10) == 2 && grassBase > -3)
				grassBase--;

			int yForX = getYFor(x) + grassBase * 20;
			map.put(new Point(x, yForX), new Block(BlockType.DIRT));
			map.put(new Point(x, yForX + 20), new Block(BlockType.DIRT));
			map.put(new Point(x, yForX + 40), new Block(BlockType.GRASS));
			yForX += 40;
			for (int i = yForX - 60; i > -1000; i -= 20) {
				map.put(new Point(x, i), new Block(BlockType.STONE));
			}
			if (new Random().nextInt(10) == 1) {
				int treeH = new Random().nextInt(10) + 1;
				for (int i = yForX + 20; i < yForX + 20 + 20 * treeH; i += 20) {
					map.put(new Point(x, i), new Block(BlockType.WOOD));
				}
				map.put(new Point(x - 20, yForX + 20 + 20 * treeH), new Block(BlockType.LEAF));
				map.put(new Point(x, yForX + 20 + 20 * treeH), new Block(BlockType.LEAF));
				map.put(new Point(x + 20, yForX + 20 + 20 * treeH), new Block(BlockType.LEAF));
				map.put(new Point(x, yForX + 20 + 20 * treeH + 20), new Block(BlockType.LEAF));
			}
			if (new Random().nextInt(10) == 1) {
				int cloudH = new Random().nextInt(50) + 10;
				map.put(new Point(x - 20, yForX + 20 + 20 * cloudH), new Block(BlockType.CLOUD));
				map.put(new Point(x, yForX + 20 + 20 * cloudH), new Block(BlockType.CLOUD));
				map.put(new Point(x + 20, yForX + 20 + 20 * cloudH), new Block(BlockType.CLOUD));
				map.put(new Point(x, yForX + 20 + 20 * cloudH + 20), new Block(BlockType.CLOUD));
				map.put(new Point(x, yForX - 20 + 20 * cloudH + 20), new Block(BlockType.CLOUD));
			}

			if (new Random().nextInt(30) == 1 && caveBase < 7)
				caveBase++;
			else if (new Random().nextInt(30) == 2 && caveBase > -5)
				caveBase--;

			int cave = new Random().nextInt(2) + 1 + caveBase;
			cave *= 20;
			cave -= 300;
			map.put(new Point(x, cave), new Block(BlockType.STONE).getHalfDamaged());
			map.put(new Point(x, cave + 20), new Block(BlockType.STONE).getHalfDamaged());
			map.put(new Point(x, cave + 40), new Block(BlockType.STONE).getHalfDamaged());
			map.put(new Point(x, cave + 60), new Block(BlockType.STONE).getHalfDamaged());

		}

	}

	private int getYFor(int x) {
		int y = (int) (Math.sin(x) + Math.sin(x / 8) * 5 + Math.sin(x / 33) * 20);
		y = (int) Math.round(((double) y) / 20) * 20;
		return y;
	}

	public int getTop(int x) {
		int top = 0;
		for (Point p : map.keySet()) {
			if (p.getX() == x) {
				if (p.getY() > top) {
					top = p.getY();
				}
			}
		}
		return top;
	}

	public void damageBlock(Point pos) {
		Block b = getBlock(pos);
		if (b == null)
			return;
		b.setHealth(b.getHealth() - 1);

		try {
			if (Game.game.isHost) {
				if (ServerNet.client != null) {
					ServerNet.client.getOutputStream().flush();
					ServerNet.client.getOutputStream().writeInt(0x03);
					ServerNet.client.getOutputStream().writeInt(pos.x);
					ServerNet.client.getOutputStream().writeInt(pos.y);
					ServerNet.client.getOutputStream().writeInt(b.getType().ordinal());
					ServerNet.client.getOutputStream().writeInt(b.getHealth());
				}
			} else {
				ClientNet.outputStream.flush();
				ClientNet.outputStream.writeInt(0x03);
				ClientNet.outputStream.writeInt(pos.x);
				ClientNet.outputStream.writeInt(pos.y);
				ClientNet.outputStream.writeInt(b.getType().ordinal());
				ClientNet.outputStream.writeInt(b.getHealth());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (b.getHealth() <= 0) {
			Drop d = new Drop(pos.x + 7, pos.y - 7, map.get(pos).getType());
			drops.add(d);
			map.remove(pos);
		}
	}

	public void update() {
		for (Drop d : drops) {
			d.update();
		}
	}

	public void setBlock(Point pos, Block block) {
		map.put(pos, block);
	}

	public Block getBlock(Point pos) {
		return map.get(pos);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public HashMap<Point, Block> getMap() {
		return map;
	}

	public void addDrop(Drop d) {
		drops.add(d);
	}

	public HashSet<Drop> getDrops() {
		return drops;
	}

	public void setDrops(HashSet<Drop> drops) {
		this.drops = drops;
	}

}