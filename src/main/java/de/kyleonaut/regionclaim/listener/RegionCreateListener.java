package de.kyleonaut.regionclaim.listener;

import de.kyleonaut.regionclaim.entity.Coordinate;
import de.kyleonaut.regionclaim.entity.Region;
import de.kyleonaut.regionclaim.entity.RegionRole;
import de.kyleonaut.regionclaim.entity.RegionUser;
import de.kyleonaut.regionclaim.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 27.01.2022
 */
@RequiredArgsConstructor
public class RegionCreateListener implements Listener {
    private final RegionService regionService;
    private final Map<UUID, Coordinate> coords = new HashMap<>();

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        event.getPlayer().getInventory().getItemInMainHand();
        if (!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.WOODEN_HOE)) {
            return;
        }
        final Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE) || player.hasPermission("region.create")) {
            event.setCancelled(true);
            if (regionService.getRegion(event.getClickedBlock().getLocation()) != null) {
                player.sendMessage("§8[§6Region§8] §7Der Block ist bereits teil einer Region.");
                return;
            }
            if (!coords.containsKey(player.getUniqueId())) {
                coords.put(player.getUniqueId(), Coordinate.of(event.getClickedBlock().getLocation()));
                player.sendMessage("§8[§6Region§8] §7Der markierte Block wurde als Startpunkt deiner neuen Region gesetzt.");
                return;
            }
            final Coordinate first = coords.get(player.getUniqueId());
            final Coordinate second = Coordinate.of(event.getClickedBlock().getLocation());
            final Region region = new Region();
            region.setFirst(first);
            region.setSecond(second);
            region.setId(regionService.increment());
            region.setRegionUsers(List.of(RegionUser.of(player, RegionRole.OWNER)));
            regionService.save(region);
            coords.remove(player.getUniqueId());
            player.sendMessage("§8[§6Region§8] §7Die Region wurde erfolgreich erstellt.");
        }
    }
}
