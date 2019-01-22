package de.jcing.game;

import java.awt.Color;
import java.awt.event.KeyEvent;

import de.jcing.Main;
import de.jcing.engine.entity.Entity;
import de.jcing.engine.io.KeyBoard;
import de.jcing.engine.io.Mouse;
import de.jcing.game.menu.PauseMenu;
import de.jcing.image.Image;
import de.jcing.util.PointMorph;
import de.jcing.util.Strings;
import de.jcing.utillities.log.Log;
import de.jcing.utillities.task.Task;
import de.jcing.utillities.task.Topic;
import de.jcing.window.Window;
import de.jcing.window.gui.Button;
import de.jcing.window.gui.ImageView;
import de.jcing.window.gui.Label;
import de.jcing.window.gui.ProgressBar;
import de.jcing.window.gui.ScrollPane;
import de.jcing.window.gui.TextPane;
import de.jcing.window.gui.animator.Fader;
import de.jcing.window.gui.utillities.Group;
import de.jcing.world.Chunk;
import de.jcing.world.Stage;
import de.jcing.world.Tile;

public class Game {
	
	private static final Log LOG = new Log(Game.class);
	
	public static final int RADIUS = 7;
	
	private Stage mainStage;
	
	private Entity player;
	
	private boolean isIntitialized;
	
	private Topic gameTopic;
	
	private boolean pauseToggled;
	private boolean faded;
	
	private Fader guiFader;
	
	private PointMorph playerPosMorph;
	private PointMorph cameraMorph;
	
	Task tick;
		
	public Game () {
		isIntitialized = false;
		gameTopic = new Topic("main game");
		LOG.info("initializing..");
		mainStage = new Stage();
//		for (int i = -RADIUS; i < RADIUS; i++) {
//			for (int j = -RADIUS; j < RADIUS; j++) {
//				mainStage.addChunk(i, j);
//			}
//		}
//		System.out.println("chunks added...");

		
		Main.getWindow().addDrawable(mainStage);
		LOG.info("started drawing..");

		Label fpsLabel = new Label("FPS: ", 5, 5);
		Label posLabel = new Label("",5,15);
		Main.getWindow().gui().addComponent(fpsLabel);
		Main.getWindow().gui().addComponent(posLabel);
		tick = new Task(() -> tick()).name("GameTick").repeat(Task.perSecond(60)).inTopic(gameTopic);
		new Task(() -> fpsLabel.setText("FPS: " + Main.getWindow().getFPS() + " - TPS: " + tick.getTps())).name("FPS updater").repeat(Task.perSecond(1)).inTopic(gameTopic);
		new Task(() -> posLabel.setText("X: " + player.getX() + ", Y: " + player.getY())).name("Player Pos updater").repeat(Task.perSecond(10)).inTopic(gameTopic);

		LOG.info("added FPS label..");

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
		
		LOG.info("added exit button..");

		ProgressBar health = new ProgressBar(Window.PIXEL_WIDTH-55,Window.PIXEL_HEIGHT-35,50,5);
		ProgressBar stamina = new ProgressBar(Window.PIXEL_WIDTH-55,Window.PIXEL_HEIGHT-25,50,5);
		ProgressBar hunger = new ProgressBar(Window.PIXEL_WIDTH-55,Window.PIXEL_HEIGHT-15,50,5);
		health.setFront(Color.red);
		stamina.setFront(Color.green.darker());
		hunger.setFront(Color.orange.darker().darker());
		new Task(()-> {
			health.setPercentage(Task.millis()/20%100);
			stamina.setPercentage(Task.millis()/10%100);
			hunger.setPercentage(Task.millis()/30%100);
			}).name("ProgressBar updater").repeat(Task.perSecond(60)).inTopic(gameTopic);
		Main.getWindow().gui().addComponent(health);
		Main.getWindow().gui().addComponent(stamina);
		Main.getWindow().gui().addComponent(hunger);
		LOG.info("added progress bar..");

		
		player = new Entity(mainStage,0,0,20,20);
		player.setAnim(Entity.ON_LEFT, new Image("gfx/player/left"));
		player.setAnim(Entity.ON_RIGHT, new Image("gfx/player/right"));
		player.setAnim(Entity.ON_UP, new Image("gfx/player/up"));
		player.setAnim(Entity.ON_DOWN, new Image("gfx/player/down"));
		mainStage.addEntity(player);
		playerPosMorph = new PointMorph(player.getPosition()) {

			@Override
			public double morphX(double x) {
				return x/Chunk.TILE_COUNT;
			}

			@Override
			public double morphY(double y) {
				return y/Chunk.TILE_COUNT;
			}
			
		};
		mainStage.setLoadingAnchor(playerPosMorph);
		LOG.info("added entity..");
		cameraMorph = new PointMorph(player.getPosition()) {

			@Override
			public double morphX(double x) {
				return x*Tile.TILE_PIXELS/Main.getWindow().getPixelSize() - Window.PIXEL_WIDTH/2;
			}

			@Override
			public double morphY(double y) {
				return y*Tile.TILE_PIXELS/Main.getWindow().getPixelSize() - Window.PIXEL_HEIGHT/2;
			}
			
		};
		mainStage.setCamera(cameraMorph);		
		
		KeyBoard.listenOnToggle(KeyEvent.VK_P);
		gameTopic.start();
		Mouse.addBinding(Mouse.ONCLICK, (i) -> mainStage.handleClick());
		isIntitialized = true;
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
		gameTopic.pause(pause);
	}
	
	public Entity getPlayer() {
		return player;
	}
	
}
