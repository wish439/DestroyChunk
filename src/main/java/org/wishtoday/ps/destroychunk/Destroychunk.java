package org.wishtoday.ps.destroychunk;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.wishtoday.ps.destroychunk.Task.CounterTask;
import org.wishtoday.ps.destroychunk.Task.TaskManager;

public class Destroychunk implements ModInitializer {

    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            TaskManager.getInstance().tickTasks();
        });
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
           TaskManager.getInstance().addTask(new CounterTask(() -> System.out.println("5秒"), 100, false));
        });
    }
}
