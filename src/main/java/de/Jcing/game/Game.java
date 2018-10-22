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
		
	public Game () {
		
		mainStage = new Stage();
		camera = new Point(0,0);
		System.out.println("initializing..");
		for (int i = -20; i < 20; i++) {
			for (int j = -20; j < 20; j++) {
				mainStage.addChunk(i, j);
			}
		}
		
		Main.getWindow().addDrawable(mainStage);
		
		Label fpsLabel = new Label("FPS: ", 5, 5);
		fpsLabel.getOnClick().add(() -> System.exit(0));
		fpsLabel.listenOnMouse();
		Main.getWindow().gui().addComponent(fpsLabel);
		new Task(() -> tick(), 60);
		new Task(() -> fpsLabel.setText("FPS: " + Main.getWindow().getFPS()), 1);
		
		Button exit = new Button("X", Window.PIXEL_WIDTH-20, 0);
		exit.setTextColor(new Color(200,200,200));
		exit.setBackground(new Color(200,50,50), Button.DEFAULT);
		exit.setBackground(new Color(220,80,80), Button.HOVERED);
		exit.setBackground(new Color(180,10,10), Button.PRESSED);
		exit.setPosition(Window.PIXEL_WIDTH-exit.getWidth(),0);
		exit.getOnClick().add(() -> System.exit(0));
		exit.listenOnMouse();
		Main.getWindow().gui().addComponent(exit);
		
		ProgressBar bar = new ProgressBar(65,10,100,20);
		
		new Task(()->bar.setPercentage(Clock.millis()/100%100),60);
		
		Main.getWindow().gui().addComponent(bar);
		
		
		testEntity = new Entity(mainStage,0,0,20,20);
		testEntity.setAnim(Entity.ON_LEFT, new Image("gfx/player/left"));
		testEntity.setAnim(Entity.ON_RIGHT, new Image("gfx/player/right"));
		testEntity.setAnim(Entity.ON_UP, new Image("gfx/player/up"));
		testEntity.setAnim(Entity.ON_DOWN, new Image("gfx/player/down"));

		mainStage.addEntity(testEntity);
		
		testEntity.getOntick().add(() -> {
			camera.x = testEntity.getX()*Tile.TILE_PIXELS/Main.getWindow().getPixelSize() - Window.PIXEL_WIDTH/2;
			camera.y = testEntity.getY()*Tile.TILE_PIXELS/Main.getWindow().getPixelSize() - Window.PIXEL_HEIGHT/2;
//			System.out.println("KAMERA: " + camera);
		});
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
	
}
