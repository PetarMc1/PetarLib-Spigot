package com.petarmc.petarlib.commands;

import com.petarmc.petarlib.Config;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.Component;
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
            if (getPlugin().hasAdventure()) {
                getPlugin().adventure().sender(sender).sendMessage(mm.deserialize(message));
            } else {
                sender.sendMessage(stripMiniMessageTags(message));
            }
        } else {
            sender.sendMessage(stripMiniMessageTags(message));
        }
    }

    private void petarLibSendCmd(String text, Player player, Type type) {
        String processed = text;
        if (Config.useMiniMessage()) {
            Component comp = mm.deserialize(processed);
            if (getPlugin().hasAdventure()) {
                switch (type) {
                    case ACTIONBAR:
                        getPlugin().adventure().player(player).sendActionBar(comp);
                        break;
                    case CHAT:
                        getPlugin().adventure().player(player).sendMessage(comp);
                        break;
                }
            } else {
                switch (type) {
                    case ACTIONBAR:
                        getPlugin().getLogger().info(Config.debugMode ? "Using non Adventure actionbar message" : null);
                        player.sendActionBar(stripMiniMessageTags(processed));
                        break;
                    case CHAT:
                        getPlugin().getLogger().info(Config.debugMode ? "Using non Adventure chat message" : null);
                        player.sendMessage(stripMiniMessageTags(processed));
                        break;
                }
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
                sendMessage(sender, "<aqua>Plugin: <white>" + getPlugin().getDescription().getName());
                sendMessage(sender, "<aqua>Author(s): <white>" + String.join(", ", getPlugin().getDescription().getAuthors()));
                sendMessage(sender, "<aqua>Version: <white>" + getPlugin().getDescription().getVersion());
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
                String targetArg = args[2];

                if (type == null) {
                    sendMessage(sender, Config.getMessage("invalid-type"));
                    return true;
                }

                if (targetArg.equalsIgnoreCase("all")) {
                    java.util.Collection<? extends Player> online = getPlugin().getServer().getOnlinePlayers();
                    if (online == null || online.isEmpty()) {
                        sendMessage(sender, Config.getMessage("player-not-found"));
                        return true;
                    }
                    String messageAll = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                    for (Player p : online) {
                        petarLibSendCmd(messageAll, p, type);
                    }
                    break;
                }

                Player target = getPlugin().getServer().getPlayer(targetArg);
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
                    getPlugin().saveConfig();
                    sendMessage(sender, Config.getMessage("debug-enabled"));
                } else {
                    getPlugin().getConfig().set("debug", false);
                    getPlugin().saveConfig();
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
            sendMessage(sender, "<gold>--- PetarLib Help ---");
            sendMessage(sender, "<yellow>/petarlib help<white> - Show this help message");
            sendMessage(sender, "<yellow>/petarlib info<white> - Show plugin information");
            sendMessage(sender, "<yellow>/petarlib version<white> - Show plugin version");
            sendMessage(sender, "<yellow>/petarlib send <type> <player> <message><white> - Send a message to a player. Type can be 'chat' or 'actionbar'");
            sendMessage(sender, "<yellow>/petarlib reload<white> - Reload plugin config");
            sendMessage(sender, "<yellow>/petarlib debug<white> - Toggle debug mode");
    }
}
