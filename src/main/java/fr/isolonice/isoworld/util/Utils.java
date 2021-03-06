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
import fr.isolonice.isoworld.location.Locations;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldArchetypes;
import org.spongepowered.api.world.gamerule.DefaultGameRules;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Utils {

    public static final Isoworld plugin = Isoworld.instance;

    public static final DataQuery toId = DataQuery.of("SpongeData", "dimensionId");

    // ------------------------------------------------- USER MANIPULATION
    // TPS
    private static final DecimalFormat tpsFormat = new DecimalFormat("#0.00");


    // ------------------------------------------------- MESSAGES

    // Méthode pour convertir type player à uuid
    public static UUID PlayerToUUID(Player player) {
        UUID uuid;
        uuid = player.getUniqueId();
        return (uuid);
    }

    // Envoie un message au serveur
    public static void sm(String msg) {
        Sponge.getServer().getBroadcastChannel().send(Text.of("[IsoWorlds]: " + msg));
    }

    // Envoie un message à la console
    public static void cm(String msg) {
        Sponge.getServer().getConsole().sendMessage(Text.of("[IsoWorlds]: " + msg));
    }

    // Tiltle with SubTitle
    public static Title titleSubtitle(String title, String subtitle) {
        Text Titre = Text.of(Text.builder(title).color(TextColors.GOLD).build());
        Text SousTitre = Text.of(Text.builder(subtitle).color(TextColors.AQUA).build());
        return (Title) Title.of(Titre, SousTitre);
    }

    // Send colored Message base
    public static void coloredMessage(Player pPlayer, String message) {
        pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: ").color(TextColors.GOLD)
                .append(Text.of(Text.builder(message).color(TextColors.AQUA))).build()));
    }


    // ------------------------------------------------- ISOWORLDS MANIPULATION

    // Get all IsoWorlds that trusted pPlayer
    public static ResultSet getAccess(Player pPlayer, String messageErreur) {
        String CHECK = "SELECT `UUID_W` FROM `autorisations` WHERE `UUID_P` = ? AND `SERVEUR_ID` = ?";
        String check_p;
        ResultSet result = null;
        try {
            PreparedStatement check = plugin.database.prepare(CHECK);

            // UUID _P
            check_p = Utils.PlayerToUUID(pPlayer).toString();
            check.setString(1, check_p);
            // SERVEUR_ID
            check.setString(2, plugin.servername);
            // Requête
            ResultSet rselect = check.executeQuery();
            if (rselect.isBeforeFirst()) {
                result = rselect;
                return result;
            }
        } catch (Exception se) {
            se.printStackTrace();
            Utils.cm(Msg.keys.SQL);
            pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder(messageErreur).color(TextColors.AQUA))).build()));
            return result;
        }
        return result;
    }

    // Get all trusted players of pPlayer's IsoWorld
    public static ResultSet getTrusts(Player pPlayer, String messageErreur) {
        String CHECK = "SELECT `UUID_P` FROM `autorisations` WHERE `UUID_W` = ? AND `SERVEUR_ID` = ?";
        String check_w;
        ResultSet result = null;
        try {
            PreparedStatement check = plugin.database.prepare(CHECK);

            // UUID _W
            check_w = pPlayer.getUniqueId().toString() + "-IsoWorld";
            check.setString(1, check_w);
            // SERVEUR_ID
            check.setString(2, plugin.servername);
            // Requête
            ResultSet rselect = check.executeQuery();
            if (rselect.isBeforeFirst()) {
                result = rselect;
                return result;
            }
        } catch (Exception se) {
            se.printStackTrace();
            Utils.cm(Msg.keys.SQL);
            pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder(messageErreur).color(TextColors.AQUA))).build()));
            return result;
        }
        return result;
    }

    // Check status of a IsoWorld, if is Pushed return true, else return false
    public static Boolean getStatus(String world, String messageErreur) {
        String CHECK = "SELECT STATUS FROM `isoworlds` WHERE `UUID_W` = ? AND `SERVEUR_ID` = ?";
        String check_w;
        try {
            PreparedStatement check = plugin.database.prepare(CHECK);

            // UUID_W
            check_w = world;
            check.setString(1, check_w);
            // SERVEUR_ID
            check.setString(2, plugin.servername);
            // Requête
            ResultSet rselect = check.executeQuery();
            Utils.cm(check.toString());
            Utils.cm("Debug 8");
            while (rselect.next()) {
                Utils.cm(rselect.toString());
                Utils.cm("Debug 9");
                if (rselect.getInt(1) == 1) {
                    Utils.cm("Debug 10");
                    return true;
                } else {
                    return false;
                }

            }
        } catch (Exception se) {
            se.printStackTrace();
            Utils.cm(messageErreur);
            return false;
        }
        return false;
    }

    // Set global status
    public static Boolean setGlobalStatus(String messageErreur) {
        String CHECK = "UPDATE `isoworlds` SET `STATUS` = 1 WHERE `SERVEUR_ID` = ?";
        String check_w;
        try {
            PreparedStatement check = plugin.database.prepare(CHECK);

            // SERVEUR_ID
            check.setString(1, plugin.servername);
            // Requête
            ResultSet rselect = check.executeQuery();
            Utils.cm(check.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Utils.cm(messageErreur);
            return false;
        }
        return true;
    }

    // Create IsoWorld for pPlayer
    public static Boolean setIsoWorld(Player pPlayer, String messageErreur) {
        String INSERT = "INSERT INTO `isoworlds` (`UUID_P`, `UUID_W`, `DATE_TIME`, `SERVEUR_ID`, `STATUS`, `DIMENSION_ID`) VALUES (?, ?, ?, ?, ?, ?)";
        String Iuuid_w;
        String Iuuid_p;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            PreparedStatement insert = plugin.database.prepare(INSERT);
            // UUID_P
            Iuuid_p = pPlayer.getUniqueId().toString();
            insert.setString(1, Iuuid_p);
            // UUID_W
            Iuuid_w = ((pPlayer.getUniqueId()) + "-IsoWorld");
            insert.setString(2, Iuuid_w);
            // Date
            insert.setString(3, (timestamp.toString()));
            // Serveur_id
            insert.setString(4, plugin.servername);
            // STATUS
            insert.setInt(5, 0);
            // DIMENSION_ID
            int id = Utils.getNextDimensionId();
            if (id == 0) {
                return false;
            }
            insert.setInt(6, id);
            insert.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.cm(Msg.keys.SQL);
            pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder(messageErreur).color(TextColors.AQUA))).build()));
            return false;
        }
        return true;
    }

    // Create trust for uuidcible on pPlayer IsoWorld
    public static Boolean setTrust(Player pPlayer, UUID uuidcible, String messageErreur) {
        String INSERT = "INSERT INTO `autorisations` (`UUID_P`, `UUID_W`, `DATE_TIME`, `SERVEUR_ID`) VALUES (?, ?, ?, ?)";
        String Iuuid_w;
        String Iuuid_p;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            PreparedStatement insert = plugin.database.prepare(INSERT);
            // UUID_P
            Iuuid_p = uuidcible.toString();
            insert.setString(1, Iuuid_p);
            // UUID_W
            Iuuid_w = ((pPlayer.getUniqueId()) + "-IsoWorld");
            insert.setString(2, Iuuid_w);
            // Date
            insert.setString(3, (timestamp.toString()));
            // Serveur_id
            insert.setString(4, plugin.servername);
            insert.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.cm(Msg.keys.SQL);
            pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder(messageErreur).color(TextColors.AQUA))).build()));
            return false;
        }
        return true;
    }

    // Set status of IsoWorld (1 for Pushed, 0 for Present)
    // It returns true if pushed, false si envoyé ou à envoyer
    public static void setStatus(String world, Integer status, String messageErreur) {
        String CHECK = "UPDATE `isoworlds` SET `STATUS` = ? WHERE `UUID_W` = ? AND `SERVEUR_ID` = ?";
        String check_w;
        try {
            PreparedStatement check = plugin.database.prepare(CHECK);

            // STATUS
            check.setInt(1, status);
            // UUID_W
            check_w = (world);
            check.setString(2, check_w);
            // SERVEUR_ID
            check.setString(3, plugin.servername);
            // Requête
            Utils.cm("Debug 3: " + check.toString());
            check.executeUpdate();
        } catch (Exception se) {
            se.printStackTrace();
            Utils.cm(messageErreur);
        }
    }

    // Create world properties IsoWorlds
    public static WorldProperties setWorldProperties(String worldname, Player pPlayer) {

        // Check si world properties en place, création else
        Optional<WorldProperties> wp = Sponge.getServer().getWorldProperties(worldname);
        WorldProperties worldProperties;

        try {
            // Deal with permission of owner only
            int x;
            String username = worldname.split("-IsoWorld")[0];
            Optional<User> user = Utils.getPlayerFromUUID(UUID.fromString(username));
            if (!username.equals(pPlayer.getUniqueId().toString())) {
                // Global
                // Radius border 500
                if (user.get().hasPermission("isoworlds.size.1000")) {
                    x = 2000;
                    // Radius border 750
                } else if (user.get().hasPermission("isoworlds.size.750")) {
                    x = 1500;
                    // Radius border 1000
                } else if (user.get().hasPermission("isoworlds.size.500")) {
                    x = 1000;
                    // Radius border default
                } else {
                    x = 500;
                }
            } else {
                // Global
                // Radius border 500
                if (pPlayer.hasPermission("isoworlds.size.1000")) {
                    x = 2000;
                    // Radius border 750
                } else if (pPlayer.hasPermission("isoworlds.size.750")) {
                    x = 1500;
                    // Radius border 1000
                } else if (pPlayer.hasPermission("isoworlds.size.500")) {
                    x = 1000;
                    // Radius border default
                } else {
                    x = 500;
                }
            }


            if (wp.isPresent()) {
                worldProperties = wp.get();
                Utils.cm("WOLRD PROPERTIES: déjà présent");
                worldProperties.setKeepSpawnLoaded(false);
                worldProperties.setLoadOnStartup(false);
                worldProperties.setGenerateSpawnOnLoad(false);
                worldProperties.setGameRule(DefaultGameRules.MOB_GRIEFING, "false");
                worldProperties.setPVPEnabled(true);
                worldProperties.setWorldBorderCenter(Locations.getAxis(worldname).getX(), Locations.getAxis(worldname).getZ());
                worldProperties.setWorldBorderDiameter(x);
                worldProperties.setEnabled(false);
                worldProperties.setEnabled(true);
                Sponge.getServer().saveWorldProperties(worldProperties);
                // Border
                Optional<World> world = Sponge.getServer().getWorld(worldname);
                if (world.isPresent()) {
                    world.get().getWorldBorder().setDiameter(x);
                }
                Logger.warning("Border nouveau: " + x);
            } else {
                worldProperties = Sponge.getServer().createWorldProperties(worldname, WorldArchetypes.OVERWORLD);
                Utils.cm("WOLRD PROPERTIES: non présents, création...");
                worldProperties.setKeepSpawnLoaded(false);
                worldProperties.setLoadOnStartup(false);
                worldProperties.setGenerateSpawnOnLoad(false);
                worldProperties.setGameRule(DefaultGameRules.MOB_GRIEFING, "false");
                worldProperties.setPVPEnabled(true);
                worldProperties.setWorldBorderCenter(Locations.getAxis(worldname).getX(), Locations.getAxis(worldname).getZ());
                worldProperties.setWorldBorderDiameter(x);
                Sponge.getServer().saveWorldProperties(worldProperties);
                Logger.warning("Border nouveau: " + x);
            }
            Utils.cm("WorldProperties à jour");

        } catch (IOException | NoSuchElementException ie) {
            ie.printStackTrace();
            Utils.coloredMessage(pPlayer, Msg.keys.SQL);
            plugin.lock.remove(pPlayer.getUniqueId().toString() + ";" + String.class.getName());
            return null;
        }

        return worldProperties;
    }

    // Delete IsoWorld of pPlayer
    public static Boolean deleteIsoWorld(Player pPlayer, String messageErreur) {
        String Iuuid_p;
        String Iuuid_w;
        String DELETE_AUTORISATIONS = "DELETE FROM `autorisations` WHERE `UUID_W` = ? AND `SERVEUR_ID` = ?";
        String DELETE_IWORLDS = "DELETE FROM `isoworlds` WHERE `UUID_P` = ? AND `UUID_W` = ? AND `SERVEUR_ID` = ?";
        try {
            PreparedStatement delete_autorisations = plugin.database.prepare(DELETE_AUTORISATIONS);
            PreparedStatement delete_iworlds = plugin.database.prepare(DELETE_IWORLDS);
            Iuuid_p = pPlayer.getUniqueId().toString();
            Iuuid_w = (pPlayer.getUniqueId().toString() + "-IsoWorld");

            // delete autorisations
            delete_autorisations.setString(1, Iuuid_w);
            delete_autorisations.setString(2, plugin.servername);

            // delete iworld
            delete_iworlds.setString(1, Iuuid_p);
            delete_iworlds.setString(2, Iuuid_w);
            delete_iworlds.setString(3, plugin.servername);

            // execute
            delete_autorisations.executeUpdate();
            delete_iworlds.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.cm(Msg.keys.SQL);
            pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder(messageErreur).color(TextColors.AQUA))).build()));
            return false;
        }
        return true;
    }

    // Delete trust uuid of pPlayer's IsoWorld
    public static Boolean deleteTrust(Player pPlayer, UUID uuid, String messageErreur) {
        String Iuuid_p;
        String Iuuid_w;
        String DELETE_AUTORISATIONS = "DELETE FROM `autorisations` WHERE `UUID_P` = ? AND `UUID_W` = ? AND `SERVEUR_ID` = ?";
        try {
            PreparedStatement delete_autorisations = plugin.database.prepare(DELETE_AUTORISATIONS);
            Iuuid_p = uuid.toString();
            Iuuid_w = (pPlayer.getUniqueId().toString() + "-IsoWorld");

            // delete autorisation
            delete_autorisations.setString(1, Iuuid_p);
            delete_autorisations.setString(2, Iuuid_w);
            delete_autorisations.setString(3, plugin.servername);

            // execute
            delete_autorisations.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.cm(Msg.keys.SQL);
            pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder(messageErreur).color(TextColors.AQUA))).build()));
            return false;
        }
        return true;
    }

    // Check if uuid cible is trusted on pPlayer's IsoWorld
    public static Boolean isTrusted(Player pPlayer, UUID uuidcible, String messageErreur) {

        String CHECK = "SELECT * FROM `autorisations` WHERE `UUID_P` = ? AND `UUID_W` = ? AND `SERVEUR_ID` = ?";
        String check_w;
        String check_p;
        try {
            PreparedStatement check = plugin.database.prepare(CHECK);
            // UUID _P
            check_p = uuidcible.toString();
            check.setString(1, check_p);
            // UUID_W
            check_w = (pPlayer.getUniqueId() + "-IsoWorld");
            check.setString(2, check_w);
            // SERVEUR_ID
            check.setString(3, plugin.servername);
            // Requête
            ResultSet rselect = check.executeQuery();
            if (rselect.isBeforeFirst()) {
                return true;
            }
        } catch (Exception se) {
            se.printStackTrace();
            Utils.cm(Msg.keys.SQL);
            pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder(messageErreur).color(TextColors.AQUA))).build()));
            return false;
        }
        return false;
    }

    // Check if pPlayer's IsoWorld is created on database
    public static Boolean isPresent(Player pPlayer, String messageErreur, Boolean load) {

        String CHECK = "SELECT * FROM `isoworlds` WHERE `UUID_P` = ? AND `UUID_W` = ? AND `SERVEUR_ID` = ?";
        String check_w;
        String check_p;
        try {
            PreparedStatement check = plugin.database.prepare(CHECK);

            // UUID _P
            check_p = Utils.PlayerToUUID(pPlayer).toString();
            check.setString(1, check_p);
            // UUID_W
            check_w = (Utils.PlayerToUUID(pPlayer) + "-IsoWorld");
            check.setString(2, check_w);
            // SERVEUR_ID
            check.setString(3, plugin.servername);
            // Requête
            ResultSet rselect = check.executeQuery();

            if (rselect.isBeforeFirst()) {
                // Chargement si load = true
                setWorldProperties(Utils.PlayerToUUID(pPlayer) + "-IsoWorld", pPlayer);
                if (!Utils.getStatus(Utils.PlayerToUUID(pPlayer) + "-IsoWorld", Msg.keys.SQL)) {
                    if (load) {

                        // TEST
                        Path levelSponge = Paths.get(ManageFiles.getPath() + Utils.PlayerToUUID(pPlayer) + "-IsoWorld/" + "level_sponge.dat");
                        if (Files.exists(levelSponge)) {
                            DataContainer dc;
                            boolean gz = false;

                            // Find dat
                            try (GZIPInputStream gzip = new GZIPInputStream(Files.newInputStream(levelSponge, StandardOpenOption.READ))) {
                                dc = DataFormats.NBT.readFrom(gzip);
                                gz = true;

                                // get all id
                                ArrayList allId = Utils.getAllDimensionId(Msg.keys.SQL);

                                // get id
                                int dimId = Utils.getDimensionId(pPlayer, Msg.keys.SQL);

                                // Si non isoworld ou non défini
                                if (dimId == 0) {
                                    for (int i = 1000; i < Integer.MAX_VALUE; i++) {
                                        if (!allId.contains(i)) {
                                            Utils.setDimensionId(pPlayer, i, Msg.keys.SQL);
                                            dimId = i;
                                            break;
                                        }
                                    }
                                }

                                dc.set(toId, dimId);

                                // define dat
                                try (OutputStream os = getOutput(gz, levelSponge)) {
                                    DataFormats.NBT.writeTo(os, dc);
                                    os.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        Sponge.getServer().loadWorld(Utils.PlayerToUUID(pPlayer) + "-IsoWorld");
                    }
                }
                return true;
            }

        } catch (Exception se) {
            se.printStackTrace();
            Utils.cm(Msg.keys.SQL);
            pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder(messageErreur).color(TextColors.AQUA))).build()));
            return false;
        }
        return false;
    }

    private static OutputStream getOutput(boolean gzip, Path file) throws IOException {
        OutputStream os = Files.newOutputStream(file);
        if (gzip) {
            return new GZIPOutputStream(os, true);
        }

        return os;
    }

    // Check tag of pPlayer IsoWorld (@PUSH, @PUSHED, @PULL, @PULLED, @PUSHED@PULL, @PUSHED@PULLED)
    public static Boolean checkTag(Player pPlayer, String worldname) {

        // Création des chemins pour vérification
        File file = new File(ManageFiles.getPath() + worldname);
        File file2 = new File(ManageFiles.getPath() + worldname + "@PUSHED");

        // Si vrai alors en état @PUSHED en bdd
        if (Utils.getStatus(worldname, Msg.keys.SQL)) {
            Utils.cm("ISOWORLD: " + worldname + " EN ETAT @PUSHED");

            // !! Gestion des anomalies !!
            // Si le dossier sans tag et avec tag existe, alors il a été accédé par un moyen tier et on supprime le non TAG
            if (file.exists()) {
                if (file2.exists()) {
                    Logger.warning(" --- Anomalie (@PUSHED: Dossier isoworld et isoworld tag tous deux présents pour: " + worldname + " ---");
                    // Déchargement au cas ou
                    if (Sponge.getServer().getWorld(worldname).isPresent()) {
                        Sponge.getServer().unloadWorld(Sponge.getServer().getWorld(worldname).get());
                        Logger.warning(" --- Anomalie (@PUSHED: Déchargement du IsoWorld anormalement chargé: " + worldname + " ---");
                    }
                    // Suppression du dossier
                    ManageFiles.deleteDir(file);
                }
            }

            // !! Démarrage de la procédure de récupération !!
            // Si dossier @PUSHED alors on le met en @PULL pour lancer la procédure de récupération
            if (file2.exists()) {
                Utils.cm("PASSAGE EN @PULL: " + worldname);
                ManageFiles.rename(ManageFiles.getPath() + worldname + "@PUSHED", "@PULL");
                Utils.cm("PASSAGE EN @PULL OK: " + worldname);

                // Vérification pour savoir si le dossier existe pour permettre au joueur d'utiliser la cmd maison
                // On retourne faux pour dire que le monde n'est pas disponible
                // Le passage du statut à NON @PUSHED se fait dans la task
                Task task = Task.builder()
                        .execute(new PullTask(pPlayer, file))
                        .async()
                        .interval(1, TimeUnit.SECONDS)
                        .name("Self-Cancelling Timer Task")
                        .submit(plugin);
            } else {
                // Gestion du cas ou le dossier IsoWorld ne serait pas présent alors qu'il est @PUSHED en bdd
                Logger.warning(" --- Anomalie (@PUSHED): Dossier isoworld tag n'existe pas: " + worldname + " ---");
            }

            // Retourner faux pour indiquer que le dossier n'existe pas, il doit être en procédure
            return false;

        } else if (!Utils.getStatus(worldname, Msg.keys.SQL)) {
            Utils.cm("ISOWORLD DISPONIBLE: " + worldname + " - ETAT NON @PUSHED");

            // Vérification si le dossier @PUSHED n'existe pas, on le supprime dans ce cas, anomalie
            if (file2.exists()) {
                ManageFiles.deleteDir(file2);
                Utils.cm(": " + worldname);
                Logger.warning(" --- Anomalie (NON @PUSHED): Dossier isoworld et isoworld tag tous deux présents pour: " + worldname + " ---");
            }

            // IsoWorld disponible, retour vrai
            return true;
        } else {
            // Si ni @PUSHED ni NON @PUSHED en BDD alors on retourne faux car il doit y avoir un gros problème :)
            Logger.warning(" --- Anomalie (NI @PUSHED NI NON @PUSHE): IsoWorld: " + worldname + " ---");
            return false;
        }
    }

    // Used for construction, check if isoworld is in database (don't care charged or not)
    public static Boolean iwExists(String uuid, String messageErreur) {
        String CHECK = "SELECT * FROM `isoworlds` WHERE `UUID_P` = ? AND `UUID_W` = ? AND `SERVEUR_ID` = ?";
        String check_w;
        String check_p;
        try {
            PreparedStatement check = plugin.database.prepare(CHECK);
            // UUID _P
            check_p = uuid;
            check.setString(1, check_p);
            // UUID_W
            check_w = uuid + "-IsoWorld";
            check.setString(2, check_w);
            // SERVEUR_ID
            check.setString(3, plugin.servername);
            // Requête
            ResultSet rselect = check.executeQuery();
            if (rselect.isBeforeFirst()) {
                return true;
            }
        } catch (Exception se) {
            se.printStackTrace();
            Utils.cm(Msg.keys.SQL);
            return false;
        }
        return false;
    }

    // COPY FOR CHARGERCOMMANDE
    // Create world properties IsoWorlds
    public static WorldProperties setWorldPropertiesCharger(String worldname) {

        // Check si world properties en place, création else
        Optional<WorldProperties> wp = Sponge.getServer().getWorldProperties(worldname);
        WorldProperties worldProperties;

        try {
            if (wp.isPresent()) {
                worldProperties = wp.get();
                Utils.cm("WOLRD PROPERTIES: déjà présent");
            } else {
                worldProperties = Sponge.getServer().createWorldProperties(worldname, WorldArchetypes.OVERWORLD);
                Utils.cm("WOLRD PROPERTIES: non présents, création...");

            }

            // Global
            // Radius border 500
            int x = 500;
            worldProperties = Sponge.getServer().createWorldProperties(worldname, WorldArchetypes.OVERWORLD);
            worldProperties.setKeepSpawnLoaded(false);
            worldProperties.setLoadOnStartup(false);
            worldProperties.setGenerateSpawnOnLoad(false);
            worldProperties.setGameRule(DefaultGameRules.MOB_GRIEFING, "false");
            worldProperties.setPVPEnabled(true);
            worldProperties.setWorldBorderCenter(0, 0);
            worldProperties.setWorldBorderDiameter(x);

            // Sauvegarde
            Sponge.getServer().saveWorldProperties(worldProperties);
            Utils.cm("WorldProperties à jour");

        } catch (IOException | NoSuchElementException ie) {
            ie.printStackTrace();
            return null;
        }

        return worldProperties;
    }

    // ------------------------------------------------- LOCK AND CHECK SYSTEM

    // Vérifie si le lock est présent et renvoi vrai, sinon défini le lock et renvoi false
    public static Boolean isLocked(Player pPlayer, String className) {
        // Si le lock est set, alors on renvoie false avec un message de sorte à stopper la commande et informer le jouer
        if (checkLockFormat(pPlayer.getUniqueId().toString(), className)) {
            pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: Sijania indique que vous devez patienter avant de pouvoir utiliser de nouveau cette commande.").color(TextColors.GOLD)
                    .append(Text.of(Text.builder("").color(TextColors.AQUA))).build()));
            return true;
        } else {
            // On set lock
            plugin.lock.put(pPlayer.getUniqueId().toString() + ";" + className, 1);
            return false;
        }
    }

    // Vérifie si le lock est présent et renvoi vrai, sinon défini le lock et renvoi false
    public static Boolean iwInProcess(Player pPlayer, String worldname) {
        // Si le lock est set, alors on renvoie false avec un message de sorte à stopper la commande et informer le jouer
        if (checkLockFormat(worldname, worldname)) {
            pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: Sijania indique que vous devez patienter avant de pouvoir utiliser de nouveau cette commande.").color(TextColors.GOLD)
                    .append(Text.of(Text.builder("").color(TextColors.AQUA))).build()));
            return true;
        } else {
            // On set lock
            plugin.lock.put(worldname + ";" + worldname, 1);
            return false;
        }
    }

    // Check if mirror iw
    //0 normal - 1 anomally
    public static boolean isMirrored(World world) {
        // Check if file exist, to detect mirrors
        File file = new File(ManageFiles.getPath() + "/" + world.getName() + "@PUSHED");
        File file2 = new File(ManageFiles.getPath() + "/" + world.getName());
        // If exists and contains Isoworld
        return file.exists() & file2.exists() & world.getName().endsWith("-IsoWorld");
    }

    // Cooldown modèle: uuid;commande
    public static Boolean checkLockFormat(String pPlayer, String command) {
        // Si le tableau est null alors lock 0 sinon lock 1
        return plugin.lock.get(pPlayer + ";" + command) != null;
    }


    // ------------------------------------------------- INFORMATION SYSTEM

    // TPS
    public static Text getTPS(double currentTps) {
        TextColor colour;

        if (currentTps > 18) {
            colour = TextColors.GREEN;
        } else if (currentTps > 15) {
            colour = TextColors.YELLOW;
        } else {
            colour = TextColors.RED;
        }
        return Text.of(colour, tpsFormat.format(currentTps));
    }

    // Récupération user (off/one) depuis un uuid
    public static Optional<User> getPlayerFromUUID(UUID uuid) {
        Optional<User> user = null;
        try {
            UserStorageService userStorage = Sponge.getServiceManager().provide(UserStorageService.class).get();
            user = userStorage.get(uuid);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return user;
        }
        return user;
    }

    // Get charge of a player
    public static Integer firstTime(Player pPlayer, String messageErreur) {
        String CHECK = "SELECT `charges` FROM `players_info` WHERE `UUID_P` = ?";
        ResultSet result;
        Integer number;
        try {
            PreparedStatement check = plugin.database.prepare(CHECK);
            // UUID _P
            check.setString(1, pPlayer.getUniqueId().toString());
            // Requête
            ResultSet rselect = check.executeQuery();
            while (rselect.next()) {
                Utils.cm(rselect.toString());
                Utils.cm("Debug charge 1");
                number = rselect.getInt(1);
                return number;
            }
        } catch (Exception se) {
            se.printStackTrace();
            Utils.cm(messageErreur);
            return null;
        }
        return null;
    }

    // -------------------------------------------------  CHARGES SYSTEM

    // Get all trusted players of pPlayer's IsoWorld
    public static Integer getDimensionId(Player pPlayer, String messageErreur) {
        String CHECK = "SELECT `DIMENSION_ID` FROM `isoworlds` WHERE `UUID_W` = ? AND `SERVEUR_ID` = ?";
        String check_w;
        try {
            PreparedStatement check = plugin.database.prepare(CHECK);

            // UUID _W
            check_w = pPlayer.getUniqueId().toString() + "-IsoWorld";
            check.setString(1, check_w);
            // SERVEUR_ID
            check.setString(2, plugin.servername);
            // Requête
            ResultSet rselect = check.executeQuery();
            if (rselect.next()) {
                return rselect.getInt(1);
            }
        } catch (Exception se) {
            se.printStackTrace();
            Utils.cm(Msg.keys.SQL);
            pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder(messageErreur).color(TextColors.AQUA))).build()));
            return 0;
        }
        return 0;
    }

    // Get all isoworlds dimension id
    public static ArrayList getAllDimensionId(String messageErreur) {
        String CHECK = "SELECT `DIMENSION_ID` FROM `isoworlds` WHERE `SERVEUR_ID` = ? ORDER BY `DIMENSION_ID` DESC";
        String check_w;
        ArrayList<Integer> dimList = new ArrayList<Integer>();
        try {
            PreparedStatement check = plugin.database.prepare(CHECK);

            // SERVEUR_ID
            check.setString(1, plugin.servername);
            // Requête
            ResultSet rselect = check.executeQuery();
            while (rselect.next()) {
                dimList.add(rselect.getInt("DIMENSION_ID"));
            }
            return dimList;
        } catch (Exception se) {
            se.printStackTrace();
            Utils.cm(Msg.keys.SQL);
            return dimList;
        }
    }

    // set isoworld dimension ID
    public static Boolean setDimensionId(Player pPlayer, Integer number, String messageErreur) {
        String CHECK = "UPDATE `isoworlds` SET `DIMENSION_ID` = ? WHERE `UUID_W` = ?";
        try {
            PreparedStatement check = plugin.database.prepare(CHECK);

            // Number
            check.setInt(1, number);
            // UUID_P
            check.setString(2, pPlayer.getUniqueId().toString() + "-IsoWorld");
            // Requête
            Utils.cm("Debug 3: " + check.toString());
            check.executeUpdate();
            return true;
        } catch (Exception se) {
            se.printStackTrace();
            Utils.cm(messageErreur);
            return false;
        }
    }

    // get next dimensionID
    public static Integer getNextDimensionId() {
        // get all id
        ArrayList allId = Utils.getAllDimensionId(Msg.keys.SQL);

        for (int i = 1000; i < Integer.MAX_VALUE; i++) {
            if (!allId.contains(i)) {
                return i;
            }
        }
        return 0;
    }

    // Get charge of a player
    public static Integer getCharge(Player pPlayer, String messageErreur) {
        String CHECK = "SELECT `charges` FROM `players_info` WHERE `UUID_P` = ?";
        ResultSet result;
        Integer number;
        try {
            PreparedStatement check = plugin.database.prepare(CHECK);
            // UUID _P
            check.setString(1, pPlayer.getUniqueId().toString());
            // Requête
            ResultSet rselect = check.executeQuery();
            while (rselect.next()) {
                Utils.cm(rselect.toString());
                Utils.cm("Debug charge 1");
                number = rselect.getInt(1);
                return number;
            }
        } catch (Exception se) {
            se.printStackTrace();
            Utils.cm(messageErreur);
            return null;
        }
        initCharges(pPlayer, Msg.keys.SQL);
        return 0;
    }

    // Ajoute des charges à un joueur, succès = true
    public static Boolean updateCharge(Player pPlayer, Integer number, String messageErreur) {
        String CHECK = "UPDATE `players_info` SET `charges` = ? WHERE `UUID_P` = ?";
        try {
            PreparedStatement check = plugin.database.prepare(CHECK);

            // Number
            check.setInt(1, number);
            // UUID_P
            check.setString(2, pPlayer.getUniqueId().toString());
            // Requête
            Utils.cm("Debug 3: " + check.toString());
            check.executeUpdate();
            return true;
        } catch (Exception se) {
            se.printStackTrace();
            Utils.cm(messageErreur);
            return false;
        }
    }

    // Init charges and playtime on first connect
    public static Boolean initCharges(Player pPlayer, String messageErreur) {
        String INSERT = "INSERT INTO `players_info` (`UUID_P`, `charges`, `playtimes`) VALUES (?, ?, ?)";
        Integer number;
        String Iuuid_p;

        try {
            PreparedStatement insert = plugin.database.prepare(INSERT);
            // UUID_P
            Iuuid_p = pPlayer.getUniqueId().toString();
            insert.setString(1, Iuuid_p);
            // Number
            number = 0;
            insert.setInt(2, number);
            // PlayTime
            insert.setInt(3, number);
            insert.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.cm(Msg.keys.SQL);
            return false;
        }
        return true;
    }

    // Vérifie les charges, retire si en possède sinon return false avec message
    public static Integer checkCharge(Player pPlayer, String messageErreur) {
        Integer charges = Utils.getCharge(pPlayer, Msg.keys.SQL);
        Integer newCharges;

        if (charges == null) {
            initCharges(pPlayer, Msg.keys.SQL);
            return -1;
        }
        // Permissions unlimited for player
        if (pPlayer.hasPermission("isoworlds.unlimited.charges")) {
            return -99;
        }
        if (charges <= 0) {
            pPlayer.sendMessage(Text.of(Text.builder("[IsoWorlds]: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder("Sijania indique que vous ne possédez aucune charge !").color(TextColors.RED))).build()));
            return -1;
        }
        return charges;
    }

    // Ajoute une minute au compteur de temps du joueur
    public static Boolean updatePlayTime(Player pPlayer, String messageErreur) {
        String CHECK = "UPDATE `players_info` SET `playtimes` = `playtimes` + 1 WHERE `UUID_P` = ?";
        try {
            PreparedStatement check = plugin.database.prepare(CHECK);

            // UUID_P
            check.setString(1, pPlayer.getUniqueId().toString());
            // Requête
            check.executeUpdate();
            return true;
        } catch (Exception se) {
            se.printStackTrace();
            Utils.cm(messageErreur);
            return false;
        }
    }

    // Get charge of a player
    public static Integer getPlayTime(Player pPlayer, String messageErreur) {
        String CHECK = "SELECT `playtimes` FROM `players_info` WHERE `UUID_P` = ?";
        ResultSet result;
        Integer number;
        try {
            PreparedStatement check = plugin.database.prepare(CHECK);
            // UUID _P
            check.setString(1, pPlayer.getUniqueId().toString());
            // Requête
            ResultSet rselect = check.executeQuery();
            while (rselect.next()) {
                number = rselect.getInt(1);
                return number;
            }
        } catch (Exception se) {
            se.printStackTrace();
            Utils.cm(messageErreur);
            return null;
        }
        return 0;
    }

    // Executer une commande sur le serveur
    public void cmds(String cmd) {
        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmd);
    }
}