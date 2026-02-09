package com.petarmc.petarlib.expansion;

import com.petarmc.petarlib.PetarLib;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceHolderAPIExpansion extends PlaceholderExpansion {

    private final PetarLib plugin;

    public PlaceHolderAPIExpansion(PetarLib plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors()); //
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
        if (params.equalsIgnoreCase("petarlib_test")) {
            return "PlaceHolderAPI Expansion works!";
        }
        return null; //
    }
}