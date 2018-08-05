package bukkit.world.generator;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class VoidGenerator extends ChunkGenerator {
    @Override
    public byte[] generate(World world, Random random, int _x, int _y) {
        return new byte[32768];
    }
}
