package de.jcing.window.gui.animator;

import de.jcing.engine.gui.Component;
import de.jcing.utillities.task.Task;
import de.jcing.utillities.task.Topic;
import de.jcing.window.gui.utillities.Group;

@Deprecated
public abstract class Animator {
	
	public static final Topic ANIMATOR_TOPIC = new Topic("Animator");
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
	}).name("Animating").repeat(Task.perSecond(TPS)).inTopic(ANIMATOR_TOPIC);
	
	
	public Animator start(long duration) {
		currentTick = 0;
		totalTicks = duration / TPS;
		animate.start();
		return this;
	}
	
	public void stop() {
		animate.stop();
	}
	
	
}
