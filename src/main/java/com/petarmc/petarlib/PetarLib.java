package com.petarmc.petarlib;

import com.petarmc.petarlib.commands.PetarLibCommand;
import com.petarmc.petarlib.expansion.PlaceHolderAPIExpansion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public final class PetarLib extends JavaPlugin {
    private static PetarLib plugin;
    private BukkitAudiences adventure;
    private  boolean isPlaceHolderAPIActive = false;
    public static boolean DebugMode = false;
    public static PetarLib getPlugin() {
        return plugin;
    }

    //used for adventure
    @NonNull
    public BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    @Override
    public void onEnable() {
        plugin = this;
        //used for adventure
        this.adventure = BukkitAudiences.create(this);

        saveDefaultConfig();
        DebugMode = getConfig().getBoolean("debug", false);
        PetarLibCommand cmdExec = new PetarLibCommand();
        Objects.requireNonNull(getCommand("petarlib"), "Command 'petarlib' is not defined in plugin.yml").setExecutor(cmdExec);

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


        //used for adventure
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }
}
