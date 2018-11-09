package de.Jcing.game;

import java.awt.Color;
import java.awt.event.KeyEvent;

import de.Jcing.Main;
import de.Jcing.engine.entity.Entity;
import de.Jcing.engine.io.KeyBoard;
import de.Jcing.engine.world.Stage;
import de.Jcing.engine.world.Tile;
import de.Jcing.image.Image;
import de.Jcing.tasks.Clock;
import de.Jcing.tasks.Scene;
import de.Jcing.tasks.Task;
import de.Jcing.util.Point;
import de.Jcing.window.Window;
import de.Jcing.window.gui.Button;
import de.Jcing.window.gui.Label;
import de.Jcing.window.gui.ProgressBar;

public class Game {
	
	private Stage mainStage;
	
	private Point camera;

	private Entity testEntity;
	
	private boolean isIntitialized;
	
	private Scene gameScene;
		
	public Game () {
		isIntitialized = false;
		gameScene = new Scene("main game");
		System.out.println("initializing..");
		mainStage = new Stage();
		camera = new Point(0,0);
		for (int i = -20; i < 20; i++) {
			for (int j = -20; j < 20; j++) {
				mainStage.addChunk(i, j);
			}
		}
		System.out.println("chunks added...");

		
		Main.getWindow().addDrawable(mainStage);
		System.out.println("started drawing..");

		Label fpsLabel = new Label("FPS: ", 5, 5);
		fpsLabel.getOnClick().add(() -> System.exit(0));
		fpsLabel.listenOnMouse();
		Main.getWindow().gui().addComponent(fpsLabel);
		new Task(() -> tick(), 60,gameScene);
		new Task(() -> fpsLabel.setText("FPS: " + Main.getWindow().getFPS()), 1, gameScene);
		System.out.println("added FPS label..");

		Button exit = new Button("X", Window.PIXEL_WIDTH-20, 0);
		exit.setTextColor(new Color(200,200,200));
		exit.setBackground(new Color(200,50,50), Button.COLOR_DEFAULT);
		exit.setBackground(new Color(220,80,80), Button.COLOR_HOVERED);
		exit.setBackground(new Color(180,10,10), Button.COLOR_PRESSED);
		exit.setPosition(Window.PIXEL_WIDTH-exit.getWidth(),0);
		exit.getOnClick().add(() -> System.exit(0));
		exit.listenOnMouse();
		Main.getWindow().gui().addComponent(exit);
		System.out.println("added exit button..");

		ProgressBar bar = new ProgressBar(65,10,100,20);
		new Task(()->bar.setPercentage(Clock.millis()/100%100),60,gameScene);
		Main.getWindow().gui().addComponent(bar);
		System.out.println("added progress bar..");

		
		testEntity = new Entity(mainStage,0,0,20,20);
		testEntity.setAnim(Entity.ON_LEFT, new Image("gfx/player/left"));
		testEntity.setAnim(Entity.ON_RIGHT, new Image("gfx/player/right"));
		testEntity.setAnim(Entity.ON_UP, new Image("gfx/player/up"));
		testEntity.setAnim(Entity.ON_DOWN, new Image("gfx/player/down"));
		mainStage.addEntity(testEntity);
		System.out.println("added entity..");

		
		testEntity.getOntick().add(() -> {
			camera.x = testEntity.getX()*Tile.TILE_PIXELS/Main.getWindow().getPixelSize() - Window.PIXEL_WIDTH/2;
			camera.y = testEntity.getY()*Tile.TILE_PIXELS/Main.getWindow().getPixelSize() - Window.PIXEL_HEIGHT/2;
		});
		
		gameScene.start();
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
		
		if(KeyBoard.isPressed(KeyEvent.VK_P)) {
			Main.getWindow().gui().setVisible(false);
			Main.getWindow().gui().unlisten();
		} else {
			Main.getWindow().gui().setVisible(true);
			Main.getWindow().gui().listenOnMouse();
		}
//		if() {
//			
//		}
		
		if(KeyBoard.isPressed(KeyEvent.VK_SHIFT)) {
			System.out.println("SHIFT");
			x *= 2;
			y *= 2;
		}
		testEntity.accelerate(x, y);
		
		mainStage.tick();
	}

	public boolean isInitialized() {
		return isIntitialized;
	}	
	
}
