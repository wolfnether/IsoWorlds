package bukkit.Listeners;

import bukkit.IworldsBukkit;
import bukkit.Utils.IworldsUtils;
import common.Msg;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import sponge.Locations.IworldsLocations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Edwin on 22/10/2017.
 */

public class IworldsListeners implements Listener {

    private final IworldsBukkit instance = IworldsBukkit.getInstance();

    @EventHandler
    public static void onRespawnPlayerEvent(PlayerRespawnEvent event) {

        Player p = event.getPlayer();
        String worldname = (p.getUniqueId() + "-iWorld");
        Integer maxy = Bukkit.getServer().getWorld(worldname).getHighestBlockYAt(0, 0);
        Location top = new Location(Bukkit.getServer().getWorld(worldname), 0, maxy, 0);

        p.teleport(top);
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerTeleportEvent event) {
        final String CHECK = "SELECT * FROM `autorisations` WHERE `UUID_P` = ? AND `UUID_W` = ?";
        String check_p;
        String check_w;

        Location worldTo = event.getTo();
        Location worldFrom = event.getFrom();
        Player pPlayer = event.getPlayer();

        if (worldTo.toString().equals(worldFrom.toString())) {
            return;
        }

        String eventworld = worldTo.getWorld().getName();

        if (eventworld == null) {
            Bukkit.getServer().createWorld(new WorldCreator(eventworld));
        }

        if (eventworld.contains("-iWorld")) {
            try {
                PreparedStatement check = instance.database.prepare(CHECK);
                // UUID_P
                check_p = pPlayer.getUniqueId().toString();
                check.setString(1, check_p);
                // UUID_W
                check_w = eventworld;
                check.setString(2, check_w);
                // Requête
                ResultSet rselect = check.executeQuery();
                if (pPlayer.hasPermission("iworlds.bypass.teleport")) {
                    return;
                }

                if (rselect.isBeforeFirst() ) {
                    pPlayer.sendMessage(ChatColor.GOLD + "[iWorlds]: " + ChatColor.AQUA + Msg.keys.SUCCES_TELEPORTATION);
                    return;
                    // Cas du untrust, pour ne pas rester bloquer
                } else if (pPlayer.getWorld().getName() == eventworld) {
                    pPlayer.sendMessage(ChatColor.GOLD + "[iWorlds]: " + ChatColor.AQUA +Msg.keys.SUCCES_RETIRER_CONFIANCE);
                    return;
                } else {
                    event.setCancelled(true);
                    pPlayer.sendMessage(ChatColor.GOLD + "[iWorlds]: " + ChatColor.AQUA + Msg.keys.DENY_TELEPORT);
                    return;
                }

            } catch (Exception se) {
                pPlayer.sendMessage(ChatColor.GOLD + "[iWorlds]: " + ChatColor.AQUA + Msg.keys.EXISTE_PAS_IWORLD);
            }

        }
    }
}