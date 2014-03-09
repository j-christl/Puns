package me.inplex.puns.entity;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Stack;

import me.inplex.puns.Game;
import me.inplex.puns.gfx.DropStage;
import me.inplex.puns.net.client.ClientNet;
import me.inplex.puns.net.server.ServerNet;

public class Player {

	private int x;
	private int y;
	private EntityState state;
	private Direction direction;
	private int speed;
	private final int normalSpeed = 1;

	private int height = 40, width = 20;

	private Stack<Drop> pocket;

	public Player(int x, int y) {
		this.x = x;
		this.y = y;
		this.setState(EntityState.IDLE);
		speed = normalSpeed;
		setPocket(new Stack<Drop>());
	}

	int motionY = 0, motionX = 0;

	public void update() {

		if (Game.game.getInput().isKeyDown(KeyEvent.VK_LEFT) || Game.game.getInput().isKeyDown(KeyEvent.VK_A)) {
			move(Direction.LEFT);
			state = EntityState.WALK;
			setDirection(Direction.LEFT);
		} else if (Game.game.getInput().isKeyDown(KeyEvent.VK_RIGHT) || Game.game.getInput().isKeyDown(KeyEvent.VK_D)) {
			move(Direction.RIGHT);
			state = EntityState.WALK;
			setDirection(Direction.RIGHT);
		} else {
			motionX = 0;
			state = EntityState.IDLE;
		}

		if (motionY > 5) {
			state = EntityState.JUMP;
		}

		if (motionY > 0 || motionY < -15) {
			try {
				if (Game.game.isHost) {
					if (ServerNet.client != null) {
						ServerNet.client.getOutputStream().flush();
						ServerNet.client.getOutputStream().writeInt(0x02);
						ServerNet.client.getOutputStream().writeInt(x);
						ServerNet.client.getOutputStream().writeInt(y);
					}
				} else {
					ClientNet.outputStream.flush();
					ClientNet.outputStream.writeInt(0x02);
					ClientNet.outputStream.writeInt(x);
					ClientNet.outputStream.writeInt(y);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (Game.game.getInput().isKeyDown(KeyEvent.VK_CONTROL) || Game.game.getInput().isMouseDown(MouseEvent.BUTTON1)) {
			state = EntityState.ATTACK;
			Point p = new Point(direction == Direction.RIGHT ? x + 20 : x - 20, y + 20 * Game.game.getSelector().getHeight() - 20);
			p.x = (int) Math.round(((double) p.x) / 20) * 20;
			p.y = (int) Math.round(((double) p.y) / 20) * 20;
			if (Game.game.getWorld().getBlock(p) != null) {
				Game.game.getWorld().damageBlock(p);
			}
		}
		if (Game.game.getInput().isKeyDown(KeyEvent.VK_SHIFT) || Game.game.getInput().isMouseDown(MouseEvent.BUTTON3)) {
			if (!pocket.isEmpty()) {
				Point p = new Point(direction == Direction.RIGHT ? x + 20 : x - 20, y + 20 * Game.game.getSelector().getHeight() - 20);
				p.x = (int) Math.round(((double) p.x) / 20) * 20;
				p.y = (int) Math.round(((double) p.y) / 20) * 20;
				if (Game.game.getWorld().getBlock(p) == null) {
					Drop d = pocket.pop();
					Block b = new Block(d.getType());
					Game.game.getWorld().getMap().put(p, b);
					DropStage.getMap().put(d, -1);
					
					try {
						if (Game.game.isHost) {
							if (ServerNet.client != null) {
								ServerNet.client.getOutputStream().flush();
								ServerNet.client.getOutputStream().writeInt(0x03);
								ServerNet.client.getOutputStream().writeInt(p.x);
								ServerNet.client.getOutputStream().writeInt(p.y);
								ServerNet.client.getOutputStream().writeInt(b.getType().ordinal());
								ServerNet.client.getOutputStream().writeInt(b.getHealth());
							}
						} else {
							ClientNet.outputStream.flush();
							ClientNet.outputStream.writeInt(0x03);
							ClientNet.outputStream.writeInt(p.x);
							ClientNet.outputStream.writeInt(p.y);
							ClientNet.outputStream.writeInt(b.getType().ordinal());
							ClientNet.outputStream.writeInt(b.getHealth());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
		}

		if (!collidesWithBlock()) {
			final boolean dCollide = collidesWithBlock();
			final int dX = x;
			final int dY = y;
			y += motionY / 7;
			motionY -= 2;
			if (!dCollide && collidesWithBlock()) {
				x = dX;
				y = dY;
				motionY = 0;
			}

		} else {
			motionY = 0;
		}

		collectDrops();

	}

	public void jump() {
		y -= 2;
		if (collidesWithBlock()) {
			y += 5;
			motionY = 50;
		}
		y += 2;
	}

	private void move(Direction dir) {
		final boolean dCollide = collidesWithBlock();
		final int dX = x;
		final int dY = y;

		motionX++;
		switch (dir) {
			case LEFT:
				x -= speed + motionX / 20;
				break;
			case RIGHT:
				x += speed + motionX / 20;
				break;
		}

		if (!dCollide && collidesWithBlock()) {
			x = dX;
			y = dY;
			motionX = 0;
		} else {
			try {
				if (Game.game.isHost) {
					if (ServerNet.client != null) {
						ServerNet.client.getOutputStream().flush();
						ServerNet.client.getOutputStream().writeInt(0x02);
						ServerNet.client.getOutputStream().writeInt(x);
						ServerNet.client.getOutputStream().writeInt(y);
					}
				} else {
					ClientNet.outputStream.flush();
					ClientNet.outputStream.writeInt(0x02);
					ClientNet.outputStream.writeInt(x);
					ClientNet.outputStream.writeInt(y);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean collidesWithBlock() {
		Rectangle r1 = new Rectangle(x, y - height / 2, width, height);
		int distance = 40;
		for (int y = (int) Math.round(((double) (Game.game.getPlayer().getY() - distance)) / 20) * 20; y < Game.game.getPlayer().getY() + distance; y += 20) {
			for (int x = (int) Math.round(((double) Game.game.getPlayer().getX() - distance) / 20) * 20; x < Game.game.getPlayer().getX() + distance; x += 20) {
				Point p = new Point(x, y);
				Block b = Game.game.getWorld().getBlock(p);
				if(b == null)
					continue;
				Rectangle r2 = new Rectangle(p.getX() + 1, p.getY() + 1, b.getWidth() - 2, b.getHeight() - 2);
				if (r1.intersects(r2) && b.getType().isSolid() && b.getHealth() > b.getType().getHealth() / 2) {
					return true;
				}
			}
		}
		return false;
	}

	private void collectDrops() {
		try {
			Rectangle r1 = new Rectangle(x, y - height / 2, width, height);
			HashSet<Drop> drops = Game.game.getWorld().getDrops();
			for (Drop d : drops) {
				Rectangle r2 = new Rectangle(d.getX(), d.getY(), d.getWidth(), d.getHeight());
				if (r1.intersects(r2)) {
					pocket.push(d);
					DropStage.addDrop(d);
					Game.game.getWorld().getDrops().remove(d);
				}
			}
		} catch (ConcurrentModificationException e) {
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public EntityState getState() {
		return state;
	}

	public void setState(EntityState state) {
		this.state = state;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Stack<Drop> getPocket() {
		return pocket;
	}

	public void setPocket(Stack<Drop> pocket) {
		this.pocket = pocket;
	}

}