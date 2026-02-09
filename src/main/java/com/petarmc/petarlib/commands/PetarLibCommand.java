package com.petarmc.petarlib.commands;

import com.petarmc.petarlib.PetarLib;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Locale;

import static com.petarmc.petarlib.PetarLib.getPlugin;


public class PetarLibCommand implements CommandExecutor {
    @Override
    public boolean onCommand( CommandSender sender,  Command command,  String label,  String[] args) {
        String cmd = command.getName();
        if (!cmd.equalsIgnoreCase("petarlib")) return false;

        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "help":
                showHelp(sender);
                break;
            case "info":
                sender.sendMessage(ChatColor.AQUA + "Plugin: " + ChatColor.WHITE + getPlugin().getDescription().getName());
                sender.sendMessage(ChatColor.AQUA + "Author(s): " + ChatColor.WHITE + String.join(", ", getPlugin().getDescription().getAuthors()));
                sender.sendMessage(ChatColor.AQUA + "Version: " + ChatColor.WHITE + getPlugin().getDescription().getVersion());
                break;
            case "version":
                sender.sendMessage(ChatColor.GREEN + "PetarLib version v" + getPlugin().getDescription().getVersion());
                break;
            case "reload":
                if (!sender.hasPermission("petarlib.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    return true;
                }
                getPlugin().reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "PetarLib configuration reloaded.");
                break;
            case "debug":
                if (!sender.hasPermission("petarlib.admin")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                    return true;
                }
                boolean currentDebug = getPlugin().getConfig().getBoolean("debug");
                if (!currentDebug) {
                    getPlugin().getConfig().set("debug", true);
                    sender.sendMessage(ChatColor.GREEN + "Debug mode set to" + ChatColor.GREEN + " true");
                } else {
                    getPlugin().getConfig().set("debug", false);
                    sender.sendMessage(ChatColor.GREEN + "Debug mode set to" + ChatColor.RED + " false");
                }
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand. Use /petarlib help");
                break;
        }

        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "--- PetarLib Help ---");
        sender.sendMessage(ChatColor.YELLOW + "/petarlib help" + ChatColor.WHITE + " - Show this help message");
        sender.sendMessage(ChatColor.YELLOW + "/petarlib info" + ChatColor.WHITE + " - Show plugin information");
        sender.sendMessage(ChatColor.YELLOW + "/petarlib reload" + ChatColor.WHITE + " - Reload plugin config (requires permission)");
        sender.sendMessage(ChatColor.YELLOW + "/petarlib debug" + ChatColor.WHITE + " - Toggle debug mode (requires permission)");
    }
}
