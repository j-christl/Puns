package me.inplex.puns.entity;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import me.inplex.puns.gfx.SpriteSheet;

public enum BlockType {

	GRASS(true, 10), DIRT(true, 10), STONE(true, 20), WOOD(true, 10), LEAF(true, 2), WATER(false, 10000), CLOUD(false, 2);

	private boolean solid;
	private int health;
	private BufferedImage image;

	private BlockType(boolean solid, int health) {
		this.solid = solid;
		this.health = health;
	}

	public static void loadImages() {
		for (int i = 0; i < values().length; i++) {
			BlockType b = values()[i];
			try {
				Field f = SpriteSheet.class.getField(b.name());
				if (f.getType() == BufferedImage.class) {
					b.image = (BufferedImage) f.get(null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isSolid() {
		return solid;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getHealth() {
		return health;
	}

}