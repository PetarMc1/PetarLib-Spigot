package com.petarmc.petarlib.commands;

import com.petarmc.petarlib.Config;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
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

    private String stripMiniMessageTags(String message) {
        return message.replaceAll("<[^>]+>", "");
    }

    private void sendMessage(CommandSender sender, String message) {
        if (Config.useMiniMessage()) {
            sender.sendMessage(mm.deserialize(message));
        } else {
            sender.sendMessage(stripMiniMessageTags(message));
        }
    }

    private void petarLibSendCmd(String text, Player player, Type type) {
        String processed = text;
        if (Config.useMiniMessage()) {
            switch (type) {
                case ACTIONBAR:
                    player.sendActionBar(mm.deserialize(processed));
                    break;
                case CHAT:
                    player.sendMessage(mm.deserialize(processed));
                    break;
            }
        } else {
            processed = stripMiniMessageTags(text);
            switch (type) {
                case ACTIONBAR:
                    player.sendActionBar(processed);
                    break;
                case CHAT:
                    player.sendMessage(processed);
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
                sender.sendMessage(mm.deserialize("<aqua>Plugin: <white>" + getPlugin().getDescription().getName()));
                sender.sendMessage(mm.deserialize("<aqua>Author(s): <white>" + String.join(", ", getPlugin().getDescription().getAuthors())));
                sender.sendMessage(mm.deserialize("<aqua>Version: <white>" + getPlugin().getDescription().getVersion()));
                break;
            case "version":
                sendMessage(sender, Config.getMessage("info-version"));
                break;
            case "reload":
                if (!sender.hasPermission("petarlib.admin")) {
                    sendMessage(sender, Config.getMessage("no-permission"));
                    if (Config.debugMode) {
                        getPlugin().getLogger().info("User" + sender + " attempted to a  command without permission.");
                    }
                    return true;
                }
                getPlugin().reloadConfig();
                Config.reloadMessages();
                sendMessage(sender, Config.getMessage("reload"));
                break;
            case "send":
                if (!sender.hasPermission("petarlib.send")) {
                    sendMessage(sender, Config.getMessage("no-permission"));
                    if (Config.debugMode) {
                        getPlugin().getLogger().info("User" + sender + " attempted to a  command without permission.");
                    }
                    return true;
                }
                if (args.length < 4) {
                    sendMessage(sender, Config.getMessage("usage-send"));
                    return true;
                }
                Type type = args[1].equalsIgnoreCase("actionbar") ? Type.ACTIONBAR : args[1].equalsIgnoreCase("chat") ? Type.CHAT : args[1].equalsIgnoreCase("title") ? Type.TITLE : null;
                Player target = getPlugin().getServer().getPlayer(args[2]);
                if (type == null) {
                    sendMessage(sender, Config.getMessage("invalid-type"));
                    return true;
                }
                if (target == null) {
                    sendMessage(sender, Config.getMessage("player-not-found"));
                    return true;
                }
                String message = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                petarLibSendCmd(message, target, type);
                break;
            case "debug":
                if (!sender.hasPermission("petarlib.admin")) {
                    sendMessage(sender, Config.getMessage("no-permission"));
                    if (Config.debugMode) {
                        getPlugin().getLogger().info("User" + sender + " attempted to a  command without permission.");
                    }
                    return true;
                }
                boolean currentDebug = getPlugin().getConfig().getBoolean("debug");
                if (!currentDebug) {
                    getPlugin().getConfig().set("debug", true);
                    sendMessage(sender, Config.getMessage("debug-enabled"));
                } else {
                    getPlugin().getConfig().set("debug", false);
                    sendMessage(sender, Config.getMessage("debug-disabled"));
                }
                break;
            default:
                sendMessage(sender, Config.getMessage("unknown-subcommand"));
                break;
        }
        return true;
    }

    private void showHelp(CommandSender sender) {
            sender.sendMessage(mm.deserialize("<gold>--- PetarLib Help ---"));
            sender.sendMessage(mm.deserialize("<yellow>/petarlib help<white> - Show this help message"));
            sender.sendMessage(mm.deserialize("<yellow>/petarlib info<white> - Show plugin information"));
            sender.sendMessage(mm.deserialize("<yellow>/petarlib version<white> - Show plugin version"));
            sender.sendMessage(mm.deserialize("<yellow>/petarlib send <type> <player> <message><white> - Send a message to a player. Type can be 'chat' or 'actionbar'"));
            sender.sendMessage(mm.deserialize("<yellow>/petarlib reload<white> - Reload plugin config"));
            sender.sendMessage(mm.deserialize("<yellow>/petarlib debug<white> - Toggle debug mode"));
    }
}
