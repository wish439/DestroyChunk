package org.wishtoday.ps.destroychunk.mixin;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.wishtoday.ps.destroychunk.Task.ClearBlocksTask;
import org.wishtoday.ps.destroychunk.Task.TaskManager;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "damage", at = @At("RETURN"))
    private void onDamage(DamageSource source
            , float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (!(livingEntity instanceof PlayerEntity player)) return;
        World world = player.getWorld();
        if (world.isClient()) return;
        Chunk chunk = world.getChunk(player.getBlockPos());
        ChunkPos pos = chunk.getPos();
        int startX = pos.getStartX();
        int endY = world.getBottomY();
        int startZ = pos.getStartZ();
        int endX = pos.getEndX();
        int topY = world.getTopY();
        int endZ = pos.getEndZ();
        List<BlockPos> list = new ArrayList<>();
        for (int y = topY; y >= endY; y--) {
            for (int x = startX; x <= endX; x++) {
                for (int z = startZ; z <= endZ; z++) {
                    if (world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR) continue;
                    list.add(new BlockPos(x, y, z));
                }
            }
        }
        TaskManager.getInstance().addTask(new ClearBlocksTask((ServerWorld) world, 100 * 100, list));
    }
}
