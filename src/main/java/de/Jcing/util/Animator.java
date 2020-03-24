package de.jcing.util;

import de.jcing.utillities.task.Task;

public class Animator {

	private float TPS;

	private int deltaTime;
	private long currentTime;
	private long goalTime;

	Task t;

	public Animator(Runnable r) {
		t = new Task().name("interpolator");
	}

	public interface Tick {

		public void tick(long currentTime, long goalTime, int deltaTime);

	}

}
