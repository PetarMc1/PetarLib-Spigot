package com.petarmc.petarlib.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TabCompletion implements TabCompleter {
    private static final List<String> ADMIN_SUBCOMMANDS = Arrays.asList("reload", "debug");
    private static final List<String> BASE_SUBCOMMANDS = Arrays.asList("help", "info", "version");
    private static final List<String> MESSAGE_TYPES = Arrays.asList("chat", "actionbar");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!"petarlib".equalsIgnoreCase(command.getName())) {
            return Collections.emptyList();
        }

        if (args.length == 0) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            List<String> options = new ArrayList<>(BASE_SUBCOMMANDS);
            if (sender.hasPermission("petarlib.send")) {
                options.add("send");
            }
            if (sender.hasPermission("petarlib.admin")) {
                options.addAll(ADMIN_SUBCOMMANDS);
            }
            return filter(options, args[0]);
        }

        String sub = args[0].toLowerCase(Locale.ROOT);
        switch (sub) {
            case "send":
                if (!sender.hasPermission("petarlib.send")) {
                    return Collections.emptyList();
                }
                if (args.length == 2) {
                    return filter(MESSAGE_TYPES, args[1]);
                }
                if (args.length == 3) {
                    return filter(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), args[2]);
                }
                return Collections.emptyList();
            case "reload":
            case "debug":
                return sender.hasPermission("petarlib.admin") ? Collections.emptyList() : Collections.emptyList();
            default:
                return Collections.emptyList();
        }
    }

    private List<String> filter(List<String> options, String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return options;
        }
        String lower = prefix.toLowerCase(Locale.ROOT);
        return options.stream()
                .filter(opt -> opt.toLowerCase(Locale.ROOT).startsWith(lower))
                .collect(Collectors.toList());
    }
}
