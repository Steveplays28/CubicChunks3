package io.github.opencubicchunks.cubicchunks.chunk.biome;

import io.github.opencubicchunks.cubicchunks.CubicChunks;
import io.github.opencubicchunks.cubicchunks.chunk.IBigCube;
import io.github.opencubicchunks.cubicchunks.chunk.ICubeProvider;
import io.github.opencubicchunks.cubicchunks.utils.Coords;
import net.minecraft.core.IdMap;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.jetbrains.annotations.Nullable;

public class ColumnBiomeContainer extends ChunkBiomeContainer {
    private static final Biome DUMMY_BIOME = BuiltinRegistries.BIOME.get(Biomes.FOREST.location());

    private Level level;

    public ColumnBiomeContainer(IdMap<Biome> idMap, Biome[] biomes) {
        super(idMap, biomes);
    }

    public ColumnBiomeContainer(IdMap<Biome> idMap, int[] is) {
        super(idMap, is);
    }

    public ColumnBiomeContainer(IdMap<Biome> idMap, ChunkPos chunkPos, BiomeSource biomeSource) {
        super(idMap, chunkPos, biomeSource);
    }

    public ColumnBiomeContainer(IdMap<Biome> idMap, ChunkPos chunkPos, BiomeSource biomeSource, @Nullable int[] is) {
        super(idMap, chunkPos, biomeSource, is);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Biome getNoiseBiome(int biomeX, int biomeY, int biomeZ) {
        if(this.level == null) {
            return DUMMY_BIOME;
        }
        int blockX = Coords.blockToCube(biomeX) << 2;
        int blockY = Coords.blockToCube(biomeY) << 2;
        int blockZ = Coords.blockToCube(biomeZ) << 2;
        IBigCube icube = ((ICubeProvider) level.getChunkSource()).getCube(blockX, blockY, blockZ, ChunkStatus.BIOMES, false);
        if(icube == null) {
            if(!level.isClientSide())
                CubicChunks.LOGGER.warn("Tried to get biome at BLOCK pos {} {} {}, but cube isn't loaded. Returning dummy biome", blockX, blockY, blockZ);
            return DUMMY_BIOME;
        }

        CubeBiomeContainer cubeBiomes = icube.getCubeBiomes();
        if(cubeBiomes != null) { //noice
            return cubeBiomes.getNoiseBiome(biomeX, biomeY, biomeZ);
        }
        CubicChunks.LOGGER.error("Tried to get biome at BLOCK pos {} {} {}, Cube didn't contain a Biome Container", blockX, blockY, blockZ);
        return DUMMY_BIOME;
    }
}
