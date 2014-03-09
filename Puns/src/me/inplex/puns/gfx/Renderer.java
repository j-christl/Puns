package me.inplex.puns.gfx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;
import java.util.ListIterator;

import me.inplex.puns.Game;
import me.inplex.puns.entity.Block;
import me.inplex.puns.entity.Direction;
import me.inplex.puns.entity.Drop;
import me.inplex.puns.entity.Point;
import me.inplex.puns.net.client.ClientNet;
import me.inplex.puns.net.server.ServerNet;

public class Renderer {

	private int renderDistance;

	public Renderer() {
		renderDistance = 100;
	}

	public void render(Graphics g) {

		for (Drop d : DropStage.getMap().keySet()) {
			int time = DropStage.getMap().get(d);
			if (Game.game.ticks % 3 == 0) {
				if (time >= 0) {
					DropStage.getMap().put(d, ++time);
				} else {
					DropStage.getMap().put(d, --time);
				}
			}
			int pos = Game.game.getWidth() - 200 + time;
			if (time < 0) {
				pos = Game.game.getWidth() + time - 100;
			}
			g.drawImage(d.getType().getImage(), pos, 10, null);
		}

		int count = 1;
		ListIterator<Drop> li = Game.game.getPlayer().getPocket().listIterator(Game.game.getPlayer().getPocket().size());
		while (li.hasPrevious()) {
			Drop d = li.previous();
			g.drawImage(d.getType().getImage(), Game.game.getWidth() - 100 + count++ * 10, 10, null);
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(-Game.game.getPlayer().getX() + Game.game.getWidth() / 2, -Game.game.getHeight() / 2 + Game.game.getPlayer().getY() + 50);
		
		for (int y = (int) Math.round(((double) (Game.game.getPlayer().getY() - renderDistance)) / 20) * 20; y < Game.game.getPlayer().getY() + renderDistance; y += 20) {
			for (int x = (int) Math.round(((double) Game.game.getPlayer().getX() - renderDistance) / 20) * 20; x < Game.game.getPlayer().getX() + renderDistance; x += 20) {
				Point p = new Point(x, y);
				Block b = Game.game.getWorld().getMap().get(p);
				if (b == null)
					continue;
				g.drawImage(b.getType().getImage(), p.x, Game.game.getHeight() - p.y, null);
				if (b.getHealth() <= b.getType().getHealth() / 2) {
					g.setColor(new Color(1.0f, 1.0f, 1.0f, 0.2f));
					g.fillRect(p.x, Game.game.getHeight() - p.y, b.getWidth(), b.getHeight());
				}
			}
		}

		for (Drop d : Game.game.getWorld().getDrops()) {
			g.drawImage(d.getImage(), d.getX(), Game.game.getHeight() - d.getY(), null);
		}

		Direction pDir = Game.game.getPlayer().getDirection();
		BufferedImage pImage = null;
		switch (Game.game.getPlayer().getState()) {
			case IDLE:
				if (Game.game.playerStateCount > 60) {
					pImage = pDir == Direction.RIGHT ? SpriteSheet.PLAYER_IDLE_3_RIGHT : SpriteSheet.PLAYER_IDLE_3_LEFT;
				} else if (Game.game.playerStateCount > 30) {
					pImage = pDir == Direction.RIGHT ? SpriteSheet.PLAYER_IDLE_2_RIGHT : SpriteSheet.PLAYER_IDLE_2_LEFT;
				} else {
					pImage = pDir == Direction.RIGHT ? SpriteSheet.PLAYER_IDLE_1_RIGHT : SpriteSheet.PLAYER_IDLE_1_LEFT;
				}
				break;
			case WALK:
				if (Game.game.playerStateCount > 50) {
					pImage = pDir == Direction.RIGHT ? SpriteSheet.PLAYER_WALK_2_RIGHT : SpriteSheet.PLAYER_WALK_2_LEFT;
				} else {
					pImage = pDir == Direction.RIGHT ? SpriteSheet.PLAYER_WALK_1_RIGHT : SpriteSheet.PLAYER_WALK_1_LEFT;
				}
				break;
			case ATTACK:
				pImage = pDir == Direction.RIGHT ? SpriteSheet.PLAYER_ATTACK_RIGHT : SpriteSheet.PLAYER_ATTACK_LEFT;
				break;
			case JUMP:
				pImage = pDir == Direction.RIGHT ? SpriteSheet.PLAYER_JUMP_RIGHT : SpriteSheet.PLAYER_JUMP_LEFT;
				break;
		}

		Point p = new Point(Game.game.getPlayer().getDirection() == Direction.RIGHT ? Game.game.getPlayer().getX() + 20 : Game.game.getPlayer()
				.getX() - 20, Game.game.getPlayer().getY() + 20 * Game.game.getSelector().getHeight() - 20);
		p.x = (int) Math.round(((double) p.x) / 20) * 20;
		p.y = (int) Math.round(((double) p.y) / 20) * 20;
		g.setColor(Game.game.getWorld().getBlock(p) != null ? Color.BLACK : Color.GRAY);
		g.drawRect(p.x, Game.game.getHeight() - p.y, 20, 20);

		g.drawImage(pImage, Game.game.getPlayer().getX(), Game.game.getHeight() - Game.game.getPlayer().getY(), null);

		if (Game.game.isHost) {
			if (ServerNet.client != null) {
				g.drawImage(SpriteSheet.PLAYER_IDLE_1_RIGHT, ServerNet.client.getX(), Game.game.getHeight() - ServerNet.client.getY(), null);
			}
		} else {
			g.drawImage(SpriteSheet.PLAYER_IDLE_1_RIGHT, ClientNet.serverX, Game.game.getHeight() - ClientNet.serverY, null);
		}

	}

	public void oldRender(Graphics g) {

		for (Drop d : DropStage.getMap().keySet()) {
			int time = DropStage.getMap().get(d);
			if (Game.game.ticks % 3 == 0) {
				if (time >= 0) {
					DropStage.getMap().put(d, ++time);
				} else {
					DropStage.getMap().put(d, --time);
				}
			}
			int pos = Game.game.getWidth() - 200 + time;
			if (time < 0) {
				pos = Game.game.getWidth() + time - 100;
			}
			g.drawImage(d.getType().getImage(), pos, 10, null);
		}

		int count = 1;
		ListIterator<Drop> li = Game.game.getPlayer().getPocket().listIterator(Game.game.getPlayer().getPocket().size());
		while (li.hasPrevious()) {
			Drop d = li.previous();
			g.drawImage(d.getType().getImage(), Game.game.getWidth() - 100 + count++ * 10, 10, null);
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(-Game.game.getPlayer().getX() + Game.game.getWidth() / 2, -Game.game.getHeight() / 2 + Game.game.getPlayer().getY() + 50);

		try {
			for (Point p : Game.game.getWorld().getMap().keySet()) {
				if (p.distanceX(new Point(Game.game.getPlayer().getX(), Game.game.getPlayer().getY())) > renderDistance
						|| p.distanceY(new Point(Game.game.getPlayer().getX(), Game.game.getPlayer().getY())) > (int) renderDistance / 1.5)
					continue;
				Block b = Game.game.getWorld().getMap().get(p);
				g.drawImage(b.getType().getImage(), p.x, Game.game.getHeight() - p.y, null);
				if (b.getHealth() <= b.getType().getHealth() / 2) {
					g.setColor(new Color(1.0f, 1.0f, 1.0f, 0.2f));
					g.fillRect(p.x, Game.game.getHeight() - p.y, b.getWidth(), b.getHeight());
				}
			}
		} catch (ConcurrentModificationException e) {
			// When 0x03 Packet is received while iterating through map
		}

		for (Drop d : Game.game.getWorld().getDrops()) {
			g.drawImage(d.getImage(), d.getX(), Game.game.getHeight() - d.getY(), null);
		}

		Direction pDir = Game.game.getPlayer().getDirection();
		BufferedImage pImage = null;
		switch (Game.game.getPlayer().getState()) {
			case IDLE:
				if (Game.game.playerStateCount > 60) {
					pImage = pDir == Direction.RIGHT ? SpriteSheet.PLAYER_IDLE_3_RIGHT : SpriteSheet.PLAYER_IDLE_3_LEFT;
				} else if (Game.game.playerStateCount > 30) {
					pImage = pDir == Direction.RIGHT ? SpriteSheet.PLAYER_IDLE_2_RIGHT : SpriteSheet.PLAYER_IDLE_2_LEFT;
				} else {
					pImage = pDir == Direction.RIGHT ? SpriteSheet.PLAYER_IDLE_1_RIGHT : SpriteSheet.PLAYER_IDLE_1_LEFT;
				}
				break;
			case WALK:
				if (Game.game.playerStateCount > 50) {
					pImage = pDir == Direction.RIGHT ? SpriteSheet.PLAYER_WALK_2_RIGHT : SpriteSheet.PLAYER_WALK_2_LEFT;
				} else {
					pImage = pDir == Direction.RIGHT ? SpriteSheet.PLAYER_WALK_1_RIGHT : SpriteSheet.PLAYER_WALK_1_LEFT;
				}
				break;
			case ATTACK:
				pImage = pDir == Direction.RIGHT ? SpriteSheet.PLAYER_ATTACK_RIGHT : SpriteSheet.PLAYER_ATTACK_LEFT;
				break;
			case JUMP:
				pImage = pDir == Direction.RIGHT ? SpriteSheet.PLAYER_JUMP_RIGHT : SpriteSheet.PLAYER_JUMP_LEFT;
				break;
		}

		Point p = new Point(Game.game.getPlayer().getDirection() == Direction.RIGHT ? Game.game.getPlayer().getX() + 20 : Game.game.getPlayer()
				.getX() - 20, Game.game.getPlayer().getY() + 20 * Game.game.getSelector().getHeight() - 20);
		p.x = (int) Math.round(((double) p.x) / 20) * 20;
		p.y = (int) Math.round(((double) p.y) / 20) * 20;
		g.setColor(Game.game.getWorld().getBlock(p) != null ? Color.BLACK : Color.GRAY);
		g.drawRect(p.x, Game.game.getHeight() - p.y, 20, 20);

		g.drawImage(pImage, Game.game.getPlayer().getX(), Game.game.getHeight() - Game.game.getPlayer().getY(), null);

		if (Game.game.isHost) {
			if (ServerNet.client != null) {
				g.drawImage(SpriteSheet.PLAYER_IDLE_1_RIGHT, ServerNet.client.getX(), Game.game.getHeight() - ServerNet.client.getY(), null);
			}
		} else {
			g.drawImage(SpriteSheet.PLAYER_IDLE_1_RIGHT, ClientNet.serverX, Game.game.getHeight() - ClientNet.serverY, null);
		}

	}

	public int getRenderDistance() {
		return renderDistance;
	}

	public void setRenderDistance(int renderDistance) {
		this.renderDistance = renderDistance;
	}

}