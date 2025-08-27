package org.wishtoday.ps.destroychunk.Helper;

import com.google.common.collect.Sets;
import net.minecraft.world.chunk.Chunk;

import java.util.HashSet;

public class ChunkHelper {
    private static ChunkHelper instance = new ChunkHelper();
    private HashSet<String> chunks = Sets.newHashSet();
    public void addToChunks(String chunk) {
        if (this.hasChunk(chunk)) return;
        chunks.add(chunk);
    }
    public void addToChunks(Chunk chunk) {
        addToChunks(chunk.toString());
    }
    public void removeFromChunks(String chunk) {
        if (!this.hasChunk(chunk)) return;
        chunks.remove(chunk);
    }
    public void removeFromChunks(Chunk chunk) {
        removeFromChunks(chunk.toString());
    }
    public boolean hasChunk(String chunk) {
        return chunks.contains(chunk);
    }
    public boolean hasChunk(Chunk chunk) {
        return hasChunk(chunk.toString());
    }
    private ChunkHelper() {}
    @SuppressWarnings({"LombokGetterMayBeUsed", "RedundantSuppression"})
    public static ChunkHelper getInstance() {
        return instance;
    }
}
