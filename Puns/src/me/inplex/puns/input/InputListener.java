package me.inplex.puns.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;

import me.inplex.puns.Game;
import me.inplex.puns.gfx.Menu;

public class InputListener implements KeyListener, MouseListener {
	
	private HashSet<Integer> keysDown;
	private HashSet<Integer> mouseDown;
	
	public InputListener() {
		this.keysDown = new HashSet<Integer>();
		this.mouseDown = new HashSet<Integer>();
	}
	
	public boolean isKeyDown(int key) {
		if(keysDown.contains(key))
			return true;
		return false;
	}
	
	public boolean isMouseDown(int mouse) {
		if(mouseDown.contains(mouse))
			return true;
		return false;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		if(Game.game.inMenu) {
			Menu.pressed(e);
			return;
		}
		
		keysDown.add(e.getKeyCode());
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			Game.game.getPlayer().jump();
		}
		if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
			if(Game.game.getSelector().getHeight() < 2)
				Game.game.getSelector().setHeight(Game.game.getSelector().getHeight()+1);
		}
		if(e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(Game.game.getSelector().getHeight() > -1)
				Game.game.getSelector().setHeight(Game.game.getSelector().getHeight()-1);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(keysDown.contains(e.getKeyCode()))
			keysDown.remove(e.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseDown.add(e.getButton());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(mouseDown.contains(e.getButton()))
			mouseDown.remove(e.getButton());
	}

}