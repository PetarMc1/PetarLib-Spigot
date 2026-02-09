package com.petarmc.petarlib;

import com.petarmc.petarlib.commands.PetarLibCommand;
import com.petarmc.petarlib.expansion.PlaceHolderAPIExpansion;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class PetarLib extends JavaPlugin {
    private static PetarLib plugin;
    private BukkitAudiences adventure;
    private  boolean isPlaceHolderAPIActive = false;
    public static boolean DebugMode = false;
    public static PetarLib getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        DebugMode = getConfig().getBoolean("debug", false);
        PetarLibCommand cmdExec = new PetarLibCommand();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
           isPlaceHolderAPIActive = true;
           new PlaceHolderAPIExpansion(this).register();
        }


        getLogger().info("-----------------------------------------------------------");
        getLogger().info(" ____      _             _     _ _     ");
        getLogger().info("|  _ \\ ___| |_ __ _ _ __| |   (_) |__  ");
        getLogger().info("| |_) / _ \\ __/ _` | '__| |   | | '_ \\ ");
        getLogger().info("|  __/  __/ || (_| | |  | |___| | |_) |");
        getLogger().info("|_|   \\___|\\__\\__,_|_|  |_____|_|_.__/ ");
        getLogger().info("");
        getLogger().info("");
        getLogger().info("- Version v0.2.0");
        getLogger().info( isPlaceHolderAPIActive ? "- PlaceHolderAPI detected, registering expansions..."  : "- PlaceHolderAPI not detected, skipping expansions...");
        getLogger().info("-----------------------------------------------------------");
    }

    @Override
    public void onDisable() {
        getLogger().info("PetarLib stopped successfully!");
    }
}
