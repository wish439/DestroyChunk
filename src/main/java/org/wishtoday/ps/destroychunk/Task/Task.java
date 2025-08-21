package org.wishtoday.ps.destroychunk.Task;

public interface Task {
    default void beforeWhile() {}
    default void tick() {
        beforeWhile();
        while (!shouldStop()) {
            tickAction();
        }
    };
    void tickAction();
    boolean isFinished();
    boolean shouldStop();
    default void onRemove(){};
}
