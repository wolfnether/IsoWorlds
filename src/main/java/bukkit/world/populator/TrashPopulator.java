package bukkit.world.populator;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class TrashPopulator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk source) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                source.getBlock(x, 63, z).setType(Material.BARRIER);
                source.getBlock(x, 62, z).setType(Material.BEDROCK);
            }
        }
    }
}
