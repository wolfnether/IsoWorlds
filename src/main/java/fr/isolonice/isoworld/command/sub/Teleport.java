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

/**
 * Created by Edwin on 14/10/2017.
 */

import fr.isolonice.isoworld.Isoworld;
import fr.isolonice.isoworld.location.Locations;
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
import java.util.List;
import java.util.Optional;

import static fr.isolonice.isoworld.util.Utils.isLocked;

public class Teleport implements CommandCallable {

    private final Isoworld plugin = Isoworld.instance;

    @Override
    public CommandResult process(CommandSource source, String args) throws CommandException {

        Player pPlayer = (Player) source;
        int length = 0;
        String[] arg = args.split(" ");
        length = arg.length;

        // Check if world exists
        if (!Utils.isPresent(pPlayer, Msg.keys.SQL, true)) {
            pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder(Msg.keys.EXISTE_PAS_IWORLD).color(TextColors.RED))).build()));
            return CommandResult.success();
        }

        // Check if is world is loaded
        if (!(Sponge.getServer().getWorld(pPlayer.getUniqueId().toString() + "-IsoWorld").get().isLoaded())) {
            pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder("Sijania indique que votre IsoWorld doit être chargé pour en changer le temps.").color(TextColors.AQUA))).build()));
            return CommandResult.success();
        }

        // Si la méthode renvoi vrai alors on return car le lock est défini, sinon elle le set auto
        if (isLocked(pPlayer, String.class.getName())) {
            return CommandResult.success();
        }

        Utils.cm("Total arguments" + length);
        Utils.cm("Total arguments2" + args);
        Utils.cm("Total arguments3" + arg);
        if (length != 2) {
            Text message = Text.of(Msg.keys.INVALIDE_JOUEUR);
            pPlayer.sendMessage(message);
            return CommandResult.success();
        }

        Optional<Player> target = Sponge.getServer().getPlayer(arg[0]);
        Player player;
        if (!target.isPresent()) {
            Text message = Text.of("Le joueur indiqué n'est pas connecté, ou vous avez mal entré son pseudonyme.");
            pPlayer.sendMessage(message);
            return CommandResult.success();
        } else {
            player = target.get().getPlayer().get();
            Locations.teleport(player, arg[1]);
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