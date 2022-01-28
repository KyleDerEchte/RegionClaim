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
import java.util.stream.Collectors;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 27.01.2022
 */
public class SetOwnerSubCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("region.setowner")) {
            player.sendMessage("§8[§6Region§8] §cDu hast keine Berechtigung diesen Befehl zu nutzen!");
            return;
        }
        if (args.length != 2) {
            player.sendMessage("§8§m---§r§8[§6Region§8]§m---");
            player.sendMessage("§e/region info §7- Zeigt dir Informationen über die Region an, in der du stehst.");
            player.sendMessage("§e/region trust <Spieler> §7- Fügt den Spieler als vertrauten Spieler hinzu.");
            player.sendMessage("§e/region untrust <Spieler> §7- Entfernt den Spieler als vertrauten Spieler.");
            player.sendMessage("§e/region setowner <Spieler> §7- Übertrage die Besitzrechte dieser Region auf einen anderen Spieler.");
            player.sendMessage("§e/region delete §7- Löscht die Region.");
            player.sendMessage("§e/region list §7- Liste alle Regions auf dem Server auf.");
            return;
        }
        final Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage("§8[§6Region§8] §7Der Spieler konnte nicht gefunden werden.");
            return;
        }
        final RegionClaimPlugin plugin = RegionClaimPlugin.getPlugin(RegionClaimPlugin.class);
        final Region region = plugin.getRegionService().getRegion(player.getLocation());
        if (region == null) {
            player.sendMessage("§8[§6Region§8] §7Du musst in einer Region sein um die Besitzrechte zu verändern.");
            return;
        }
        final List<RegionUser> regionUsers = new ArrayList<>(region.getRegionUsers());
        final Optional<RegionUser> optionalRegionUser = regionUsers.stream().filter(regionUser -> regionUser.getUuid().equals(player.getUniqueId())).findFirst();
        if (optionalRegionUser.isEmpty() && !player.hasPermission("region.setowner.admin")) {
            player.sendMessage("§8[§6Region§8] §7Du kannst die Besitzrechte nur für deine eigene Region ändern.");
            return;
        }
        final List<RegionUser> owners = regionUsers.stream().filter(regionUser -> regionUser.getRegionRole().equals(RegionRole.OWNER)).collect(Collectors.toList());
        owners.forEach(regionUsers::remove);
        regionUsers.add(new RegionUser(target.getName(), target.getUniqueId(), RegionRole.OWNER));
        region.setRegionUsers(regionUsers);
        plugin.getRegionService().update();
        player.sendMessage("§8[§6Region§8] §7Die Besitzrechte wurden erfolgreich geändert.");
    }
}
