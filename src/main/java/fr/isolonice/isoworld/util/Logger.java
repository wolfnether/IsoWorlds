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
package fr.isolonice.isoworld.util;


import fr.isolonice.isoworld.Isoworld;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Logger {
    private static final Isoworld instance = Isoworld.instance;

    public static void info(String s) {
        instance.getGame().getServer().getConsole().sendMessage(Text.of(Text.builder("[").color(TextColors.WHITE)
                .append(Text.of(Text.builder("IW").color(TextColors.GOLD)
                        .append(Text.of(Text.builder("] ").color(TextColors.WHITE)
                                .append(Text.builder(s).color(TextColors.GREEN).build())))))));
    }

    public static void warning(String s) {
        instance.getGame().getServer().getConsole().sendMessage(Text.of(Text.builder("[").color(TextColors.WHITE)
                .append(Text.of(Text.builder("IW").color(TextColors.GOLD)
                        .append(Text.of(Text.builder("] ").color(TextColors.WHITE)
                                .append(Text.builder(s).color(TextColors.GOLD).build())))))));
    }

    public static void severe(String s) {
        instance.getGame().getServer().getConsole().sendMessage(Text.of(Text.builder("[").color(TextColors.WHITE)
                .append(Text.of(Text.builder("IW").color(TextColors.GOLD)
                        .append(Text.of(Text.builder("] ").color(TextColors.WHITE)
                                .append(Text.builder(s).color(TextColors.RED).build())))))));
    }

    public static void tracking(String s) {
        instance.getGame().getServer().getConsole().sendMessage(Text.of(Text.builder("[").color(TextColors.WHITE)
                .append(Text.of(Text.builder("IW").color(TextColors.GOLD)
                        .append(Text.of(Text.builder("] ").color(TextColors.WHITE)
                                .append(Text.builder(s).color(TextColors.AQUA).build())))))));
    }

    public static void tag() {
        instance.getGame().getServer().getConsole().sendMessage(Text.of(Text.builder("[IW]: ").color(TextColors.GOLD) + " _____          __          __           _      _      "));
        instance.getGame().getServer().getConsole().sendMessage(Text.of(Text.builder("[IW]: ").color(TextColors.GOLD) + "|_   _|         \\ \\        / /          | |    | |     "));
        instance.getGame().getServer().getConsole().sendMessage(Text.of(Text.builder("[IW]: ").color(TextColors.GOLD) + "  | |   ___   ___\\ \\  /\\  / /___   _ __ | |  __| | ___ "));
        instance.getGame().getServer().getConsole().sendMessage(Text.of(Text.builder("[IW]: ").color(TextColors.GOLD) + "  | |  / __| / _ \\\\ \\/  \\/ // _ \\ | '__|| | / _` |/ __|"));
        instance.getGame().getServer().getConsole().sendMessage(Text.of(Text.builder("[IW]: ").color(TextColors.GOLD) + " _| |_ \\__ \\| (_) |\\  /\\  /| (_) || |   | || (_| |\\__ \\"));
        instance.getGame().getServer().getConsole().sendMessage(Text.of(Text.builder("[IW]: ").color(TextColors.GOLD) + "|_____||___/ \\___/  \\/  \\/  \\___/ |_|   |_| \\__,_||___/"));
    }

}
