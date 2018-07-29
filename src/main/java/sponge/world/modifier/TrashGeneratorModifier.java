package sponge.world.modifier;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.storage.WorldProperties;
import sponge.world.generator.TrashGenerator;

public class TrashGeneratorModifier implements WorldGeneratorModifier {
    @Override
    public void modifyWorldGenerator(WorldProperties world, DataContainer settings, WorldGenerator worldGenerator) {
        worldGenerator.setBaseGenerationPopulator(new TrashGenerator());
        worldGenerator.getPopulators().clear();
        worldGenerator.getGenerationPopulators().clear();
    }

    public String getId() {
        return "Isoworlds:trashModifier";
    }

    @Override
    public String getName() {
        return "Trash modifier";
    }
}
