package org.wishtoday.ps.destroychunk.Task.Tasks;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.Chunk;
import org.wishtoday.ps.destroychunk.Helper.ChunkHelper;
import org.wishtoday.ps.destroychunk.Util.ChunkUtil;

public class ClearChunkTask extends ClearBlocksTask {
    private final ChunkHelper helper = ChunkHelper.getInstance();
    private final Chunk chunk;
    public ClearChunkTask(ServerWorld world, int maxNumInATick, Chunk chunk) {
        super(world, maxNumInATick, ChunkUtil.getBlocksPosFromChunk(chunk, world));
        this.chunk = chunk;
        helper.addToChunks(chunk);
        System.out.println("ClearChunkTask#s i c");
    }

    @Override
    public void onRemove() {
        super.onRemove();
        helper.removeFromChunks(chunk);
    }
}
