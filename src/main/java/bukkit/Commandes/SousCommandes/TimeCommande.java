package bukkit.Commandes.SousCommandes;

import bukkit.IsoworldsBukkit;

import common.Msg;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import bukkit.Utils.IsoworldsUtils;

/**
 * Created by Edwin on 24/10/2017.
 */
public class TimeCommande {

    public static IsoworldsBukkit instance;

    public static void Time(CommandSender sender, String[] args) {

        instance = IsoworldsBukkit.getInstance();
        instance = IsoworldsBukkit.getInstance();
        int num;
        Player pPlayer = (Player) sender;
        Integer len = args.length;

        if (!IsoworldsUtils.iworldExists(pPlayer.getUniqueId().toString(), Msg.keys.SQL)) {
            pPlayer.sendMessage(ChatColor.GOLD + "[IsoWorlds]" + ChatColor.AQUA + Msg.keys.EXISTE_PAS_IWORLD);
            return;
        }

        if (len < 2) {
            pPlayer.sendMessage(ChatColor.GOLD + "--------------------- [ " + ChatColor.AQUA + "IsoWorlds " + ChatColor.GOLD + "] ---------------------");
            pPlayer.sendMessage(" ");
            pPlayer.sendMessage(ChatColor.AQUA + "Sijania vous propose deux temps:");
            pPlayer.sendMessage(ChatColor.GOLD + "- Jour: " + ChatColor.AQUA + "/iw time " + ChatColor.GOLD + "[" + ChatColor.GREEN + "jour"
                    + ChatColor.GOLD + "/" + ChatColor.GREEN + "nuit" + ChatColor.GOLD + "]");
            pPlayer.sendMessage(" ");
            return;
        } else {
            try{
                num = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                pPlayer.sendMessage(ChatColor.AQUA + "Sijania indique que vous n'avez pas renseigné de minutes.");
                return;
            }
            World weather = Bukkit.getServer().getWorld(pPlayer.getUniqueId().toString() + "-IsoWorld");
            IsoworldsUtils.cm("Time world: " + weather.getName());
            if (args[1].equals("jour") || args[1].equals("day")) {
                weather.setTime(0);
                pPlayer.sendMessage(ChatColor.AQUA + "Sijania vient de changer le temps de votre IsoWorld.");
                return;
            } else if (args[1].equals("nuit") || args[1].equals("night")) {
                weather.setTime(12000);
                pPlayer.sendMessage(ChatColor.AQUA + "Sijania vient de changer la météo de votre IsoWorld.");
                return;
            }
            return;
        }
    }
}
