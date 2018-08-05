package bukkit.world.generator;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class FlatGenerator extends ChunkGenerator {
    @Override
    public byte[] generate(World world, Random random, int _x, int _y) {
        byte[] chunk = new byte[32768];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunk[(x * 16 + z) * 128] = (byte) Material.BEDROCK.getId();
                chunk[(x * 16 + z) * 128 + /*TODO set y max value*/] = (byte) Material.GRASS.getId();
                for (int y = 1; y < /*TODO set y max value*/; y++) {
                    chunk[(x * 16 + z) * 128 + y] = (byte) Material.DIRT.getId();
                }
            }
        }
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                chunk[(x * 16 + z) * 128 + 63] = (byte) 166; //Barrier
                chunk[(x * 16 + z) * 128 + 62] = (byte) 7; //bedrock
            }
        }
        return chunk;
    }
}
