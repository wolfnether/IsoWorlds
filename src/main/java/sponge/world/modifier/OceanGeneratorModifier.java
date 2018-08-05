package sponge.world.modifier;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.storage.WorldProperties;
import sponge.world.generator.OceanBiomeGenerator;
import sponge.world.generator.OceanGenerator;

public class OceanGeneratorModifier implements WorldGeneratorModifier {
    @Override
    public void modifyWorldGenerator(WorldProperties world, DataContainer settings, WorldGenerator worldGenerator) {
        worldGenerator.setBiomeGenerator(new OceanBiomeGenerator());
        worldGenerator.setBaseGenerationPopulator(new OceanGenerator());
    }

    @Override
    public String getId() {
        return "Isoworlds:OceanModifier";
    }

    @Override
    public String getName() {
        return "ocean modifier";
    }
}
