package de.kyleonaut.regionclaim.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Objects;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 27.01.2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coordinate {
    private double x;
    private double y;
    private double z;
    private String worldName;

    public static Coordinate of(Location location) {
        return new Coordinate(location.getX(), location.getY(), location.getZ(), Objects.requireNonNull(location.getWorld()).getName());
    }

    public Location toBukkitLocation() {
        return new Location(Bukkit.getWorld(this.worldName), x, y, z);
    }

    public String toString() {
        return "X: " + x + ", Y: " + y + ", Z: " + z;
    }
}
