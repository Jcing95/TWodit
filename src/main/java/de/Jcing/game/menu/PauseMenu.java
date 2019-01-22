package de.jcing.game.menu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import de.jcing.Main;
import de.jcing.engine.io.KeyBoard;
import de.jcing.window.Window;
import de.jcing.window.gui.Button;
import de.jcing.window.gui.Canvas;
import de.jcing.window.gui.Container;

public class PauseMenu extends Container{
	
	
	Button resume, exit;
	Canvas background;
	
	public PauseMenu() {
		super(0,0,Window.PIXEL_WIDTH,Window.PIXEL_HEIGHT);
		resume = new Button("   R E S U M E   ",Window.PIXEL_WIDTH/2,Window.PIXEL_HEIGHT/3);
		exit = new Button("   E X I T   ",Window.PIXEL_WIDTH/2,Window.PIXEL_HEIGHT/3*2);
		background = new Canvas(0, 0, Window.PIXEL_WIDTH, Window.PIXEL_HEIGHT);
		Graphics2D bg = background.getGraphics();
		bg.setColor(new Color(0,0,0,0.45f));
		bg.fillRect(0, 0, Window.PIXEL_WIDTH, Window.PIXEL_HEIGHT);
		bg.dispose();
		resume.centerHorizontal(true);
		resume.centerVertical(true);
		exit.centerHorizontal(true);
		exit.centerVertical(true);
		
		KeyBoard.resetKey(KeyEvent.VK_P);
		
		resume.getOnClick().add(() -> { 
			unlisten();
			Main.getWindow().removeDrawable(this);
			Main.getGame().pause(false);
		});
		
		exit.getOnClick().add(() -> Main.finish());
		resume.setColors(new Color(10,150,10), new Color(40,185,40), new Color(5,100,5), new Color(230,230,230));
		exit.setColors(new Color(150,10,10), new Color(185,40,40), new Color(100,5,5), new Color(0,0,0));
		
		addComponent(background);
		addComponent(resume);
		addComponent(exit);
		listenOnMouse();
		
		Main.getWindow().addDrawable(this);
	}
	
	
	
}
