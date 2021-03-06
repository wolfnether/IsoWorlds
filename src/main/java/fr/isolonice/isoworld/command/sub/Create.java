/*
 * This file is part of IsoWorlds, licensed under the MIT License (MIT).
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
package fr.isolonice.isoworld.command.sub;

import fr.isolonice.isoworld.Isoworld;
import fr.isolonice.isoworld.location.Locations;
import fr.isolonice.isoworld.util.ManageFiles;
import fr.isolonice.isoworld.util.Msg;
import fr.isolonice.isoworld.util.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static fr.isolonice.isoworld.util.Utils.setWorldProperties;

public class Create implements CommandCallable {
    private final Isoworld plugin = Isoworld.instance;

    @Override
    public CommandResult process(CommandSource source, String args) throws CommandException {

        // Variables
        String fullpath = "";
        String worldname = "";
        Player pPlayer = (Player) source;
        Utils.coloredMessage(pPlayer, Msg.keys.CREATION_IWORLD);
        fullpath = (ManageFiles.getPath() + Utils.PlayerToUUID(pPlayer) + "-IsoWorld");
        worldname = (pPlayer.getUniqueId().toString() + "-IsoWorld");
        Utils.cm("IsoWorld name: " + worldname);
        String[] arg = args.split(" ");
        int size = arg.length;

        // SELECT WORLD
        if (Utils.isPresent(pPlayer, Msg.keys.SQL, false)) {
            pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder(Msg.keys.EXISTE_IWORLD).color(TextColors.AQUA))).build()));
            return CommandResult.success();
        }

        // Check si le monde existe déjà
        if (Sponge.getServer().getWorldProperties(worldname).isPresent()) {
            return CommandResult.success();
        }

        // Vérifie le nb argument
        if (size < 1) {
            pPlayer.sendMessage(Text.of(Text.builder("--------------------- [ ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder("IsoWorlds ").color(TextColors.AQUA)))
                    .append(Text.of(Text.builder("] ---------------------").color(TextColors.GOLD)))
                    .build()));

            pPlayer.sendMessage(Text.of(Text.builder(" ").color(TextColors.GOLD).build()));

            // Soleil
            Text isoworld = Text.of(Text.builder("Sijania vous propose 4 types de IsoWorld:").color(TextColors.AQUA).build());
            pPlayer.sendMessage(isoworld);
            Text iw = Text.of(Text.builder("- FLAT/OCEAN/NORMAL/VOID: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder("/iw creation [TYPE]").color(TextColors.AQUA))).build());
            pPlayer.sendMessage(iw);
            return CommandResult.success();
        }

        Utils.cm("DEBUGGGG: " + arg[0]);

        File sourceFile;
        switch (arg[0]) {
            case ("n"):
                sourceFile = new File(ManageFiles.getPath() + "PATERN/");
                Utils.cm("[TRACKING-IW] PATERN NORMAL: " + pPlayer.getName());
                break;
            case ("v"):
                sourceFile = new File(ManageFiles.getPath() + "PATERN/");
                Utils.cm("[TRACKING-IW] PATERN VOID: " + pPlayer.getName());
                break;
            case ("o"):
                sourceFile = new File(ManageFiles.getPath() + "PATERN/");
                Utils.cm("[TRACKING-IW] PATERN OCEAN: " + pPlayer.getName());
                break;
            case ("f"):
                sourceFile = new File(ManageFiles.getPath() + "PATERN/");
                Utils.cm("[TRACKING-IW] PATERN FLAT: " + pPlayer.getName());
                break;
            default:
                return CommandResult.success();
        }

        File destFile = new File(fullpath);


        try {
            ManageFiles.copyFileOrFolder(sourceFile, destFile);
        } catch (IOException ie) {
            ie.printStackTrace();
            Utils.coloredMessage(pPlayer, Msg.keys.SQL);
            return CommandResult.success();
        }

        // Création properties
        setWorldProperties(worldname, pPlayer);

        // INSERT
        if (Utils.setIsoWorld(pPlayer, Msg.keys.SQL)) {
            // INSERT TRUST
            if (Utils.setTrust(pPlayer, pPlayer.getUniqueId(), Msg.keys.SQL)) {
                // Chargement
                Sponge.getGame().getServer().loadWorld(worldname);

                pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: ").color(TextColors.GOLD)
                        .append(Text.of(Text.builder(Msg.keys.SUCCES_CREATION_1).color(TextColors.AQUA))).build()));
                // Téléport
                Locations.teleport(pPlayer, worldname);
                pPlayer.sendTitle(Utils.titleSubtitle(Msg.keys.TITRE_BIENVENUE_1 + pPlayer.getName(), Msg.keys.TITRE_BIENVENUE_2));
            }
        }
        return CommandResult.success();
    }

    @Override
    public List<String> getSuggestions(CommandSource source, String arguments, @Nullable Location<World> targetPosition) throws CommandException {
        return null;
    }

    @Override
    public boolean testPermission(CommandSource source) {
        return false;
    }

    @Override
    public Optional<Text> getShortDescription(CommandSource source) {
        return null;
    }

    @Override
    public Optional<Text> getHelp(CommandSource source) {
        return null;
    }

    @Override
    public Text getUsage(CommandSource source) {
        return null;
    }
}