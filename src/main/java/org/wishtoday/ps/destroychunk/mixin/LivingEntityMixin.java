package org.wishtoday.ps.destroychunk.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.wishtoday.ps.destroychunk.Helper.ChunkHelper;
import org.wishtoday.ps.destroychunk.Task.Tasks.ClearBlocksTask;
import org.wishtoday.ps.destroychunk.Task.TaskManager;
import org.wishtoday.ps.destroychunk.Task.Tasks.ClearChunkTask;
import org.wishtoday.ps.destroychunk.Task.Tasks.CounterTask;
import org.wishtoday.ps.destroychunk.Util.ChunkUtil;
import org.wishtoday.ps.destroychunk.Util.StructureUtil;

import java.util.List;
import java.util.stream.Collectors;

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
        if (ChunkHelper.getInstance().hasChunk(chunk)) return;
        ChunkPos pos = chunk.getPos();
        ClearBlocksTask task = new ClearChunkTask((ServerWorld) world, 100 * 100, chunk);
        CounterTask counterTask1 = new CounterTask.Builder()
                .setTask(counterTask -> {
                    int remaining = 5 - (counterTask.getCounter() / 20);
                    if (remaining < 0) remaining = 0;

                    if (remaining > 0) {
                        List<PlayerEntity> players = getPlayerFromChunk(chunk, world);
                        final int r = remaining;
                        players.forEach(
                                player2 -> ((ServerPlayerEntity) player2).networkHandler.sendPacket(
                                        new TitleS2CPacket(Text.of(String.valueOf(r)))
                                )
                        );
                        /*MinecraftServer server = serverPlayer.getServer();
                        List<ServerPlayerEntity> playerList = server.getPlayerManager().getPlayerList();
                        for (ServerPlayerEntity entity : playerList) {
                            entity.sendMessage(Text.of("区块:" + pos.toString() + "将在" + remaining + "秒后消失"));
                        }*/
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
    @Unique
    private List<PlayerEntity> getPlayerFromChunk(Chunk chunk, World world) {
        ChunkPos pos = chunk.getPos();
        Box box = new Box(new Vec3d(pos.getStartX() - 1, world.getBottomY(), pos.getStartZ() - 1), new Vec3d(pos.getEndX() + 1, world.getTopY(), pos.getEndZ() + 1));
        List<Entity> list = world.getOtherEntities(null, box, e -> (e instanceof PlayerEntity player) && !player.isSpectator());
        return list.stream().map(entity -> (PlayerEntity) entity).collect(Collectors.toList());
    }
}
