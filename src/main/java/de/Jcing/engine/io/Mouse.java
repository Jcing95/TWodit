package de.Jcing.engine.io;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.LinkedList;

public class Mouse {
	
	public static int x,y;
	public static int lx, ly;
	
	public static HashMap<Integer, Boolean> keys = new HashMap<>();
	
	public static final int ONPRESS = 0;
	public static final int ONRELEASE = 1;
	public static final int ONCLICK = 2;
	
	public static final int ONENTER = 10;
	public static final int ONEXIT = 11;
	
	public static final int ONMOVE = 20;
	public static final int ONDRAG = 21;
	
	private static final LinkedList<Binding> onPress = new LinkedList<>();
	private static final LinkedList<Binding> onRelease = new LinkedList<>();
	private static final LinkedList<Binding> onClick = new LinkedList<>();
	private static final LinkedList<Binding> onEnter = new LinkedList<>();
	private static final LinkedList<Binding> onExit = new LinkedList<>();
	private static final LinkedList<Binding> onMove = new LinkedList<>();
	private static final LinkedList<Binding> onDrag = new LinkedList<>();
	
	public static final int LEFT = MouseEvent.BUTTON1;
//	public static final int RIGHT = MouseEvent.BUTTON2;
	
	
	private static final HashMap<Integer, LinkedList<Binding>> bindings = new HashMap<>();
	
	static {
		bindings.put(ONPRESS, onPress);
		bindings.put(ONRELEASE, onRelease);
		bindings.put(ONCLICK, onClick);
		bindings.put(ONENTER, onEnter);
		bindings.put(ONEXIT, onExit);
		bindings.put(ONMOVE, onMove);
		bindings.put(ONDRAG, onDrag);
	}
	
	public static final MouseListener mouseListener = new MouseListener() {
		
		//TODO: check bindings for illegal modifications.
		
		@Override
		public void mouseClicked(MouseEvent e) {
			for(Binding b : onClick)
				b.onAction(e.getButton());
		}

		@Override
		public void mousePressed(MouseEvent e) {
//			System.out.println(e.getButton() + "  " + LEFT);
			keys.put(e.getButton(),true);
			for(Binding b : onPress)
				b.onAction(e.getButton());
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			keys.put(e.getButton(), false);
			for(Binding b : onRelease)
				b.onAction(e.getButton());
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			for(Binding b : onEnter)
				b.onAction(e.getButton());
		}

		@Override
		public void mouseExited(MouseEvent e) {	
			for(Binding b : onExit)
				b.onAction(e.getButton());
		}
		
	};
	
	public static final MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		
		@Override
		public void mouseDragged(MouseEvent e) {
			lx = x;
			x = e.getX();
			ly = y;
			y = e.getY();
			for(Binding b : onDrag)
				b.onAction(e.getButton());
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			lx = x;
			x = e.getX();
			ly = y;
			y = e.getY();
			for(Binding b : onMove)
				b.onAction(e.getButton());
		}
	};
	
	public static void addBinding(int KEY, Binding binding) {
		if(bindings.containsKey(KEY))
			bindings.get(KEY).add(binding);
		else
			throw new IllegalArgumentException("invalid binding key!");
	}

	public static int getX() {
		return x;
	}
	
	public static int getY() {
		return y;
	}
	
	public static int getLastX() {
		return lx;
	}
	
	public static int getLastY() {
		return ly;
	}
	
	
}
