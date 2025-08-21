package org.wishtoday.ps.destroychunk.Task.Tasks;

import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.wishtoday.ps.destroychunk.Task.Task;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class ClearBlocksTask implements Task {
    private int maxNumInATick;
    private ServerWorld world;
    private Queue<BlockPos> queue;
    private int processed;

    public ClearBlocksTask(ServerWorld world
            , int maxNumInATick, @NotNull Collection<BlockPos> blocks) {
        this.world = world;
        this.maxNumInATick = maxNumInATick;
        this.queue = new LinkedList<>(blocks);
    }
    public ClearBlocksTask(ServerWorld world
            , int maxNumInATick, BlockPos startPos, BlockPos endPos) {
        this.world = world;
        this.maxNumInATick = maxNumInATick;
        Iterable<BlockPos> iterate = BlockPos.iterate(startPos, endPos);
        this.queue = new LinkedList<>();
        for (BlockPos blockPos : iterate) {
            this.queue.add(blockPos);
        }
    }

    @Override
    public void beforeWhile() {
        processed = 0;
    }

    @Override
    public void tickAction() {
        BlockPos pos = queue.poll();
        if (!world.getBlockState(pos).isAir()) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
        }
        processed++;
    }

    @Override
    public boolean isFinished() {
        return queue.isEmpty();
    }

    @Override
    public boolean shouldStop() {
        return queue.isEmpty() || processed >= maxNumInATick;
    }
}
