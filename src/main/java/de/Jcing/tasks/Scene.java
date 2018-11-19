package de.Jcing.tasks;

import java.util.HashSet;

public class Scene {
	
	private String name;
	
	private HashSet<Task> tasks;
	
	public Scene(String name) {
		this.name = name;
		tasks = new HashSet<>();
		Clock.addScene(this);
	}
	
	public Scene start() {
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
		for(Task t: tasks) {
			t.pause(pause);
		}
	}
	
	public Scene addTask(Task t) {
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
	
}
