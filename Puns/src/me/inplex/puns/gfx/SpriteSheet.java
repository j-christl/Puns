package me.inplex.puns.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.inplex.puns.entity.BlockType;

public class SpriteSheet {

	public static BufferedImage image;

	public static BufferedImage PLAYER_IDLE_1_RIGHT;
	public static BufferedImage PLAYER_IDLE_2_RIGHT;
	public static BufferedImage PLAYER_IDLE_3_RIGHT;
	public static BufferedImage PLAYER_WALK_1_RIGHT;
	public static BufferedImage PLAYER_WALK_2_RIGHT;

	public static BufferedImage PLAYER_IDLE_1_LEFT;
	public static BufferedImage PLAYER_IDLE_2_LEFT;
	public static BufferedImage PLAYER_IDLE_3_LEFT;
	public static BufferedImage PLAYER_WALK_1_LEFT;
	public static BufferedImage PLAYER_WALK_2_LEFT;
	
	public static BufferedImage PLAYER_ATTACK_RIGHT;
	public static BufferedImage PLAYER_ATTACK_LEFT;
	
	public static BufferedImage PLAYER_JUMP_RIGHT;
	public static BufferedImage PLAYER_JUMP_LEFT;
	
	public static BufferedImage ZOMBIE_IDLE_1_RIGHT;
	public static BufferedImage ZOMBIE_IDLE_2_RIGHT;
	public static BufferedImage ZOMBIE_IDLE_3_RIGHT;
	public static BufferedImage ZOMBIE_WALK_1_RIGHT;
	public static BufferedImage ZOMBIE_WALK_2_RIGHT;

	public static BufferedImage ZOMBIE_IDLE_1_LEFT;
	public static BufferedImage ZOMBIE_IDLE_2_LEFT;
	public static BufferedImage ZOMBIE_IDLE_3_LEFT;
	public static BufferedImage ZOMBIE_WALK_1_LEFT;
	public static BufferedImage ZOMBIE_WALK_2_LEFT;
	
	public static BufferedImage ZOMBIE_ATTACK_RIGHT;
	public static BufferedImage ZOMBIE_ATTACK_LEFT;
	
	public static BufferedImage ZOMBIE_JUMP_RIGHT;
	public static BufferedImage ZOMBIE_JUMP_LEFT;

	public static BufferedImage GRASS;
	public static BufferedImage DIRT;
	public static BufferedImage STONE;
	public static BufferedImage WOOD;
	public static BufferedImage LEAF;
	public static BufferedImage WATER;
	public static BufferedImage CLOUD;

	public static void init(String path) {
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}

		PLAYER_IDLE_1_RIGHT = image.getSubimage(0, 0, 20, 40);
		PLAYER_IDLE_2_RIGHT = image.getSubimage(20, 0, 20, 40);
		PLAYER_IDLE_3_RIGHT = image.getSubimage(40, 0, 20, 40);
		PLAYER_WALK_1_RIGHT = image.getSubimage(60, 0, 20, 40);
		PLAYER_WALK_2_RIGHT = image.getSubimage(80, 0, 20, 40);
		PLAYER_ATTACK_RIGHT = image.getSubimage(100, 0, 20, 40);
		PLAYER_JUMP_RIGHT = image.getSubimage(120, 0, 20, 40);

		PLAYER_IDLE_1_LEFT = horizontalflip(PLAYER_IDLE_1_RIGHT);
		PLAYER_IDLE_2_LEFT = horizontalflip(PLAYER_IDLE_2_RIGHT);
		PLAYER_IDLE_3_LEFT = horizontalflip(PLAYER_IDLE_3_RIGHT);
		PLAYER_WALK_1_LEFT = horizontalflip(PLAYER_WALK_1_RIGHT);
		PLAYER_WALK_2_LEFT = horizontalflip(PLAYER_WALK_2_RIGHT);
		PLAYER_ATTACK_LEFT = horizontalflip(PLAYER_ATTACK_RIGHT);
		PLAYER_JUMP_LEFT = horizontalflip(PLAYER_JUMP_RIGHT);
		
		ZOMBIE_IDLE_1_RIGHT = image.getSubimage(0, 80, 20, 40);
		ZOMBIE_IDLE_2_RIGHT = image.getSubimage(20, 80, 20, 40);
		ZOMBIE_IDLE_3_RIGHT = image.getSubimage(40, 80, 20, 40);
		ZOMBIE_WALK_1_RIGHT = image.getSubimage(60, 80, 20, 40);
		ZOMBIE_WALK_2_RIGHT = image.getSubimage(80, 80, 20, 40);
		ZOMBIE_ATTACK_RIGHT = image.getSubimage(100, 80, 20, 40);
		ZOMBIE_JUMP_RIGHT = image.getSubimage(120, 80, 20, 40);
		
		ZOMBIE_IDLE_1_LEFT = horizontalflip(ZOMBIE_IDLE_1_RIGHT);
		ZOMBIE_IDLE_2_LEFT = horizontalflip(ZOMBIE_IDLE_2_RIGHT);
		ZOMBIE_IDLE_3_LEFT = horizontalflip(ZOMBIE_IDLE_3_RIGHT);
		ZOMBIE_WALK_1_LEFT = horizontalflip(ZOMBIE_WALK_1_RIGHT);
		ZOMBIE_WALK_2_LEFT = horizontalflip(ZOMBIE_WALK_2_RIGHT);
		ZOMBIE_ATTACK_LEFT = horizontalflip(ZOMBIE_ATTACK_RIGHT);
		ZOMBIE_JUMP_LEFT = horizontalflip(ZOMBIE_JUMP_RIGHT);

		GRASS = image.getSubimage(0, 40, 20, 20);
		DIRT = image.getSubimage(20, 40, 20, 20);
		STONE = image.getSubimage(0, 60, 20, 20);
		WOOD = image.getSubimage(40, 40, 20, 20);
		LEAF = image.getSubimage(60, 40, 20, 20);
		WATER = image.getSubimage(80, 40, 20, 20);
		CLOUD = image.getSubimage(100, 40, 20, 20);

		BlockType.loadImages();

	}

	public static BufferedImage horizontalflip(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(w, h, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
		g.dispose();
		return dimg;
	}

	public static BufferedImage getImage() {
		return image;
	}

}
