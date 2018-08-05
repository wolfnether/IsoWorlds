package sponge.world.modifier;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.storage.WorldProperties;
import sponge.world.generator.FlatGenerator;

public class FlatGeneratorModifier implements WorldGeneratorModifier {
    @Override
    public void modifyWorldGenerator(WorldProperties world, DataContainer settings, WorldGenerator worldGenerator) {
        worldGenerator.setBaseGenerationPopulator(new FlatGenerator());
    }

    @Override
    public String getId() {
        return "Isoworlds:FlatModifier";
    }

    @Override
    public String getName() {
        return "flat modifier";
    }
}
