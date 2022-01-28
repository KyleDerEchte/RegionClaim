package de.kyleonaut.regionclaim.command;

import org.bukkit.entity.Player;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 27.01.2022
 */
public interface SubCommand {
    void execute(Player player, String[] args);
}
