package sponge.world.generator;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.ImmutableBiomeVolume;
import org.spongepowered.api.world.extent.MutableBlockVolume;
import org.spongepowered.api.world.gen.GenerationPopulator;

public class TrashGenerator implements GenerationPopulator {
    @Override
    public void populate(World world, MutableBlockVolume buffer, ImmutableBiomeVolume biomes) {
        for (int x = buffer.getBlockMin().getX(); x <= buffer.getBlockMax().getX(); x++) {
            for (int z = buffer.getBlockMin().getZ(); z <= buffer.getBlockMax().getZ(); z++) {
                buffer.setBlockType(x, 63, z, BlockTypes.BARRIER, Cause.source(this).build());
                buffer.setBlockType(x, 62, z, BlockTypes.BEDROCK, Cause.source(this).build());
            }
        }
    }
}
