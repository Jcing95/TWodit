package de.Jcing.game;

import java.awt.Color;
import java.awt.event.KeyEvent;

import de.Jcing.Main;
import de.Jcing.engine.entity.Entity;
import de.Jcing.engine.io.KeyBoard;
import de.Jcing.engine.io.Mouse;
import de.Jcing.engine.world.Stage;
import de.Jcing.engine.world.Tile;
import de.Jcing.game.menu.PauseMenu;
import de.Jcing.image.Image;
import de.Jcing.tasks.Clock;
import de.Jcing.tasks.Scene;
import de.Jcing.tasks.Task;
import de.Jcing.util.Point;
import de.Jcing.util.Strings;
import de.Jcing.window.Window;
import de.Jcing.window.gui.Button;
import de.Jcing.window.gui.ImageView;
import de.Jcing.window.gui.Label;
import de.Jcing.window.gui.ProgressBar;
import de.Jcing.window.gui.ScrollPane;
import de.Jcing.window.gui.TextPane;
import de.Jcing.window.gui.animator.Fader;
import de.Jcing.window.gui.utillities.Group;

public class Game {
	
	
	public static final int RADIUS = 7;
	
	private Stage mainStage;
	
	private Point camera;

	private Entity player;
	
	private boolean isIntitialized;
	
	private Scene gameScene;
	
	private boolean pauseToggled;
	private boolean faded;
	
	private Fader guiFader;

	
		
	public Game () {
		isIntitialized = false;
		gameScene = new Scene("main game");
		System.out.println("initializing..");
		mainStage = new Stage();
		camera = new Point(0,0);
//		for (int i = -RADIUS; i < RADIUS; i++) {
//			for (int j = -RADIUS; j < RADIUS; j++) {
//				mainStage.addChunk(i, j);
//			}
//		}
//		System.out.println("chunks added...");

		
		Main.getWindow().addDrawable(mainStage);
		System.out.println("started drawing..");

		Label fpsLabel = new Label("FPS: ", 5, 5);
		fpsLabel.getOnClick().add(() -> System.exit(0));
		fpsLabel.listenOnMouse();
		Main.getWindow().gui().addComponent(fpsLabel);
		new Task(() -> tick(), "GameTick",60,gameScene);
		new Task(() -> fpsLabel.setText("FPS: " + Main.getWindow().getFPS()),"FPS updater",1, gameScene);
		System.out.println("added FPS label..");

		Button exit = new Button("X", Window.PIXEL_WIDTH-20, 0);
		exit.setTextColor(new Color(200,200,200));
		exit.setBackground(new Color(200,50,50), Button.COLOR_DEFAULT);
		exit.setBackground(new Color(220,80,80), Button.COLOR_HOVERED);
		exit.setBackground(new Color(180,10,10), Button.COLOR_PRESSED);
		exit.setPosition(Window.PIXEL_WIDTH-exit.getWidth(),0);
		exit.getOnClick().add(() -> Main.finish());
		exit.listenOnMouse();
		Main.getWindow().gui().addComponent(exit);
		
		ScrollPane pane = new ScrollPane(5,Window.PIXEL_HEIGHT-100,Window.PIXEL_WIDTH-70,95).setBackground(new Color(47, 71, 109));
		TextPane test = new TextPane(Strings.SHORT_STORY , 5, 10, pane.getWidth()-20, pane.getHeight());
		test.setColor(new Color(206, 215, 229));
		pane.addComponent(test);
		pane.listenOnMouse();
		test.listenOnMouse();
		ImageView portrait = new ImageView(new Image("gfx/player/down").get(0),Window.PIXEL_WIDTH-55,Window.PIXEL_HEIGHT-100,60,60);
		
//		Fader fader = new Fader(new Group(pane), Fader.ALPHA, 0.01f, 1f);
//		pane.getOnClick().add(() -> fader.reverse().start(300));
		Main.getWindow().gui().addComponent(pane);
		Main.getWindow().gui().addComponent(portrait);
		guiFader = new Fader(new Group(Main.getWindow().gui()), Fader.ALPHA, 0, 1);
		
		System.out.println("added exit button..");

		ProgressBar health = new ProgressBar(Window.PIXEL_WIDTH-55,Window.PIXEL_HEIGHT-35,50,5);
		ProgressBar stamina = new ProgressBar(Window.PIXEL_WIDTH-55,Window.PIXEL_HEIGHT-25,50,5);
		ProgressBar hunger = new ProgressBar(Window.PIXEL_WIDTH-55,Window.PIXEL_HEIGHT-15,50,5);
		health.setFront(Color.red);
		stamina.setFront(Color.green.darker());
		hunger.setFront(Color.orange.darker().darker());
		new Task(()-> {
			health.setPercentage(Clock.millis()/20%100);
			stamina.setPercentage(Clock.millis()/10%100);
			hunger.setPercentage(Clock.millis()/30%100);
			},"ProgressBar updater",60,gameScene);
		Main.getWindow().gui().addComponent(health);
		Main.getWindow().gui().addComponent(stamina);
		Main.getWindow().gui().addComponent(hunger);
		System.out.println("added progress bar..");

		
		player = new Entity(mainStage,0,0,20,20);
		player.setAnim(Entity.ON_LEFT, new Image("gfx/player/left"));
		player.setAnim(Entity.ON_RIGHT, new Image("gfx/player/right"));
		player.setAnim(Entity.ON_UP, new Image("gfx/player/up"));
		player.setAnim(Entity.ON_DOWN, new Image("gfx/player/down"));
		mainStage.addEntity(player);
		System.out.println("added entity..");

		
		player.getOntick().add(() -> {
			camera.x = player.getX()*Tile.TILE_PIXELS/Main.getWindow().getPixelSize() - Window.PIXEL_WIDTH/2;
			camera.y = player.getY()*Tile.TILE_PIXELS/Main.getWindow().getPixelSize() - Window.PIXEL_HEIGHT/2;
		});
		KeyBoard.listenOnToggle(KeyEvent.VK_P);
		gameScene.start();
		Mouse.addBinding(Mouse.ONCLICK, (i) -> mainStage.handleClick());
		isIntitialized = true;
	}
	
	public Point getCamera() {
		return camera;
	}
	
	public void tick() {
		float x = 0;
		float y = 0;
		if(KeyBoard.isPressed(KeyEvent.VK_W) || KeyBoard.isPressed(KeyEvent.VK_UP))
			y = -0.01f;
		
		if(KeyBoard.isPressed(KeyEvent.VK_A) || KeyBoard.isPressed(KeyEvent.VK_LEFT))
			x = -0.01f;
		
		if(KeyBoard.isPressed(KeyEvent.VK_S) || KeyBoard.isPressed(KeyEvent.VK_DOWN))
			y = 0.01f;
		
		if(KeyBoard.isPressed(KeyEvent.VK_D) || KeyBoard.isPressed(KeyEvent.VK_RIGHT))
			x = 0.01f;
//		
		pauseToggled = KeyBoard.isToggled(KeyEvent.VK_P);
		
		if(pauseToggled) {
			if(!faded) {
				guiFader.reverse().start(100);
				faded = true;
			}
			Main.getWindow().gui().unlisten();
			pause(true);
			new PauseMenu();
		} else {
			if(faded) {
				guiFader.reverse().start(300);
				faded = false;
			}
			Main.getWindow().gui().listenOnMouse();
		}
		
		if(KeyBoard.isPressed(KeyEvent.VK_SHIFT)) {
			x *= 2;
			y *= 2;
		}
		player.accelerate(x, y);
		
		mainStage.tick();
	}

	public boolean isInitialized() {
		return isIntitialized;
	}
	
	public void pause(boolean pause) {
		gameScene.pause(pause);
	}
	
	public Entity getPlayer() {
		return player;
	}
	
}
