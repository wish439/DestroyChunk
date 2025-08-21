package org.wishtoday.ps.destroychunk.Task.Tasks;

import org.jetbrains.annotations.Nullable;
import org.wishtoday.ps.destroychunk.Task.Task;

import java.util.function.Consumer;

public class CounterTask implements Task {
    private int counter = 0;
    private boolean cancelled = false;
    private boolean counted = false;
    private Consumer<CounterTask> task;
    private int delay;
    private boolean stopAfterRun;
    private int countTick;
    private boolean runOnSubmit = false;

    protected CounterTask(@Nullable Consumer<CounterTask> task, int delay, boolean runedBreak, int countTick, boolean runOnSubmit) {
        this.task = task;
        this.delay = Math.max(1, delay);
        this.stopAfterRun = runedBreak;
        this.countTick = countTick;
        this.runOnSubmit = runOnSubmit;

        if (task == null) return;
        if (runOnSubmit) {
            task.accept(this);
            if (stopAfterRun) cancelled = true;
        }
    }



    @Override
    public void beforeWhile() {
        counted = false;
    }

    @Override
    public void tickAction() {
        counter++;

        if (counter % delay == 0) {
            onTick();
            if (stopAfterRun) {
                cancelled = true;
                return;
            }
        }

        if (counter >= countTick) {
            cancelled = true;
            return;
        }
        counted = true;
    }
    protected void onTick() {
        task.accept(this);
    }

    @Override
    public boolean isFinished() {
        return cancelled;
    }

    @Override
    public boolean shouldStop() {
        return cancelled || counted;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public int getCounter() {
        return counter;
    }

    public static class Builder {
        private Consumer<CounterTask> task = task -> {
        };
        protected int delay = 1;
        protected boolean stopAfterRun = true;
        protected int countTick = Integer.MAX_VALUE;
        protected boolean runOnSubmit = false;

        public Builder() {

        }

        public Builder setTaskAndDelay(Consumer<CounterTask> task, int delay) {
            this.task = task;
            this.delay = delay;
            return this;
        }

        public Builder setTask(Consumer<CounterTask> task) {
            this.task = task;
            return this;
        }

        public Builder setDelay(int delay) {
            this.delay = delay;
            return this;
        }

        public Builder setStopAfterRun(boolean b) {
            this.stopAfterRun = b;
            return this;
        }

        public Builder setCountTick(int i) {
            this.countTick = i;
            return this;
        }

        public Builder setRunOnSubmit(boolean runOnSubmit) {
            this.runOnSubmit = runOnSubmit;
            return this;
        }

        public CounterTask build() {
            return new CounterTask(task, delay, stopAfterRun, countTick, runOnSubmit);
        }
    }
}
