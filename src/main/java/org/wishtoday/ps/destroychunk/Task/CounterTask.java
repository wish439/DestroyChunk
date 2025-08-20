package org.wishtoday.ps.destroychunk.Task;

public class CounterTask implements Task {
    private int counter = 0;
    private boolean cancelled = false;
    private boolean counted = false;
    private Runnable task;
    private int delay;
    private boolean runedBreak = true;

    public CounterTask(Runnable task, int delay, boolean runedBreak) {
        this.task = task;
        this.delay = delay;
        this.runedBreak = runedBreak;
    }

    public CounterTask(Runnable task, int delay) {
        this(task, delay, true);
    }

    public CounterTask() {
        this(() -> {}, 1, true);
    }

    @Override
    public void beforeWhile() {
        counted = false;
    }

    @Override
    public void tickAction() {
        counter++;

        if (counter % delay == 0) {
            task.run();
            if (runedBreak) {
                cancelled = true;
                return;
            }
        }
        counted = true;
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
}
