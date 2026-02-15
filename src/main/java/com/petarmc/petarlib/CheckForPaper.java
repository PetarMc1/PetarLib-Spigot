package com.petarmc.petarlib;

import org.bukkit.Bukkit;

public class CheckForPaper {
    /**
     * Attempts to detect if the running server is Paper.
     * First checks for a known Paper class, then falls back to inspecting
     * the Bukkit name/version strings for the substring "paper".
     */
    public static boolean isPaperServer() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException ignored) {
        }
        String name = Bukkit.getName();
        String version = Bukkit.getVersion();
        return (name != null && name.toLowerCase().contains("paper")) || (version != null && version.toLowerCase().contains("paper"));
    }
}
