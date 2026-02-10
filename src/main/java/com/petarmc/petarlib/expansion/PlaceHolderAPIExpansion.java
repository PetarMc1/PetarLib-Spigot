package com.petarmc.petarlib.expansion;

import com.petarmc.petarlib.PetarLib;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import static com.petarmc.petarlib.PetarLib.getPlugin;
public class PlaceHolderAPIExpansion extends PlaceholderExpansion {

    private final PetarLib plugin;

    public PlaceHolderAPIExpansion(PetarLib plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "PetarLib";
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        switch (params.toLowerCase()) {
            case "petarlib_version":
                return plugin.getDescription().getVersion();
            case "petarlib_debug":
                if (PetarLib.getPlugin().getConfig().getBoolean("debug")) {
                    return "enabled";
                } else {
                    return "disabled";
                }
            case "petarlib_debug_boolean":
                return PetarLib.getPlugin().getConfig().getBoolean("debug") ? "true" : "false";
        }
        return null;
    }
}