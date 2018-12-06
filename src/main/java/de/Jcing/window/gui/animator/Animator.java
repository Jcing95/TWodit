package de.Jcing.window.gui.animator;

import de.Jcing.tasks.Scene;
import de.Jcing.tasks.Task;
import de.Jcing.window.gui.Component;
import de.Jcing.window.gui.utillities.Group;

public abstract class Animator {
	
	public static final Scene ANIMATOR_SCENE = new Scene("Animator");
	public static final double TPS = 30;
	
	protected Group toAnimate;	
	protected int currentTick;
	protected double totalTicks;
	
	public Animator(Group group) {
		toAnimate = group;
	}
		
	protected abstract void animate(Component c, int tick, double of);
	
	protected Task animate = new Task(() -> {
		if(totalTicks > currentTick) {
			toAnimate.doAll((c) -> {
				animate(c,currentTick,totalTicks);
			});
			currentTick++;
		}else {
			stop();
		}
	}, "Animating", TPS, ANIMATOR_SCENE);
	
	
	public Animator start(long duration) {
		currentTick = 0;
		totalTicks = duration / TPS;
		animate.start();
		return this;
	}
	
	public void stop() {
		animate.finish();
	}
	
	
}
