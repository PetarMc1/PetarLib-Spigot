package com.petarmc.petarlib;

import com.petarmc.petarlib.expansion.PlaceHolderAPIExpansion;
import org.bukkit.Bukkit;

import static com.petarmc.petarlib.PetarLib.getPlugin;

public class Config {
    public static boolean debugMode = false;
    public static boolean isPlaceHolderAPIActive = false;
    public static int defaultMaxRetries = 3;


    public static void load() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            isPlaceHolderAPIActive = true;
            new PlaceHolderAPIExpansion(getPlugin()).register();
        }
        debugMode = getPlugin().getConfig().getBoolean("debug", false);
        defaultMaxRetries = getPlugin().getConfig().getInt("defaultMaxRetries", 3);
    }
}
