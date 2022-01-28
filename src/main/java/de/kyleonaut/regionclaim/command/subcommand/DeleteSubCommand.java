package de.kyleonaut.regionclaim.command.subcommand;

import de.kyleonaut.regionclaim.RegionClaimPlugin;
import de.kyleonaut.regionclaim.command.SubCommand;
import de.kyleonaut.regionclaim.entity.Region;
import de.kyleonaut.regionclaim.entity.RegionUser;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 27.01.2022
 */
public class DeleteSubCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("region.delete")) {
            player.sendMessage("§8[§6Region§8] §cDu hast keine Berechtigung diesen Befehl zu nutzen!");
            return;
        }
        final RegionClaimPlugin plugin = RegionClaimPlugin.getPlugin(RegionClaimPlugin.class);
        if (args.length == 2) {
            if (!player.hasPermission("region.delete.admin")) {
                player.sendMessage("§8[§6Region§8] §cDu hast keine Berechtigung diesen Befehl zu nutzen!");
                return;
            }
            try {
                long id = Long.parseLong(args[1]);
                final Region region = plugin.getRegionService().getRegion(id);
                if (region == null) {
                    player.sendMessage("§8[§6Region§8] §7Es konnte keine Region mit dieser Id gefunden werden.");
                    return;
                }
                plugin.getRegionService().remove(id);
                player.sendMessage("§8[§6Region§8] §7Die Region wurde erfolgreich gelöscht.");
                return;
            } catch (NumberFormatException e) {
                player.sendMessage("§8[§6Region§8] §7Die angegebene Id ist ungültig.");
            }
        }
        final Region region = plugin.getRegionService().getRegion(player.getLocation());
        if (region == null) {
            player.sendMessage("§8[§6Region§8] §7Du musst dich in einer Region befinden um diese zu löschen.");
            return;
        }
        final Optional<RegionUser> optionalRegionUser = region.getRegionUsers().stream().filter(regionUser -> regionUser.getUuid().equals(player.getUniqueId())).findFirst();
        if (optionalRegionUser.isEmpty() && !player.hasPermission("region.delete.admin")) {
            player.sendMessage("§8[§6Region§8] §7Du kannst nur deine eigene Region löschen.");
            return;
        }
        plugin.getRegionService().remove(region.getId());
        player.sendMessage("§8[§6Region§8] §7Die Region wurde erfolgreich gelöscht.");
    }
}
