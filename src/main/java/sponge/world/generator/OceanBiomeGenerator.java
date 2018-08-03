package sponge.world.generator;

import org.spongepowered.api.world.biome.BiomeTypes;
import org.spongepowered.api.world.extent.MutableBiomeVolume;
import org.spongepowered.api.world.gen.BiomeGenerator;

public class OceanBiomeGenerator implements BiomeGenerator {
    @Override
    public void generateBiomes(MutableBiomeVolume buffer) {
        for (int x = buffer.getBiomeMin().getX(); x <= buffer.getBiomeMin().getZ(); x++) {
            for (int z = buffer.getBiomeMin().getZ(); z <= buffer.getBiomeMax().getZ(); z++) {
                buffer.getRelativeBiomeView().setBiome(x, 60, z, BiomeTypes.OCEAN);
            }
        }
    }
}
