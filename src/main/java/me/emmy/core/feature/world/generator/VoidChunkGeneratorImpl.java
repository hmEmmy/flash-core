package me.emmy.core.feature.world.generator;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

/**
 * @author Emmy
 * @project Flash-Core
 * @since 30/03/2025
 */
public class VoidChunkGeneratorImpl extends ChunkGenerator {
    /**
     * Creates the chunk data for the world generator.
     *
     * @param world       the world to generate the chunk data for
     * @param random      the random seed for chunk generation
     * @param x           the chunk's x-coordinate
     * @param z           the chunk's z-coordinate
     * @param biomeGrid   the biome grid for the chunk
     * @return the chunk data for the world (empty world in this case)
     */
    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biomeGrid) {
        ChunkData chunkData = this.createChunkData(world);
        for (int xPos = 0; xPos < 16; xPos++) {
            for (int zPos = 0; zPos < 16; zPos++) {
                biomeGrid.setBiome(xPos, zPos, Biome.PLAINS);
            }
        }

        return chunkData;
    }
}