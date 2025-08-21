package org.wishtoday.ps.destroychunk.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.wishtoday.ps.destroychunk.Task.Tasks.ClearBlocksTask;
import org.wishtoday.ps.destroychunk.Task.TaskManager;
import org.wishtoday.ps.destroychunk.Task.Tasks.ClearChunkTask;
import org.wishtoday.ps.destroychunk.Task.Tasks.CounterTask;
import org.wishtoday.ps.destroychunk.Util.ChunkUtil;
import org.wishtoday.ps.destroychunk.Util.StructureUtil;

import java.util.List;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "damage", at = @At("RETURN"))
    private void onDamage(DamageSource source
            , float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.isOf(DamageTypes.OUT_OF_WORLD)) return;
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (!(livingEntity instanceof PlayerEntity player)) return;
        World world = player.getWorld();
        if (world.isClient()) return;
        Chunk chunk = world.getChunk(player.getBlockPos());
        ChunkPos pos = chunk.getPos();
        ClearBlocksTask task = new ClearChunkTask((ServerWorld) world, 100 * 100, chunk);
        CounterTask counterTask1 = new CounterTask.Builder()
                .setTask(counterTask -> {
                    int remaining = 5 - (counterTask.getCounter() / 20);
                    if (remaining < 0) remaining = 0;

                    if (remaining > 0) {
                        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                        serverPlayer.networkHandler.sendPacket(
                                new TitleS2CPacket(Text.of(String.valueOf(remaining)))
                        );
                        MinecraftServer server = serverPlayer.getServer();
                        List<ServerPlayerEntity> playerList = server.getPlayerManager().getPlayerList();
                        for (ServerPlayerEntity entity : playerList) {
                            entity.sendMessage(Text.of("区块:" + pos.toString() + "将在" + remaining + "秒后消失"));
                        }
                    }

                    if (remaining == 0) {
                        StructureUtil.saveChunkToStructure(chunk,(ServerWorld) world, ChunkUtil.chunkPosNormalize(pos));
                        TaskManager.getInstance().addTask(task);
                        counterTask.setCancelled(true);
                    }
                })
                .setDelay(20)
                .setStopAfterRun(false)
                .setRunOnSubmit(true)
                .build();
        TaskManager.getInstance().addTask(counterTask1);
    }
}
