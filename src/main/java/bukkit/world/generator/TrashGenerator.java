package bukkit.world.generator;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class TrashGenerator extends ChunkGenerator {

    @Override
    public byte[] generate(World world, Random random, int _x, int _y) {
        byte[] chunk = new byte[32768];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunk[(x * 16 + z) * 128 + 63] = (byte) 166; //Barrier
                chunk[(x * 16 + z) * 128 + 62] = (byte) 7; //bedrock
            }
        }
        return chunk;
    }
}
