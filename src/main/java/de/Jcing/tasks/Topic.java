package de.Jcing.tasks;

import java.util.HashSet;

public class Topic {
	
	private String name;
	
	private boolean pause;
	
	private HashSet<Task> tasks;
	
	public Topic(String name) {
		this.name = name;
		tasks = new HashSet<>();
		Clock.addScene(this);
	}
	
	public Topic start() {
		for (Task t: tasks) {
			t.start();
		}
		return this;
	}
	
	public void finish() {
		for(Task t: tasks) {
			t.finish();
		}
	}
	
	public void pause(boolean pause) {
		this.pause = pause;
		for(Task t: tasks) {
			t.pause(pause);
		}
	}
	
	public Topic addTask(Task t) {
		tasks.add(t);
		return this;
	}
	
	public void removeTask(Task t) {
		tasks.remove(t);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public boolean isPaused() {
		return pause;
	}
	
}
