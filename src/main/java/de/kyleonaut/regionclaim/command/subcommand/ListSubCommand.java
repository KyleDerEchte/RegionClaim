package de.kyleonaut.regionclaim.command.subcommand;

import de.kyleonaut.regionclaim.RegionClaimPlugin;
import de.kyleonaut.regionclaim.command.SubCommand;
import de.kyleonaut.regionclaim.entity.Region;
import de.kyleonaut.regionclaim.entity.RegionRole;
import de.kyleonaut.regionclaim.entity.RegionUser;
import de.kyleonaut.regionclaim.service.RegionService;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 28.01.2022
 */
public class ListSubCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("region.list")) {
            player.sendMessage("§8[§6Region§8] §cDu hast keine Berechtigung diesen Befehl zu nutzen!");
            return;
        }
        final RegionService regionService = RegionClaimPlugin.getPlugin(RegionClaimPlugin.class).getRegionService();
        final List<Region> regions = regionService.list();
        if (regions.size() == 0) {
            player.sendMessage("§8[§6Region§8] §7Auf dem Server wurden noch keine Regionen erstellt.");
            return;
        }
        player.sendMessage("§8§m---§r§8[§6Regionen§8]§m---");
        for (Region region : regions) {
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
                if (trusts.lastIndexOf(trust) == trusts.size() - 1) {
                    trustsBuilder.append("§e").append(trust.getName());
                    break;
                }
                trustsBuilder.append("§e").append(trust.getName()).append("§7, ");
            }
            player.sendMessage("§7Regions-Id:§e " + region.getId());
            player.sendMessage("§7Regionssbesitzer:§e " + ownersBuilder);
            player.sendMessage("§7Vertraute Spieler:§e " + trustsBuilder);
            player.sendMessage("§7Regionseckpunkt 1:§e " + region.getFirst().toString());
            player.sendMessage("§7Regionseckpunkt 2:§e " + region.getSecond().toString());
            player.sendMessage("    ");
        }
    }
}
