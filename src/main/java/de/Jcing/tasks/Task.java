package de.Jcing.tasks;

public class Task {
	
	protected double waitingTime;
	protected boolean running, pause;
	protected Runnable routine;
	protected Thread th;

	protected int TPS;
	
	public int tps;
	
	public Task(Runnable routine) {
		this.routine = routine;
		TPS = -1;
	}
	
	public Task(Runnable routine, int tps) {
		this.TPS = tps;
		waitingTime = 1000.0 / tps;
		this.routine = routine;
		Clock.addTask(this);
	}
	
	protected Runnable wrapper = () -> {
		routine.run();
		if(TPS <= 0)
			running = false;
	};
	
	public void start() {
		running = true;
		pause = false;
		Clock.execute(this);
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


	public void pause(boolean pause) {
		this.pause = pause;
	}


	public boolean isPaused() {
		return pause;
	}

	public boolean isFinished() {
		return !running;
	}

}
