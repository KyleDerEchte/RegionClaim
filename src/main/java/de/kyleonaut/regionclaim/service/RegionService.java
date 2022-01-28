package de.kyleonaut.regionclaim.service;

import de.kyleonaut.regionclaim.entity.Region;
import de.kyleonaut.regionclaim.entity.RegionRole;
import de.kyleonaut.regionclaim.entity.RegionUser;
import de.kyleonaut.regionclaim.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 27.01.2022
 */
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;
    private final List<Region> regions = new ArrayList<>();

    private double min(double a, double b) {
        return Math.min(a, b);
    }

    private double max(double a, double b) {
        return Math.max(a, b);
    }

    public void load() {
        this.regions.addAll(this.regionRepository.load());
    }

    public List<Region> list() {
        return new ArrayList<>(this.regions);
    }

    public void update() {
        this.regionRepository.save(this.regions);
    }

    public void save(Region region) {
        this.regions.add(region);
        update();
    }

    public void remove(long id) {
        this.regions.stream().filter(region -> region.getId() == id).findFirst().ifPresent(this.regions::remove);
        update();
    }

    public Region getRegion(Location location) {
        for (Region region : regions) {
            final Location minLocation = new Location(
                    region.getFirst().toBukkitLocation().getWorld(),
                    min(region.getFirst().getX(), region.getSecond().getX()),
                    min(region.getFirst().getY(), region.getSecond().getY()),
                    min(region.getFirst().getZ(), region.getSecond().getZ()));
            final Location maxLocation = new Location(region.getFirst().toBukkitLocation().getWorld(),
                    max(region.getFirst().getX(), region.getSecond().getX()),
                    max(region.getFirst().getY(), region.getSecond().getY()),
                    max(region.getFirst().getZ(), region.getSecond().getZ()));
            if (minLocation.getX() <= location.getX() && minLocation.getY() <= location.getY() && minLocation.getZ() <= location.getZ() && maxLocation.getX() >= location.getX() && maxLocation.getY() >= location.getY() && maxLocation.getZ() >= location.getZ()) {
                return region;
            }
        }
        return null;
    }

    public long increment() {
        long highest = 0;
        for (Region region : this.regions) {
            if (region.getId() >= highest) {
                highest = region.getId() + 1;
            }
        }
        return highest;
    }

    public Region getRegion(long id) {
        final Optional<Region> optionalRegion = this.regions.stream()
                .filter(region -> region.getId() == id)
                .findFirst();
        return optionalRegion.orElse(null);
    }

    public boolean isForbiddenToPerform(Player player, Location location) {
        final Region region = getRegion(location);
        if (region == null) {
            return false;
        }
        final Optional<RegionUser> optionalRegionUser = region.getRegionUsers().stream()
                .filter(regionUser -> regionUser.getRegionRole().equals(RegionRole.OWNER) || regionUser.getRegionRole().equals(RegionRole.TRUSTED))
                .filter(regionUser -> regionUser.getUuid().equals(player.getUniqueId()))
                .findFirst();
        return optionalRegionUser.isEmpty();
    }
}
