package de.Jcing.tasks;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;

import de.Jcing.util.Util;

public class Clock {

//	private static LinkedList<Task> tasks = new LinkedList<>();
	
	private static HashMap<Integer,Scene> scenes = new HashMap<>();
	private static Scene mainScene = new Scene("global");
	
	private static long startMillis = System.currentTimeMillis();
	public static final int NUM_CORES = Runtime.getRuntime().availableProcessors()/2;
	public static final int RESERVED_CORES = 1;
	
	
	static {
		scenes.put(mainScene.hashCode(), mainScene);	
	}
	
	private Clock() {
		
	}

	protected static void addTask(Task task) {
		mainScene.addTask(task);
	}

	public static void start() {
		mainScene.start();
	}

	public static void stop() {
		mainScene.finish();
	}
	
	protected static void addScene(Scene s) {
		scenes.put(s.hashCode(), s);
	}
	
	public static void startAll() {
		for(Scene s : scenes.values()) {
			s.start();
		}
	}
	
	public static void stopAll() {
		for(Scene s : scenes.values()) {
			s.finish();
		}
	}
	
	public static long millis() {
		return (System.currentTimeMillis()-startMillis);
	}
	
	
	public static void execute(Task task) {
		new Thread( () -> {
		long lastTick;
		long lastSec = System.currentTimeMillis();
		double difft = 0;
		int ticks = 0;
		while (task.running) {
			while (task.pause) {
				ticks = 0;
				task.tps = 0;
				Util.sleep((long) task.waitingTime);
			}
			if (task.running == false) {
				return;
			}
			ticks++;			
			lastTick = System.currentTimeMillis();
			task.wrapper.run();
			if (System.currentTimeMillis() - lastSec >= 1000) {
				task.tps = ticks;
				ticks = 0;
				lastSec = System.currentTimeMillis();
			}
			
			difft -= (int)difft;
			difft += task.waitingTime-(System.currentTimeMillis() - lastTick);
			
			if (difft > 0)
				Util.sleep((long)(difft));
		}
		task.finished = true;
		}).start();
	}

	@SuppressWarnings("unused")
	@Deprecated
	public static void schedule(boolean blocking, Runnable ...runnables) {
		//TODO: feed tasks
		int numThreads = NUM_CORES - RESERVED_CORES;
		if(numThreads < 1) {
			for(Runnable r : runnables)
				r.run();
			return;
		}
		
		Thread th[] = new Thread[numThreads];
		LinkedBlockingDeque<Runnable> toExecute = new LinkedBlockingDeque<>();
		boolean[] finished = new boolean[numThreads];
		
		try {
//			System.out.println("numThreads: " + numThreads);
		for (int i = 0; i < runnables.length; i++) {
			toExecute.put(runnables[i]);			
		}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < numThreads;i++){
//			final int index = i;
			th[i] = new Thread(() -> {
				while(!toExecute.isEmpty()) {
					try {
					Runnable r = toExecute.pop();
					if(r == null)
						return;
					else
						r.run();
					} catch (NoSuchElementException e) {
						return;
					}
//					System.out.println("SIZE: " + toExecute.size());
				}
//				System.out.println(index + " = " + true);
//				finished[index] = true;
			});
			th[i].setName("Painter " + i);
			th[i].start();
		}
//		System.out.println("EXEC");
		if(blocking) {
			boolean fin = false;
			while(!fin) {
				fin = true;
				for (int i = 0; i < th.length; i++) {
					if(th[i].isAlive()) {
						fin = false;
						break;
					}
				}
//				System.out.println("BLOCK");
//				Util.sleep(1);
			}
		}
//		System.out.println("FIN");

	}

	public static Scene getGlobalScene() {
		return mainScene;
	}

}
