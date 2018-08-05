package sponge.world.generator;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.ImmutableBiomeVolume;
import org.spongepowered.api.world.extent.MutableBlockVolume;
import org.spongepowered.api.world.gen.GenerationPopulator;

public class FlatGenerator implements GenerationPopulator {
    @Override
    public void populate(World world, MutableBlockVolume buffer, ImmutableBiomeVolume biomes) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                buffer.setBlockType(x, 0, z, BlockTypes.BEDROCK, Cause.source(this).build());
                buffer.setBlockType(x, /*TODO set y max value*/, z, BlockTypes.GRASS, Cause.source(this).build());
                for (int y = 1; y < /*TODO set y max value*/; y++) {
                    buffer.setBlockType(x, y, z, BlockTypes.DIRT, Cause.source(this).build());
                }
            }
        }
    }
}
