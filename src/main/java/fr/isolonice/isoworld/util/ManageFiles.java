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

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.util.ArrayList;
import java.util.List;

public class ManageFiles {

    public static void copyFileOrFolder(File source, File dest, CopyOption... options) throws IOException {
        if (source.isDirectory())
            copyFolder(source, dest, options);
        else {
            ensureParentFolder(dest);
            copyFile(source, dest, options);
        }
    }

    private static void copyFolder(File source, File dest, CopyOption... options) throws IOException {
        if (!dest.exists())
            dest.mkdirs();
        File[] contents = source.listFiles();
        if (contents != null) {
            for (File f : contents) {
                File newFile = new File(dest.getAbsolutePath() + File.separator + f.getName());
                if (f.isDirectory())
                    copyFolder(f, newFile, options);
                else
                    copyFile(f, newFile, options);
            }
        }
    }

    private static void copyFile(File source, File dest, CopyOption... options) throws IOException {
        java.nio.file.Files.copy(source.toPath(), dest.toPath(), options);
    }

    private static void ensureParentFolder(File file) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists())
            parent.mkdirs();
    }

    // Move directory
    public static boolean move(String source, String dest) {
        // File (or Directory) to be moved
        File file = new File(source);

        // Destination directory
        File dir = new File(dest);

        // Move file to a new directory

        return file.renameTo(new File(dir, file.getName()));
    }

    // Récupère la liste des dossiers tag
    // Si contient @ alors add to list et retourne
    public static List<File> getOutSAS(File currDir) {
        List<File> dirs = new ArrayList<>();
        for (File file : currDir.listFiles()) {
            if (file.isDirectory() & file.getName().contains("-IsoWorld")) {
                dirs.add(file);
            }
        }
        return dirs;
    }

    // Renommer dossier
    public static Boolean rename(String path, String newname) {
        File file = new File(path);
        if (!file.isDirectory()) {
            return false;
        }
        // ISOWORLDS on unload auto
        if (path.contains("@TEMP-PUSH")) {
            path = path.split("@TEMP-PUSH")[0];
        }
        file.renameTo(new File(path + newname));
        return true;
    }

    public static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

    // Récupération du chemin racine
    public static String getPath() {
        return System.getProperty("user.dir") + "/Isolonice/";
    }

}
