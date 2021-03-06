package de.kyleonaut.regionclaim.command.subcommand;

import de.kyleonaut.regionclaim.RegionClaimPlugin;
import de.kyleonaut.regionclaim.command.SubCommand;
import de.kyleonaut.regionclaim.entity.Region;
import de.kyleonaut.regionclaim.entity.RegionRole;
import de.kyleonaut.regionclaim.entity.RegionUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 27.01.2022
 */
public class TrustSubCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("region.trust")) {
            player.sendMessage("§8[§6Region§8] §cDu hast keine Berechtigung diesen Befehl zu nutzen!");
            return;
        }
        final RegionClaimPlugin plugin = RegionClaimPlugin.getPlugin(RegionClaimPlugin.class);
        final Region region = plugin.getRegionService().getRegion(player.getLocation());
        if (args.length != 2) {
            player.sendMessage("§8§m---§r§8[§6Region§8]§m---");
            player.sendMessage("§e/region info");
            player.sendMessage("§e/region trust <Spieler>");
            player.sendMessage("§e/region untrust <Spieler>");
            player.sendMessage("§e/region setowner <Spieler>");
            player.sendMessage("§e/region delete");
            player.sendMessage("§e/region list");
            return;
        }
        final Player target = Bukkit.getPlayer(args[1]);
        if (player == target) {
            player.sendMessage("§8[§6Region§8] §7Du darfst dich nicht selbst als vertrauten Spieler hinzufügen.");
            return;
        }
        if (target == null) {
            player.sendMessage("§8[§6Region§8] §7Der Spieler konnte nicht gefunden werden.");
            return;
        }
        if (region == null) {
            player.sendMessage("§8[§6Region§8] §7Du musst in einer Region sein um einen Spieler zu den vertrauten Spielern hinzuzufügen.");
            return;
        }
        final List<RegionUser> regionUsers = new ArrayList<>(region.getRegionUsers());
        final Optional<RegionUser> optionalRegionUser = regionUsers.stream()
                .filter(regionUser -> regionUser.getUuid().equals(player.getUniqueId()))
                .filter(regionUser -> regionUser.getRegionRole().equals(RegionRole.OWNER))
                .findFirst();
        if (optionalRegionUser.isEmpty() && !player.hasPermission("region.trust.admin")) {
            player.sendMessage("§8[§6Region§8] §7Du kannst Spieler nur in deiner eigenen Region zu den vertrauten Spielern hinzufügen.");
            return;
        }
        regionUsers.add(new RegionUser(target.getName(), target.getUniqueId(), RegionRole.TRUSTED));
        region.setRegionUsers(regionUsers);
        plugin.getRegionService().update();
        player.sendMessage("§8[§6Region§8] §7Der Spieler wurde erfolgreich zu den vertrauten Spielern hinzugefügt.");
    }
}
