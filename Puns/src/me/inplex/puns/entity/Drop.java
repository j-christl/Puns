package me.inplex.puns.entity;

import java.awt.image.BufferedImage;

import me.inplex.puns.Game;

public class Drop {

	private int x;
	private int y;
	private int width = 5, height = 5;
	private BlockType type;
	private BufferedImage image;

	public Drop(int x, int y, BlockType type) {
		this.x = x;
		this.y = y;
		this.type = type;
		loadImage();
	}

	public void update() {
		int pX = Game.game.getPlayer().getX();
		int pY = Game.game.getPlayer().getY();
		if (x > pX)
			x--;
		if (x < pX)
			x++;
		if (y > pY)
			y--;
		if (y < pY)
			y++;
	}

	private void loadImage() {
		image = type.getImage().getSubimage(0, 0, 5, 5);
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

	public BlockType getType() {
		return type;
	}

	public void setType(BlockType type) {
		this.type = type;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}