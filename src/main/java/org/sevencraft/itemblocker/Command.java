package org.sevencraft.itemblocker;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if(!commandSender.hasPermission("itemblocker.reload")) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getConfiguration().getString("no-permission")));
            return false;
        }
        Main.reloadConfiguration();
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getConfiguration().getString("reload-message")));
        return true;
    }

}