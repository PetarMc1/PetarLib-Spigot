package com.petarmc.petarlib;

import com.petarmc.petarlib.expansion.PlaceHolderAPIExpansion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

import static com.petarmc.petarlib.PetarLib.getPlugin;

public class Config {
    public static boolean debugMode = false;
    public static boolean isPlaceHolderAPIActive = false;
    public static int defaultMaxRetries = 3;
    public static boolean useMiniMessage() { return getPlugin().getConfig().getBoolean("use-minimessage"); }
    public static FileConfiguration getMessagesConfig() { return messagesConfig; }
    public static void reloadMessages(){ messagesConfig = YamlConfiguration.loadConfiguration(messagesFile); }
    public static String getMessage(String path) { return applyPlaceholders(getMessagesConfig().getString(path)); }
    private static File messagesFile = new File(getPlugin().getDataFolder(), "messages.yml");
    private static FileConfiguration messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

    private static String applyPlaceholders(String message) {
        if (message == null) return null;
        String version = getPlugin().getDescription().getVersion();
        String authors = String.join(", ", getPlugin().getDescription().getAuthors());
        return message
                .replace("{version}", version == null ? "" : version)
                .replace("{authors}", authors == null ? "" : authors);
    }


    public static void load() {
        //check if PlaceHolderAPI is installed/enabled
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            isPlaceHolderAPIActive = true;
            new PlaceHolderAPIExpansion(getPlugin()).register();
        }
        // check if messages.yml exists
        if (!messagesFile.exists()) {
            getPlugin().saveResource("messages.yml", false);
        }
        debugMode = getPlugin().getConfig().getBoolean("debug", false);
        defaultMaxRetries = getPlugin().getConfig().getInt("defaultMaxRetries", 3);
    }
}
