package org.wishtoday.ps.destroychunk.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "damage",at = @At("HEAD"))
    private void onDamage(DamageSource source
            , float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (!(livingEntity instanceof PlayerEntity player)) return;
        World world = player.getWorld();
        Chunk chunk = world.getChunk(player.getBlockPos());
        ChunkPos pos = chunk.getPos();
        int startX = pos.getStartX();
        System.out.println(startX);
        int endY = world.getBottomY();
        int startZ = pos.getStartZ();
        System.out.println(startZ);
        int endX = pos.getEndX();
        System.out.println(endX);
        int topY = world.getTopY();
        int endZ = pos.getEndZ();
        System.out.println(endZ);
        for (int x = startX; x <= endX; x++) {
            for (int z = startZ; z <= endZ; z++) {
                for (int y = endY; y <= topY; y++) {
                    world.setBlockState(new BlockPos(x,y,z), Blocks.AIR.getDefaultState());
                }
            }
        }
    }
}
