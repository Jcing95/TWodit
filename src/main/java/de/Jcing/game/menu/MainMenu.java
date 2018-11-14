package de.Jcing.game.menu;

import java.awt.Color;

import de.Jcing.Main;
import de.Jcing.window.Window;
import de.Jcing.window.gui.Button;
import de.Jcing.window.gui.Container;

public class MainMenu extends Container {
	
	Button play, exit;
	
	
	public MainMenu() {
		super(0,0,Window.PIXEL_WIDTH,Window.PIXEL_HEIGHT);
		play = new Button("   P L A Y   ",Window.PIXEL_WIDTH/2,Window.PIXEL_HEIGHT/3);
		exit = new Button("   E X I T   ",Window.PIXEL_WIDTH/2,Window.PIXEL_HEIGHT/3*2);
		play.centerHorizontal(true);
		play.centerVertical(true);
		exit.centerHorizontal(true);
		exit.centerVertical(true);
		
		play.getOnClick().add(() -> { 
			unlisten();
			Main.getWindow().removeDrawable(this);
			new Thread(() -> Main.initGame()).start();
		});
		
		exit.getOnClick().add(() -> Main.finish());
		play.setColors(new Color(10,150,10), new Color(40,185,40), new Color(5,100,5), new Color(230,230,230));
		exit.setColors(new Color(150,10,10), new Color(185,40,40), new Color(100,5,5), new Color(0,0,0));
		
		addComponent(play);
		addComponent(exit);
		listenOnMouse();
		
		Main.getWindow().addDrawable(this);
	}
	
	
	
	
}
