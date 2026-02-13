package com.petarmc.petarlib;

import com.petarmc.petarlib.commands.PetarLibCommand;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;
import org.bukkit.Bukkit;

public final class PetarLib extends JavaPlugin {
    private static PetarLib plugin;
    public static PetarLib getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        Config.load();

        PetarLibCommand cmdExec = new PetarLibCommand();
        Objects.requireNonNull(getCommand("petarlib"), "Command 'petarlib' is not defined in plugin.yml").setExecutor(cmdExec);
        Objects.requireNonNull(getCommand("petarlib"), "Command 'petarlib' is not defined in plugin.yml").setTabCompleter(new com.petarmc.petarlib.commands.TabCompletion());

        Bukkit.getConsoleSender().sendMessage("-----------------------------------------------------------");
        Bukkit.getConsoleSender().sendMessage(" ____      _             _     _ _     ");
        Bukkit.getConsoleSender().sendMessage("|  _ \\ ___| |_ __ _ _ __| |   (_) |__  ");
        Bukkit.getConsoleSender().sendMessage("| |_) / _ \\ __/ _` | '__| |   | | '_ \\ ");
        Bukkit.getConsoleSender().sendMessage("|  __/  __/ || (_| | |  | |___| | |_) |");
        Bukkit.getConsoleSender().sendMessage("|_|   \\___|\\__\\__,_|_|  |_____|_|_.__/ ");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("- Version v" + getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(Config.isPlaceHolderAPIActive ? "- PlaceHolderAPI detected, registering expansions..." : "- PlaceHolderAPI not detected, skipping expansions...");
        if (Config.debugMode) { Bukkit.getConsoleSender().sendMessage("- Debug mode enabled, extra logging will be shown."); }
        Bukkit.getConsoleSender().sendMessage("-----------------------------------------------------------");
    }

    @Override
    public void onDisable() {

    }
}