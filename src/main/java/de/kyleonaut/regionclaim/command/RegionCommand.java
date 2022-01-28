package de.kyleonaut.regionclaim.command;

import de.kyleonaut.regionclaim.command.subcommand.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 27.01.2022
 */
public class RegionCommand implements CommandExecutor {
    private final Map<String, SubCommand> commandMap;

    public RegionCommand() {
        commandMap = new HashMap<>();
        commandMap.put("info", new InfoSubCommand());
        commandMap.put("i", new InfoSubCommand());
        commandMap.put("trust", new TrustSubCommand());
        commandMap.put("t", new TrustSubCommand());
        commandMap.put("untrust", new UnTrustSubCommand());
        commandMap.put("ut", new UnTrustSubCommand());
        commandMap.put("setowner", new SetOwnerSubCommand());
        commandMap.put("so", new SetOwnerSubCommand());
        commandMap.put("delete", new DeleteSubCommand());
        commandMap.put("d", new DeleteSubCommand());
        commandMap.put("list", new ListSubCommand());
        commandMap.put("l", new ListSubCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }
        if (!player.hasPermission("region.command")) {
            player.sendMessage("§8[§6Region§8] §cDu hast keine Berechtigung diesen Befehl zu nutzen!");
            return false;
        }
        if (args.length == 0) {
            player.sendMessage("§8§m---§r§8[§6Region§8]§m---");
            player.sendMessage("§e/region info");
            player.sendMessage("§e/region trust <Spieler>");
            player.sendMessage("§e/region untrust <Spieler>");
            player.sendMessage("§e/region setowner <Spieler>");
            player.sendMessage("§e/region delete");
            player.sendMessage("§e/region list");
            return true;
        }
        for (String s : commandMap.keySet()) {
            if (args[0].equalsIgnoreCase(s)) {
                commandMap.get(s.toLowerCase()).execute(player, args);
                return true;
            }
        }
        player.sendMessage("§8§m---§r§8[§6Region§8]§m---");
        player.sendMessage("§e/region info §7- Zeigt dir Informationen über die Region an, in der du stehst.");
        player.sendMessage("§e/region trust <Spieler> §7- Fügt den Spieler als vertrauten Spieler hinzu.");
        player.sendMessage("§e/region untrust <Spieler> §7- Entfernt den Spieler als vertrauten Spieler.");
        player.sendMessage("§e/region setowner <Spieler> §7- Übertrage die Besitzrechte dieser Region.");
        player.sendMessage("§e/region delete §7- Löscht die Region.");
        player.sendMessage("§e/region list §7- Liste alle Regionen auf dem Server auf.");
        return false;
    }
}
