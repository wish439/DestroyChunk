package org.wishtoday.ps.destroychunk.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.chunk.Chunk;
import org.wishtoday.ps.destroychunk.Util.StructureUtil;

import static net.minecraft.server.command.CommandManager.literal;

public class RestoreChunkCommand {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("restorechunk")
                        .executes(
                                RestoreChunkCommand::execute
                        )
        );
    }
    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            source.sendFeedback(() -> Text.of("请让玩家来恢复"),false);
            return -1;
        }
        Chunk chunk = player.getWorld().getChunk(player.getChunkPos().getStartPos());
        StructureUtil.loadChunkFromChunk(player.getServerWorld(), chunk);
        source.sendFeedback(() -> Text.of("恢复成功"),true);
        return 1;
    }
}
