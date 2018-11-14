package de.Jcing.tasks;

public class Task {
	
	protected double waitingTime;
	protected boolean running, pause;
	protected Runnable routine;
	protected Thread th;

	protected boolean finished;
	protected int TPS;
	private String name;
	public int tps;
	
	public Task(Runnable routine) {
		this(routine, -1);
	}
	
	public Task(Runnable routine, int tps) {
		this(routine, tps, Clock.getGlobalScene());
	}
	
	public Task(Runnable routine, int tps, Scene scene) {
		this.TPS = tps;
		waitingTime = 1000.0 / tps;
		this.routine = routine;
		finished = false;
		scene.addTask(this);
	}
	
	protected Runnable wrapper = () -> {
		routine.run();
		if(TPS <= 0)
			running = false;
	};
	
	public Task start() {
		if(!running) {
			running = true;
			pause = false;
			Clock.execute(this);
		}
		return this;
	}
	
	public void finish() {
		running = false;
		pause = false;
	}

	public int getTps() {
		return TPS;
	}
	
	public void setTps(int tps){
		this.TPS = tps;
		waitingTime = 1000.0 / tps;
	}


	public boolean isRunning() {
		if (!pause)
			return running;
		else
			return false;
	}
	
	public boolean isFinished() {
		return finished;
	}

	public void pause(boolean pause) {
		this.pause = pause;
	}


	public boolean isPaused() {
		return pause;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
