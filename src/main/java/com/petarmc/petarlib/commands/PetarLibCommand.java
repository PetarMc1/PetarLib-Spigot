package com.petarmc.petarlib.commands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

import static com.petarmc.petarlib.PetarLib.getPlugin;


public class PetarLibCommand implements CommandExecutor {
    // notifications function that is used by the /petarlib send <player> <message> command.
    MiniMessage mm = MiniMessage.miniMessage();
    private enum Type {
        ACTIONBAR,
        CHAT,
        TITLE
    }
    private void petarLibSendCmd(String text, Player player, Type type) {
        if (getPlugin().getConfig().getBoolean("use-minimessage")){
            switch (type) {
                case ACTIONBAR:
                    player.sendActionBar(mm.deserialize(text));
                    break;
                case CHAT:
                    player.sendMessage(mm.deserialize(text));
                    break;
            }
        } else {
            switch (type) {
                case ACTIONBAR:
                    player.sendActionBar(text);
                    break;
                case CHAT:
                    player.sendMessage(text);
                    break;
            }
        }

    }
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
                    sender.sendMessage("You don't have permission to use this command.");
                    return true;
                }
                getPlugin().reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "PetarLib configuration reloaded.");
                break;
            case "send":
                if (args.length < 4) {
                    sender.sendMessage("Usage: /petarlib send <player> <message> <type>");
                    return true;
                }
                if (!sender.hasPermission("petarlib.send")) {
                    sender.sendMessage("You don't have permission to use this command.");
                    return true;
                }
                Player target = getPlugin().getServer().getPlayer(args[1]);
                Type type = args[3].equalsIgnoreCase("actionbar") ? Type.ACTIONBAR : args[3].equalsIgnoreCase("chat") ? Type.CHAT : args[3].equalsIgnoreCase("title") ? Type.TITLE : null;
                if (target == null) {
                    sender.sendMessage("Player not found.");
                    return true;
                }
                if (type == null) {
                    sender.sendMessage("Invalid type. Use 'actionbar' or 'chat'.");
                    return true;
                }
                String message = String.join(" ", args).substring(args[0].length() + args[1].length() + 2);
                petarLibSendCmd(message, target, type);
                break;
            case "debug":
                if (!sender.hasPermission("petarlib.admin")) {
                    sender.sendMessage("You don't have permission to use this command.");
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
                sender.sendMessage("Unknown subcommand. Use /petarlib help");
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
