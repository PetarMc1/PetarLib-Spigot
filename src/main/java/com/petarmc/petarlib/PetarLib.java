package com.petarmc.petarlib;

import com.petarmc.petarlib.commands.PetarLibCommand;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import org.jspecify.annotations.NonNull;
import java.util.Objects;

public final class PetarLib extends JavaPlugin {
    private static PetarLib plugin;
    private BukkitAudiences adventure;
    public static PetarLib getPlugin() {
        return plugin;
    }

    @NonNull
    public BukkitAudiences adventure() {
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }
    public boolean hasAdventure() {
        return this.adventure != null;
    }
    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        this.adventure = BukkitAudiences.create(this);
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
        Bukkit.getConsoleSender().sendMessage("- Running version v" + getDescription().getVersion() + " for" + (CheckForPaper.isPaperServer() ? " Paper" : " Spigot/CraftBukkit")+".");
        Bukkit.getConsoleSender().sendMessage(Config.isPlaceHolderAPIActive ? "- PlaceHolderAPI detected, registering expansions..." : "- PlaceHolderAPI not detected, skipping expansions...");
        if (Config.debugMode) { Bukkit.getConsoleSender().sendMessage("- Debug mode enabled, extra logging will be shown."); }
        if (Config.debugMode) { Bukkit.getConsoleSender().sendMessage("- Using " + (CheckForPaper.isPaperServer() ? "built in" : "shaded") + " Adventure support."); }
        Bukkit.getConsoleSender().sendMessage("-----------------------------------------------------------");
    }


    @Override
    public void onDisable() {
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }
}