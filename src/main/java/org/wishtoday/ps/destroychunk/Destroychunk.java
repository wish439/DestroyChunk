package org.wishtoday.ps.destroychunk;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.wishtoday.ps.destroychunk.Task.TaskManager;
import org.wishtoday.ps.destroychunk.command.RestoreChunkCommand;

public class Destroychunk implements ModInitializer {

    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            TaskManager.getInstance().tickTasks();
        });
        CommandRegistrationCallback.EVENT.register((dispatcher
                , dedicated
                ,environment) -> {
            RestoreChunkCommand.registerCommands(dispatcher);
        });
    }
}
