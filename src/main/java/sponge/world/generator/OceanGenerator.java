package sponge.world.generator;

import com.flowpowered.noise.Noise;
import com.flowpowered.noise.NoiseQuality;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.ImmutableBiomeVolume;
import org.spongepowered.api.world.extent.MutableBlockVolume;
import org.spongepowered.api.world.gen.GenerationPopulator;

public class OceanGenerator implements GenerationPopulator {

    @Override
    public void populate(World world, MutableBlockVolume buffer, ImmutableBiomeVolume biomes) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                double height = 40 + 10 * Noise.valueCoherentNoise3D(buffer.getBlockMin().getX() + x, 64, buffer.getBlockMin().getZ() + z, Math.toIntExact(world.getProperties().getSeed()), NoiseQuality.STANDARD);
                buffer.setBlockType(x, 0, z, BlockTypes.BEDROCK, Cause.source(this).build());
                for (int y = 1; y <= height - 2; y++) {
                    buffer.setBlockType(x, y, z, BlockTypes.STONE, Cause.source(this).build());
                }
                for (int y = (int) height - 1; y <= height + 2; y++) {
                    buffer.setBlockType(x, y, z, BlockTypes.SAND, Cause.source(this).build());
                }
                for (int y = (int) height + 3; y <= 60; y++) {
                    buffer.setBlockType(x, y, z, BlockTypes.WATER, Cause.source(this).build());
                }
            }
        }
    }
}
