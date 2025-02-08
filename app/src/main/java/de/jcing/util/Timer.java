package de.jcing.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.AccessLevel;
import lombok.Setter;

public class Timer {

    private static final float SECOND = 1000;
    private static final float MINUTE = SECOND * 60;
    private static final float HOUR = MINUTE * 60;

    private final ScheduledExecutorService executor;
    private final Runnable runnable;
    private final Runnable wrappedRunnable = this::wrapRunnable;

    @Setter(AccessLevel.PRIVATE)
    private int waitTime;
    @Setter(AccessLevel.PRIVATE)
    private int delay;

    private boolean once = false;

    public Timer(Runnable runnable) {
        this.runnable = runnable;
        executor = Executors.newScheduledThreadPool(1);
    }

    public Time every(float x) {
        return new Time(this, x, this::setWaitTime);
    }

    public Timer once() {
        once = true;
        return this;
    }
    
    public Time after(float x) {
        return new Time(this, x, this::setDelay);
    }

    public Times per(float x) {
        return new Times(this, x, this::setWaitTime);
    }


    private void wrapRunnable() {
        long startTime = System.currentTimeMillis();
        this.runnable.run();
        if (once) {
            executor.shutdown();
        } else {
            long passedTime = System.currentTimeMillis() - startTime;
            executor.schedule(this.wrappedRunnable, waitTime - passedTime, TimeUnit.MILLISECONDS);
        }
    }

    public Timer start() {
        executor.schedule(this.wrappedRunnable, delay, TimeUnit.MILLISECONDS);
        return this;
    }

    private interface TimeSetter {
        public void set(int x);
    }

    public class Time {

        private final Timer t;
        private float time;
        private TimeSetter s;

        private Time(Timer t, float time, TimeSetter s) {
            this.t = t;
            this.time = time;
            this.s = s;
        }

        public Timer seconds() {
            s.set((int)(time * SECOND));
            return t;
        }
        public Timer minutes() {
            s.set((int)(time * MINUTE));
            return t;
        }
        public Timer hours() {
            s.set((int)(time * HOUR));
            return t;
        }
    }
    public class Times {

        private final Timer t;
        private float times;
        private TimeSetter s;

        private Times(Timer t, float times, TimeSetter s) {
            this.t = t;
            this.times = times;
            this.s = s;
        }

        public Timer second() {
            s.set((int)(SECOND / times));
            return t;
        }
        public Timer minute() {
            s.set((int)(MINUTE / times));
            return t;
        }
        public Timer hour() {
            s.set((int)(HOUR / times));
            return t;
        }
    }


}
