package me.inplex.puns.gfx;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import me.inplex.puns.Game;
import me.inplex.puns.net.client.ClientNet;
import me.inplex.puns.net.server.ServerNet;

public class Menu {

	/*
	 * 0 Start 1 Help 2 About 3 Exit
	 */
	static int selection = 0;

	static int minSelection = 0;
	static int maxSelection = 3;

	static int screen = 0;
	
	static String ip = "";

	public static void update() {

	}

	public static void render(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		if (screen == 0) {
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("Serif", Font.BOLD, 128));
			g.drawString("Puns", 50, 120);
			g2d.setFont(new Font("Serif", Font.BOLD, 36));
			g2d.drawString(selection == 0 ? "> START" : "  START", Game.game.getWidth() - 200, Game.game.getHeight() - 250);
			g2d.drawString(selection == 1 ? "> HELP" : "  HELP", Game.game.getWidth() - 200, Game.game.getHeight() - 200);
			g2d.drawString(selection == 2 ? "> ABOUT" : "  ABOUT", Game.game.getWidth() - 200, Game.game.getHeight() - 150);
			g2d.drawString(selection == 3 ? "> EXIT" : "  EXIT", Game.game.getWidth() - 200, Game.game.getHeight() - 100);
		} else if (screen == 1) {
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("Serif", Font.BOLD, 48));
			g.drawString("Move using Arrow Keys", 50, 120);
			g.drawString("Break using Control or Left Mouse Key", 50, 220);
			g.drawString("Build using Shift or Right Mouse Key", 50, 320);
		} else if (screen == 2) {
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("Serif", Font.BOLD, 64));
			g.drawString("'Puns' is a Game.", 50, 120);
		} else if (screen == 3) {
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("Serif", Font.BOLD, 128));
			g.drawString("Puns", 50, 120);
			g2d.setFont(new Font("Serif", Font.BOLD, 36));
			g2d.drawString(selection == 0 ? "> SinglePlayer" : "  SinglePlayer", Game.game.getWidth() - 250, Game.game.getHeight() - 100);
			g2d.drawString(selection == 1 ? "> MultiPlayer" : "  MultiPlayer", Game.game.getWidth() - 250, Game.game.getHeight() - 50);
		} else if (screen == 4) {
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("Serif", Font.BOLD, 128));
			g.drawString("Puns", 50, 120);
			g2d.setFont(new Font("Serif", Font.BOLD, 36));
			g2d.drawString("Enter IP: " + ip, Game.game.getWidth() - 500, Game.game.getHeight() - 100);
		}
	}

	public static void pressed(KeyEvent e) {
		
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_ENTER) {
			if (screen == 0) {
				maxSelection = 3;
				if (selection == 0) {
					screen = 3;
					selection = 0;
					maxSelection = 1;
				} else if (selection == 1) {
					screen = 1;
				} else if (selection == 2) {
					screen = 2;
				} else if (selection == 3) {
					Game.game.stop();
				}
			} else if(screen == 3) {
				maxSelection = 1;
				if(selection == 0) {
					ServerNet.host();
					Game.game.inMenu = false;
					return;
				} else if(selection == 1) {
					screen = 4;
				}
			}  else if(screen == 4) {
				ClientNet.connect(ip);
			}
		}
		if (key == KeyEvent.VK_ESCAPE) {
			if (screen == 0) {
				Game.game.stop();
			} else {
				screen = 0;
				maxSelection = 3;
			}
		}
		if(screen == 4) {
			ip = ip.replaceAll("\n", "");
			if(key == KeyEvent.VK_BACK_SPACE) {
				if(ip.length() > 0) {
					ip = ip.substring(0, ip.length()-1);
				}
			} else
				ip += e.getKeyChar();
		}
		
		if (key == KeyEvent.VK_DOWN) {
			if (selection < maxSelection) {
				selection++;
			}
		}
		if (key == KeyEvent.VK_UP) {
			if (selection > minSelection) {
				selection--;
			}
		}
		
	}

}