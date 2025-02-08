package de.jcing.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import lombok.Getter;
import lombok.Setter;

public class TimeTracker {
    
    private long lastTick;
    private int ticks;
    
    @Setter
    private float tps;

    @Getter
    private int currentTps;

    public TimeTracker(float tps) {
        lastTick = System.currentTimeMillis();
        this.tps = tps;
    }

    public void tick() {
        ticks++;
        long current = System.currentTimeMillis();
        if (current - lastTick > 1000) {
            currentTps = ticks;
            ticks = 0;
            lastTick = current;
        }
    }

    public boolean shouldTick() {
        return System.currentTimeMillis() - lastTick < 1000 / tps;
    }

    public long remainingWaitTime() {
        long waittime = (long)(1000 / tps - (System.currentTimeMillis() - lastTick));
        return waittime;
    }

    public void sleep() {
        CompletableFuture<Void> f = new CompletableFuture<>();
        new Thread(() -> {
            try {
                Thread.sleep(Long.max(0,remainingWaitTime()));
                f.complete(null);
            } catch (InterruptedException e) {
                f.cancel(true);
            }
        }).start();
        try {
            f.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
