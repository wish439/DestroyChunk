package org.wishtoday.ps.destroychunk;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.wishtoday.ps.destroychunk.Task.TaskManager;

public class Destroychunk implements ModInitializer {

    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            TaskManager.getInstance().tickTasks();
        });
    }
}
