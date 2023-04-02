package de.jcing.util.task;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class Context {

	private final CopyOnWriteArrayList<Runnable> toRun;
	private final CopyOnWriteArrayList<Runnable> toLoop;
	private final CopyOnWriteArrayList<Runnable> preLoop;
	private final CopyOnWriteArrayList<Runnable> postLoop;

	protected Context() {
		toRun = new CopyOnWriteArrayList<>();
		toLoop = new CopyOnWriteArrayList<>();
		preLoop = new CopyOnWriteArrayList<>();
		postLoop = new CopyOnWriteArrayList<>();
	}

	public void run(Runnable... run) {
		toRun.addAll(Arrays.asList(run));

	}

	public void loop(Runnable... run) {
		toLoop.addAll(Arrays.asList(run));

	}

	public void preLoop(Runnable... run) {
		preLoop.addAll(Arrays.asList(run));

	}

	public void postLoop(Runnable... run) {
		postLoop.addAll(Arrays.asList(run));

	}

	protected void exec() {
		while (!toRun.isEmpty()) {
			toRun.remove(0).run();
		}

		for (Runnable r : preLoop) {
			r.run();
		}
		for (Runnable r : toLoop) {
			r.run();
		}

		for (Runnable r : postLoop) {
			r.run();
		}

	}

	public int loopSize() {
		return toLoop.size();
	}

	public Iterator<Runnable> getLoopIterator() {
		return toLoop.iterator();
	}

}
