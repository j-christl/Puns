package me.inplex.puns;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import me.inplex.puns.entity.Player;
import me.inplex.puns.entity.Selector;
import me.inplex.puns.entity.World;
import me.inplex.puns.gfx.DropStage;
import me.inplex.puns.gfx.Menu;
import me.inplex.puns.gfx.Renderer;
import me.inplex.puns.gfx.SpriteSheet;
import me.inplex.puns.input.InputListener;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public static String title = "Puns";
	public static int width = 1000;
	public static int height = width / 16 * 9;
	public static String SPRITES_PATH = "res/sprites.png";
	
	public static Game game;
	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	public long ticks = 0;
	public boolean inMenu = true;
	public boolean isHost;
	
	private InputListener input;
	private Renderer renderer;
	private World world;
	private Player player;
	private Selector selector;
	private int time = 0;
	private boolean timeState = true;
	public int playerStateCount = 0;
	
	public Game() {
		Dimension size = new Dimension(width, height);
		setPreferredSize(size);		
		frame = new JFrame();
		input = new InputListener();
		renderer = new Renderer();
		world = new World(10000);
		player = new Player(world.getSize()/4,400);
		selector = new Selector();
		SpriteSheet.init(SPRITES_PATH);
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle(title + " | " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
	}

	private void update() {
		
		if(inMenu) {
			Menu.update();
			return;
		}
		
		ticks++;
		world.update();
		DropStage.update();
		player.update();
		time++;
		if(time >= 3000) {
			time = 0;
			timeState = !timeState;
		}
		if(!timeState) {
			time-=2;
		}
		
		if (playerStateCount++ > 100)
			playerStateCount = 0;
		
	}
	
	
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		GradientPaint gp = new GradientPaint(getWidth()/2, 0, new Color(0x8FD0FF), getWidth()/2, getHeight(), new Color(0x5CA0CC));
		((Graphics2D)g).setPaint(gp);
		((Graphics2D)g).fillRect(0, 0, getWidth(), getHeight());
		if(inMenu)
			Menu.render(g);
		else
			renderer.render(g);
		g.dispose();
		bs.show();
	}
	
	public void resized() {
		renderer.setRenderDistance(getWidth()/2+20);
	}

	public static void main(String[] args) {
		System.out.println("Starting with " + Runtime.getRuntime().freeMemory()/1048576 + "MB");
		game = new Game();
		game.frame.setTitle(title + " | Starting ..");
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setIconImage((Image)SpriteSheet.GRASS);
		game.frame.setLocationRelativeTo(null);
		game.frame.setResizable(true);
		game.addKeyListener(game.input);
		game.addMouseListener(game.input);
		game.frame.addComponentListener(new ComponentAdapter() {
		    @Override
		    public void componentResized(ComponentEvent e)
		    {
		        game.resized();
		    }
		});
		
		game.frame.setVisible(true);

		game.start();
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public InputListener getInput() {
		return input;
	}

	public Selector getSelector() {
		return selector;
	}

	public void setSelector(Selector selector) {
		this.selector = selector;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
