package org.wishtoday.ps.destroychunk.Util;

import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class StructureUtil {
    public static void saveChunkToStructure(
            Chunk chunk,
            ServerWorld world,
            boolean includeEntities,
            @Nullable Block ignoredBlock,
            Identifier structureName) {
        ChunkPos pos = chunk.getPos();
        BlockPos startPos = new BlockPos(pos.getStartX(), world.getBottomY(), pos.getStartZ());
        int height = world.getTopY() - world.getBottomY();
        Vec3i size = new Vec3i(16, height, 16);
        StructureTemplateManager manager = world.getStructureTemplateManager();
        StructureTemplate template = manager.getTemplateOrBlank(structureName);
        template.saveFromWorld(world, startPos, size, includeEntities, ignoredBlock);
        manager.saveTemplate(structureName);
    }


    public static void saveChunkToStructure(
            Chunk chunk
            , ServerWorld world
            , boolean includeEntities
            , @Nullable Block ignoredBlock
            , String structureName) {
        Identifier identifier = IdentifierUtil.ofThis(structureName);
        saveChunkToStructure(chunk, world, includeEntities, ignoredBlock, identifier);
    }

    public static void saveChunkToStructure(
            Chunk chunk
            , ServerWorld world
            , String structureName) {
        saveChunkToStructure(chunk, world, true, null, structureName);
    }

    public static void saveChunkToStructure(
            Chunk chunk
            , ServerWorld world) {
        saveChunkToStructure(chunk, world, true, null, chunk.getPos().toString());
    }

    public static void loadChunkFromChunk(
            ServerWorld world
            , Chunk chunk) {
        StructureTemplateManager manager = world.getStructureTemplateManager();
        ChunkPos pos = chunk.getPos();
        Optional<StructureTemplate> optional = manager.getTemplate(IdentifierUtil.ofThis(ChunkUtil.chunkPosNormalize(pos)));
        if (optional.isEmpty()) return;
        StructureTemplate template = optional.get();
        BlockPos blockPos = new BlockPos(pos.getStartX(), world.getBottomY(), pos.getStartZ());
        template.place(world, blockPos, blockPos, new StructurePlacementData().setIgnoreEntities(false), Random.create(), 2);
    }

}
