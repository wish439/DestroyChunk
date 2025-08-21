package org.wishtoday.ps.destroychunk.Task.Tasks;

import java.util.function.Consumer;

public class DataCounterTask<T> extends CounterTask {
    private Consumer<DataCounterTask<T>> task;
    private T data;

    protected DataCounterTask(Consumer<DataCounterTask<T>> task,
                              int delay,
                              boolean stopAfterRun,
                              int countTick,
                              boolean runOnSubmit,
                              T data) {
        super(null, delay, stopAfterRun, countTick, runOnSubmit);
        this.task = task;
        this.data = data;
        if (runOnSubmit) {
            task.accept(this);
        }
    }


    public T getData() {
        return data;
    }

    @Override
    protected void onTick() {
        task.accept(this);
    }

    public static class Builder<T> {
        private Consumer<DataCounterTask<T>> task = t -> {
        };
        private int delay = 1;
        private boolean stopAfterRun = true;
        private int countTick = Integer.MAX_VALUE;
        private boolean runOnSubmit = false;
        private T data;

        public Builder<T> setTask(Consumer<DataCounterTask<T>> task) {
            this.task = task;
            return this;
        }

        public Builder<T> setDelay(int delay) {
            this.delay = delay;
            return this;
        }

        public Builder<T> setStopAfterRun(boolean stopAfterRun) {
            this.stopAfterRun = stopAfterRun;
            return this;
        }

        public Builder<T> setCountTick(int countTick) {
            this.countTick = countTick;
            return this;
        }

        public Builder<T> setRunOnSubmit(boolean runOnSubmit) {
            this.runOnSubmit = runOnSubmit;
            return this;
        }

        public Builder<T> setData(T data) {
            this.data = data;
            return this;
        }

        public DataCounterTask<T> build() {
            return new DataCounterTask<>(task, delay, stopAfterRun, countTick, runOnSubmit, data);
        }
    }
}
