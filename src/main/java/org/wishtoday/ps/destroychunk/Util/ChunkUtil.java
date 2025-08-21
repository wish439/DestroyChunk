package org.wishtoday.ps.destroychunk.Util;

import com.google.common.collect.Sets;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

import java.util.Set;
import java.util.concurrent.Callable;

public class ChunkUtil {
    public static Set<BlockPos> getBlocksPosFromChunk(Chunk chunk, ServerWorld world) {
        Callable<Set<BlockPos>> runnable = () -> getBlocksPosFromChunk2(chunk, world);
        try {
            return runnable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBlocksPosFromChunk2(chunk, world);
    }

    public static Set<BlockPos> getBlocksPosFromChunk2(Chunk chunk, ServerWorld world) {
        ChunkPos pos = chunk.getPos();
        int startX = pos.getStartX();
        int endX = pos.getEndX();
        int startZ = pos.getStartZ();
        int endZ = pos.getEndZ();
        int endY = world.getBottomY();
        int topY = world.getTopY();
        Set<BlockPos> set = Sets.newHashSet();
        for (int y = topY; y >= endY; y--) {
            for (int x = startX; x <= endX; x++) {
                for (int z = startZ; z <= endZ; z++) {
                    if (world.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.AIR) continue;
                    set.add(new BlockPos(x, y, z));
                }
            }
        }
        return set;
    }

    public static String chunkPosNormalize(ChunkPos pos) {
        return pos.toString().replace("[", "").replace("]", "").replace(", ", ".");
    }
}
