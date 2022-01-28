package de.kyleonaut.regionclaim.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 27.01.2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionUser {
    private String name;
    private UUID uuid;
    private RegionRole regionRole;

    public static RegionUser of(Player player, RegionRole regionRole) {
        return new RegionUser(player.getName(), player.getUniqueId(), regionRole);
    }
}
