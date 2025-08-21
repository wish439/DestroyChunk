package org.wishtoday.ps.destroychunk.Util;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.ChunkPos;

public class IdentifierUtil {
    public static Identifier ofThis(String id) {
        return Identifier.of("destroy_chunk",id);
    }
}
