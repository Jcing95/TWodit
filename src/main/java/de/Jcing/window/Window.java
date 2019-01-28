package de.jcing.window;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JFrame;

import de.jcing.Main;
import de.jcing.engine.graphics.Drawable;
import de.jcing.engine.io.KeyBoard;
import de.jcing.engine.io.Mouse;
import de.jcing.util.Point;
import de.jcing.util.Util;
import de.jcing.utillities.task.Task;
import de.jcing.window.gui.Container;
import de.jcing.world.Tile;

public class Window {
	
	public static final String TITLE = "TwoDedit";
	
	public static final int DEFAULT_WIDTH = 1280;
	public static final int DEFAULT_HEIGHT = 720;
	
	public static final int PIXEL_WIDTH = Tile.TILE_PIXELS * 24;
	public static final int PIXEL_HEIGHT = Tile.TILE_PIXELS * 16;
	
	public static final Color DEFAULT_BACKGROUND = new Color(5,20,2);
	public static final Color DEFAULT_FOREGROUND = new Color(220,220,220);
	
	private JFrame frame;
	private Canvas canvas;
	
	private LinkedList<Drawable> drawables;
	private Drawable[] nextDrawables;
	
	private Container gui;
	
	private Task task;

	private FontMetrics fontMetrics;

	private int xOffset;

	private int yOffset;
	
	public Window() {
		//initialize Swing frame and canvas
		frame = new JFrame(TITLE);
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		canvas.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		canvas.setBackground(DEFAULT_BACKGROUND);
		canvas.setForeground(DEFAULT_FOREGROUND);
		
		//add global listener for KeyBoard and Mouse classes to work
		canvas.addKeyListener(KeyBoard.keyListener);
		canvas.addMouseListener(Mouse.mouseListener);
		canvas.addMouseMotionListener(Mouse.mouseMotionListener);
		canvas.addMouseWheelListener(Mouse.mouseWheelListener);
		frame.add(canvas);
		frame.addWindowListener(windowListener);
		
		frame.pack();
		frame.setVisible(true);
		frame.requestFocus();
		canvas.requestFocus();
		
		//drawables are all things to draw into the frame later on
		drawables = new LinkedList<>();
		//nextDrawables to buffer them to prevent concurrent modifications
		nextDrawables = new Drawable[0];
		
		//main container with the size of the Window. 
		gui = new Container(0,0,PIXEL_WIDTH,PIXEL_HEIGHT);
		
		//initialize the Graphics2D object. 
		Graphics2D initGraphics = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB).createGraphics();
		//TODO: set font and init other flags.
		fontMetrics = initGraphics.getFontMetrics();
		
		//primary render task. max 144 times/second
		task = new Task(() -> render()).name("draw window").repeat(Task.perSecond(144)).start();
		new Task(() -> gui.listenOnMouse()).delay(100).start();
	}
	
	public void render() {
		//draw everything on buffer for later scaling.
		BufferedImage buffer = new BufferedImage(PIXEL_WIDTH, PIXEL_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = buffer.createGraphics();
		for(Drawable d : nextDrawables) {
			d.draw(g);
		}
		gui.draw(g);
		double imageWidth = canvas.getWidth();
		if(imageWidth / 16 * 9 > canvas.getHeight())
			imageWidth = canvas.getHeight() / 9.0 * 16;
		double imageHeight = imageWidth / 16.0 * 9;
		xOffset = (int)((canvas.getWidth()-imageWidth)/2);
		yOffset = (int)((canvas.getHeight()-imageHeight)/2);
		
		BufferStrategy bs = canvas.getBufferStrategy();
		
		if(bs == null){
			canvas.createBufferStrategy(2);
			bs = canvas.getBufferStrategy();
			return;
		}	
		
		Graphics2D g2 = (Graphics2D) bs.getDrawGraphics();
		
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g2.drawImage(buffer, xOffset, yOffset, (int)imageWidth, (int)imageHeight, null);
		g.dispose();
		g2.dispose();
		bs.show();
		
		nextDrawables = new Drawable[drawables.size()];
		drawables.toArray(nextDrawables);
	}
	
	public void finish() {
		while(!task.isFinished())
			Util.sleep(20);
		frame.dispose();
	}
	
	public void addDrawable(Drawable drawable) {
		drawables.add(drawable);
	}
	
	public void removeDrawable(Drawable drawable) {
		drawables.remove(drawable);
	}
	
	public Container gui() {
		return gui;
	}

	
	WindowListener windowListener = new WindowListener() {

		@Override
		public void windowClosing(WindowEvent arg0) {
			Main.finish();
		}
		
		@Override
		public void windowActivated(WindowEvent arg0) {}

		@Override
		public void windowClosed(WindowEvent arg0) {}

		@Override
		public void windowDeactivated(WindowEvent arg0) {}

		@Override
		public void windowDeiconified(WindowEvent arg0) {}

		@Override
		public void windowIconified(WindowEvent arg0) {}

		@Override
		public void windowOpened(WindowEvent arg0) {}
		
	};

//	public double getPixelSize() {
//		return (1.0*canvas.getWidth()-2*xOffset)/PIXEL_WIDTH;
//	}
	
	public double getPixelWidth() {
		return (1.0*canvas.getWidth()-2*xOffset)/PIXEL_WIDTH;
	}
	
	public double getPixelHeight() {
		return (1.0*canvas.getHeight()-2*yOffset)/PIXEL_HEIGHT;
	}

	public FontMetrics getFontMetrics() {
		return fontMetrics;
	}
	
	public int getFPS() {
		return task.getTps();
	}

	public Point getMouseOnCanvas() {
		return new Point((Mouse.getX()-xOffset)/getPixelWidth(), (Mouse.getY()-yOffset)/getPixelHeight());
	}
	
}
