package bukkit.world.generator;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;

import java.util.Random;

public class OceanGenerator extends ChunkGenerator {
    @Override
    public byte[] generate(World world, Random random, int _x, int _z) {
        byte[] chunk = new byte[32768];
        PerlinOctaveGenerator generator = new PerlinOctaveGenerator(world.getSeed(), 10);
        generator.setScale(0.01);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                double height = 40 + 10 * generator.noise(_x * 16 + x, _z * 16 + z, 0.5, 0.5);
                chunk[(x * 16 + z) * 128] = (byte) Material.BEDROCK.getId();
                for (int y = 1; y <= height - 2; y++) {
                    chunk[(x * 16 + z) * 128 + y] = (byte) Material.STONE.getId();
                }
                for (int y = (int) height - 1; y <= height + 2; y++) {
                    chunk[(x * 16 + z) * 128 + y] = (byte) Material.SAND.getId();
                }
                for (int y = (int) height + 3; y <= 60; y++) {
                    chunk[(x * 16 + z) * 128 + y] = (byte) Material.STATIONARY_WATER.getId();
                }
            }
        }
        return chunk;
    }

    @Override
    public byte[][] generateBlockSections(World world, Random random, int _x, int _z, BiomeGrid biomes) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                biomes.setBiome(x, z, Biome.OCEAN);
            }
        }
        return super.generateBlockSections(world, random, _x, _z, biomes);
    }
}
