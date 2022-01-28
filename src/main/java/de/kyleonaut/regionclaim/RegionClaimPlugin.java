package de.kyleonaut.regionclaim;

import de.kyleonaut.regionclaim.command.RegionCommand;
import de.kyleonaut.regionclaim.listener.RegionCreateListener;
import de.kyleonaut.regionclaim.listener.RegionListener;
import de.kyleonaut.regionclaim.repository.RegionRepository;
import de.kyleonaut.regionclaim.service.RegionService;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 27.01.2022
 */
public class RegionClaimPlugin extends JavaPlugin {
    @Getter
    private RegionRepository regionRepository;
    @Getter
    private RegionService regionService;

    @Override
    public void onEnable() {
        init();
        Bukkit.getPluginManager().registerEvents(new RegionCreateListener(this.regionService), this);
        Bukkit.getPluginManager().registerEvents(new RegionListener(), this);
        Objects.requireNonNull(getCommand("region")).setExecutor(new RegionCommand());
    }

    public void init() {
        this.regionRepository = new RegionRepository(this);
        this.regionService = new RegionService(this.regionRepository);
        this.regionService.load();
    }
}
