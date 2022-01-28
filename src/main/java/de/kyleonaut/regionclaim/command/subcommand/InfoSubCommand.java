package de.kyleonaut.regionclaim.command.subcommand;

import de.kyleonaut.regionclaim.RegionClaimPlugin;
import de.kyleonaut.regionclaim.command.SubCommand;
import de.kyleonaut.regionclaim.entity.Region;
import de.kyleonaut.regionclaim.entity.RegionRole;
import de.kyleonaut.regionclaim.entity.RegionUser;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 27.01.2022
 */
public class InfoSubCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("region.info")) {
            player.sendMessage("§8[§6Region§8] §cDu hast keine Berechtigung diesen Befehl zu nutzen!");
            return;
        }
        final Region region = RegionClaimPlugin.getPlugin(RegionClaimPlugin.class).getRegionService().getRegion(player.getLocation());
        if (region == null) {
            player.sendMessage("§8[§6Region§8] §7Du befindest dich in keiner Region.");
            return;
        }
        final List<RegionUser> owners = region.getRegionUsers().stream().filter(regionUser -> regionUser.getRegionRole().equals(RegionRole.OWNER)).collect(Collectors.toList());
        final List<RegionUser> trusts = region.getRegionUsers().stream().filter(regionUser -> regionUser.getRegionRole().equals(RegionRole.TRUSTED)).collect(Collectors.toList());
        final StringBuilder ownersBuilder = new StringBuilder();
        for (RegionUser owner : owners) {
            if (owners.lastIndexOf(owner) == owners.size() - 1) {
                ownersBuilder.append("§e").append(owner.getName());
                break;
            }
            ownersBuilder.append("§e").append(owner.getName()).append("§7, ");
        }
        final StringBuilder trustsBuilder = new StringBuilder();
        for (RegionUser trust : trusts) {
            if (owners.lastIndexOf(trust) == owners.size() - 1) {
                trustsBuilder.append("§e").append(trust.getName());
                break;
            }
            trustsBuilder.append("§e").append(trust.getName()).append("§7, ");
        }
        player.sendMessage("§8§m---§r§8[§6Regions§7-§6Info§8]§m---");
        player.sendMessage("§7Regions-Id:§e " + region.getId());
        player.sendMessage("§7Regionssbesitzer:§e " + ownersBuilder);
        player.sendMessage("§7Vertraute Spieler:§e " + trustsBuilder);
        player.sendMessage("§8§m---§r§8[§6Regions§7-§6Info§8]§m---");
    }
}
