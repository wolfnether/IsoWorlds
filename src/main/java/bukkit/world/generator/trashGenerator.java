package bukkit.world.generator;

import bukkit.world.populator.TrashPopulator;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.List;

public class trashGenerator extends ChunkGenerator {
    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList(new TrashPopulator());
    }
}
