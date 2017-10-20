package isolonice.iworlds.Commandes.SousCommandes;

import isolonice.iworlds.IworldsSponge;
import isolonice.iworlds.Locations.IworldsLocations;
import isolonice.iworlds.Utils.IworldsUtils;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Edwin on 10/10/2017.
 */
public class MaisonCommande implements CommandExecutor {

    private final IworldsSponge plugin = IworldsSponge.instance;

    @Override
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException {

        // Variables
        String worldname = "";
        Player pPlayer = (Player) source;
        final String check_p;
        final String check_w;
        final String CHECK = "SELECT * FROM `iworlds` WHERE `UUID_P` = ? AND `UUID_W` = ?";

        worldname = (IworldsUtils.PlayerToUUID(pPlayer) + "-iWorld");

        try {
            PreparedStatement check = plugin.database.prepare(CHECK);

            // UUID _P
            check_p = IworldsUtils.PlayerToUUID(pPlayer).toString();
            check.setString(1, check_p);
            // UUID_W
            check_w = (IworldsUtils.PlayerToUUID(pPlayer) + "-iWorld");
            check.setString(2, check_w);

            IworldsUtils.cm("CHECK REQUEST: " + check);
            // Requête
            ResultSet rselect = check.executeQuery();
            if (rselect.isBeforeFirst() ) {
                Sponge.getServer().loadWorld(worldname);
            }
        } catch (Exception se){
            pPlayer.sendMessage(Text.of(Text.builder("[iWorlds]: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder("CHECK Sijania indique que vous ne possédez aucun iWorld.").color(TextColors.AQUA))).build()));
            return CommandResult.success();
        }

        // Construction du point de respawn
        Location<World> spawn = plugin.getGame().getServer().getWorld(worldname).get().getSpawnLocation();
        Location<World> maxy = new Location<>(spawn.getExtent(), 0, 0, 0);
        Location<World> top = IworldsLocations.getHighestLoc(maxy).orElse(null);

        // Téléportation du joueur
        if (pPlayer.setLocationSafely(top)) {
            pPlayer.sendMessage(Text.of(Text.builder("[iWorlds]: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder("Bon retour à vous, " + pPlayer.getName()).color(TextColors.AQUA))).build()));
        } else {
            pPlayer.sendMessage(Text.of(Text.builder("[iWorlds]: ").color(TextColors.GOLD)
                    .append(Text.of(Text.builder("Sijania ne parvient pas à vous téléporter, veuillez contacter un membre de l'équipe Isolonice.").color(TextColors.AQUA))).build()));
        }

        return CommandResult.success();
    }


    // Constructeurs
    public static CommandSpec getCommand() {
        return CommandSpec.builder()
                .description(Text.of("Commande pour retourner dans son iWorld"))
                .permission("iworlds.maison")
                .executor(new MaisonCommande())
                .build();
    }
}