package org.wishtoday.ps.destroychunk.Task;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskManager {
    private static TaskManager instance = new TaskManager();
    private final List<Task> tasks = Lists.newArrayList();
    private final List<Task> pendingTasks = new ArrayList<>();
    private TaskManager() {}

    public static TaskManager getInstance() {
        return instance;
    }
    public synchronized void addTask(Task task) {
        pendingTasks.add(task);
    }
    public synchronized void addTask(int priority, Task task) {
        pendingTasks.add(priority ,task);
    }
    public void removeTask(Task task) {
        tasks.remove(task);
    }
    public void tickTasks() {
        synchronized (this) {
            if (!pendingTasks.isEmpty()) {
                tasks.addAll(pendingTasks);
                pendingTasks.clear();
            }
        }
        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (task.isFinished()) {
                task.onRemove();
                iterator.remove();
                continue;
            }
            task.tick();
        }
    }
}
