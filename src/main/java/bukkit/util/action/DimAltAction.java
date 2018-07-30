/*
 * This file is part of Isoworlds, licensed under the MIT License (MIT).
 *
 * Copyright (c) Edwin Petremann <https://github.com/Isolonice/>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package bukkit.util.action;

import bukkit.Main;
import bukkit.util.console.Command;
import bukkit.util.console.Logger;
import bukkit.world.generator.trashGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DimAltAction {

    private static final Main plugin = Main.instance;

    public static void generateDim() {
        // Remove files
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

            @Override
            public void run() {

                String[] dimsSkyblock = new String[]{"MS3", "SF3", "AS2", "PO2", "PO2K"};
                Map<Integer, String> dims = new HashMap<>();
                dims.put(99997, "trash");

                // Si contient alors on met pas le minage
                if (!Arrays.asList(dimsSkyblock).contains(plugin.servername)) {
                    dims.put(99999, "exploration");
                    dims.put(99998, "minage");
                }
                for (Map.Entry<Integer, String> dim : dims.entrySet()) {
                    WorldCreator worldGenerator = new WorldCreator(dim.getValue());
                    if (dim.getValue().equals("trash")) {
                        worldGenerator.generator(new trashGenerator());
                    }
                    // Create
                    Bukkit.getServer().createWorld(worldGenerator);

                    // Load world
                    Bukkit.getServer().createWorld(worldGenerator);

                    // Set properties
                    setWorldProperties(dim.getValue());
                }
            }
        }, 60 * 20);
    }

    private static void setWorldProperties(String worldname) {

        // Properties of Isoworld
        World world = Bukkit.getServer().getWorld(worldname);

        Logger.severe("Size: " + 3000 + " " + 3000);
        Command.sendCmd("wb " + worldname + " set " + 3000 + " " + 3000 + " 0 0");

        if (world != null) {
            Block yLoc = world.getHighestBlockAt(0, 0);
            world.setPVP(false);
            world.setSpawnLocation(0, yLoc.getY(), 0);
            world.setAutoSave(true);
        }
        Logger.info("WorldProperties à jour");
    }
}
