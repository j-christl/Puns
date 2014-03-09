package me.inplex.puns.entity;

public class Block {
	
	private int width = 20, height = 20;
	private BlockType type;
	private int health;
	
	public Block(BlockType type) {
		this.type = type;
		this.health = type.getHealth();
	}
	
	public Block getHalfDamaged() {
		health /= 2;
		return this;
	}
	
	public BlockType getType() {
		return type;
	}

	public void setType(BlockType type) {
		this.type = type;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
}