package de.kyleonaut.regionclaim.listener;

import de.kyleonaut.regionclaim.RegionClaimPlugin;
import de.kyleonaut.regionclaim.service.RegionService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 28.01.2022
 */
public class RegionListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final RegionService regionService = RegionClaimPlugin.getPlugin(RegionClaimPlugin.class).getRegionService();
        if (event.getAction().equals(Action.PHYSICAL) || event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_AIR)) {
            if (regionService.isForbiddenToPerform(player, player.getLocation())) {
                event.setCancelled(true);
                player.sendMessage("§8[§6Region§8] §7Du bist nicht als vertrauter Spieler eingetragen.");
                player.sendMessage("§8[§6Region§8] §7Du darfst diese Aktion nicht ausführen.");
            }
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        if (regionService.isForbiddenToPerform(player, event.getClickedBlock().getLocation())) {
            event.setCancelled(true);
            player.sendMessage("§8[§6Region§8] §7Du bist nicht als vertrauter Spieler eingetragen.");
            player.sendMessage("§8[§6Region§8] §7Du darfst diese Aktion nicht ausführen.");
        }
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }
        final RegionService regionService = RegionClaimPlugin.getPlugin(RegionClaimPlugin.class).getRegionService();
        if (regionService.isForbiddenToPerform(player, event.getEntity().getLocation())) {
            player.sendMessage("§8[§6Region§8] §7Du bist nicht als vertrauter Spieler eingetragen.");
            player.sendMessage("§8[§6Region§8] §7Du darfst diese Aktion nicht ausführen.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        final RegionService regionService = RegionClaimPlugin.getPlugin(RegionClaimPlugin.class).getRegionService();
        if (regionService.isForbiddenToPerform(event.getPlayer(), event.getBlockPlaced().getLocation())) {
            event.getPlayer().sendMessage("§8[§6Region§8] §7Du bist nicht als vertrauter Spieler eingetragen.");
            event.getPlayer().sendMessage("§8[§6Region§8] §7Du darfst diese Aktion nicht ausführen.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        final RegionService regionService = RegionClaimPlugin.getPlugin(RegionClaimPlugin.class).getRegionService();
        if (regionService.isForbiddenToPerform(event.getPlayer(), event.getBlock().getLocation())) {
            event.getPlayer().sendMessage("§8[§6Region§8] §7Du bist nicht als vertrauter Spieler eingetragen.");
            event.getPlayer().sendMessage("§8[§6Region§8] §7Du darfst diese Aktion nicht ausführen.");
            event.setCancelled(true);
        }
    }
}
