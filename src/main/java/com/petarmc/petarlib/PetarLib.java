package com.petarmc.petarlib;

import com.petarmc.petarlib.commands.PetarLibCommand;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

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


        getLogger().info("-----------------------------------------------------------");
        getLogger().info(" ____      _             _     _ _     ");
        getLogger().info("|  _ \\ ___| |_ __ _ _ __| |   (_) |__  ");
        getLogger().info("| |_) / _ \\ __/ _` | '__| |   | | '_ \\ ");
        getLogger().info("|  __/  __/ || (_| | |  | |___| | |_) |");
        getLogger().info("|_|   \\___|\\__\\__,_|_|  |_____|_|_.__/ ");
        getLogger().info("");
        getLogger().info("");
        getLogger().info("- Version v" + getDescription().getVersion());
        getLogger().info( Config.isPlaceHolderAPIActive ? "- PlaceHolderAPI detected, registering expansions..."  : "- PlaceHolderAPI not detected, skipping expansions...");
        if (Config.debugMode) {getLogger().info("- Debug mode enabled, extra logging will be shown.");};
        getLogger().info("-----------------------------------------------------------");
    }

    @Override
    public void onDisable() {
        getLogger().info("PetarLib stopped successfully!");

    }
}