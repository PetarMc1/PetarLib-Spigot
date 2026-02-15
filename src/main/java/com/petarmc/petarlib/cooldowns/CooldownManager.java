package com.petarmc.petarlib.cooldowns;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import static com.petarmc.petarlib.PetarLib.getPlugin;

/**
 * Per-player cooldowns.
 * Stores expiration timestamps (System.currentTimeMillis) and cleans expired entries on read.
 */
public class CooldownManager {
    private final Map<UUID, ConcurrentHashMap<String, Long>> cooldowns = new ConcurrentHashMap<>();

    /**
     * Checks if the given player is still on the specified cooldown.
     * @param playerId player UUID
     * @param cooldownId logical cooldown identifier
     * @return {@code true} if cooldown exists and has not expired
     */
    public boolean isOnCooldown(UUID playerId, String cooldownId) {
        return getRemaining(playerId, cooldownId) > 0;
    }

    /**
     * Player overload of {@link #isOnCooldown(UUID, String)}.
     * @param player Bukkit player (ignored if null)
     * @param cooldownId logical cooldown identifier
     * @return {@code true} if cooldown exists and has not expired
     */
    public boolean isOnCooldown(Player player, String cooldownId) {
        if (player == null) return false;
        return isOnCooldown(player.getUniqueId(), cooldownId);
    }

    /**
     * Returns remaining cooldown time in milliseconds for the given player and ID.
     * Expired or missing cooldowns return 0 and are pruned from storage.
     * Calls CooldownEndEvent when an expiration is detected.
     * @param playerId player UUID
     * @param cooldownId logical cooldown identifier
     * @return remaining time in ms, or 0 if expired/missing
     */
    public long getRemaining(UUID playerId, String cooldownId) {
        if (playerId == null || cooldownId == null) return 0L;
        ConcurrentHashMap<String, Long> playerCooldowns = cooldowns.get(playerId);
        if (playerCooldowns == null) return 0L;

        long now = System.currentTimeMillis();
        Long expiresAt = playerCooldowns.get(cooldownId);
        if (expiresAt == null) return 0L;

        long remaining = expiresAt - now;
        if (remaining <= 0L) {
            triggerEnd(playerId, cooldownId, expiresAt, playerCooldowns);
            return 0L;
        }
        return remaining;
    }

    /**
     * Player overload of {@link #getRemaining(UUID, String)}.
     * @param player Bukkit player (ignored if null)
     * @param cooldownId logical cooldown identifier
     * @return remaining time in ms, or 0 if expired/missing
     */
    public long getRemaining(Player player, String cooldownId) {
        if (player == null) return 0L;
        return getRemaining(player.getUniqueId(), cooldownId);
    }

    /**
     * Sets a cooldown for the given player and ID using the supplied duration.
     * Calls CooldownStartEvent; if cancelled, no cooldown is stored.
     * @param playerId player UUID
     * @param cooldownId logical cooldown identifier
     * @param durationMs duration in milliseconds (ignored if non-positive)
     */
    public void setCooldown(UUID playerId, String cooldownId, long durationMs) {
        if (playerId == null || cooldownId == null || durationMs <= 0L) return;
        long expiresAt = System.currentTimeMillis() + durationMs;

        CooldownStartEvent startEvent = new CooldownStartEvent(playerId, cooldownId, durationMs, expiresAt);
        callEvent(startEvent);
        if (startEvent.isCancelled()) return;

        ConcurrentHashMap<String, Long> playerCooldowns = cooldowns.computeIfAbsent(playerId, id -> new ConcurrentHashMap<>());
        playerCooldowns.put(cooldownId, expiresAt);
    }

    /**
     * Player overload of {@link #setCooldown(UUID, String, long)}.
     * @param player Bukkit player (ignored if null)
     * @param cooldownId logical cooldown identifier
     * @param durationMs duration in milliseconds (ignored if non-positive)
     */
    public void setCooldown(Player player, String cooldownId, long durationMs) {
        if (player == null) return;
        setCooldown(player.getUniqueId(), cooldownId, durationMs);
    }

    /**
     * Removes a specific cooldown for a player, if present.
     * Does not call end events; intended for manual invalidation.
     * @param playerId player UUID
     * @param cooldownId logical cooldown identifier
     */
    public void removeCooldown(UUID playerId, String cooldownId) {
        if (playerId == null || cooldownId == null) return;
        ConcurrentHashMap<String, Long> playerCooldowns = cooldowns.get(playerId);
        if (playerCooldowns == null) return;
        removeAndPrune(playerId, playerCooldowns, cooldownId);
    }

    /**
     * Checks a single cooldown for expiry, calls CooldownEndEvent, and removes it if expired.
     * @param playerId player UUID
     * @param cooldownId logical cooldown identifier
     * @return true if cooldown existed and was expired/removed
     */
    public boolean checkAndTriggerEnd(UUID playerId, String cooldownId) {
        if (playerId == null || cooldownId == null) return false;
        ConcurrentHashMap<String, Long> playerCooldowns = cooldowns.get(playerId);
        if (playerCooldowns == null) return false;
        Long expiresAt = playerCooldowns.get(cooldownId);
        if (expiresAt == null) return false;
        long now = System.currentTimeMillis();
        if (expiresAt <= now) {
            triggerEnd(playerId, cooldownId, expiresAt, playerCooldowns);
            return true;
        }
        return false;
    }

    /**
     * Player overload of {@link #checkAndTriggerEnd(UUID, String)}.
     * @param player Bukkit player (ignored if null)
     * @param cooldownId logical cooldown identifier
     * @return true if cooldown existed and was expired/removed
     */
    public boolean checkAndTriggerEnd(Player player, String cooldownId) {
        if (player == null) return false;
        return checkAndTriggerEnd(player.getUniqueId(), cooldownId);
    }

    /**
     * Formats a remaining duration into mm:ss (zero-padded) format.
     * @param ms duration in milliseconds
     * @return formatted time, or 00:00 when non-positive
     */
    public static String formatRemaining(long ms) {
        if (ms <= 0L) return "00:00";
        long minutes = TimeUnit.MILLISECONDS.toMinutes(ms);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Triggers CooldownEndEvent and removes the entry when expired.
     * Ensures end events are dispatched before cleanup.
     */
    private void triggerEnd(UUID playerId, String cooldownId, long expiresAt, ConcurrentHashMap<String, Long> playerCooldowns) {
        CooldownEndEvent endEvent = new CooldownEndEvent(playerId, cooldownId, expiresAt);
        callEvent(endEvent);
        removeAndPrune(playerId, playerCooldowns, cooldownId);
    }

    /**
     * Calls a Bukkit event, ensuring dispatch occurs on the main thread.
     * Safe to invoke from async contexts.
     * @param event Bukkit event to dispatch
     */
    private void callEvent(Event event) {
        Runnable call = () -> Bukkit.getPluginManager().callEvent(event);
        if (Bukkit.isPrimaryThread()) {
            call.run();
        } else {
            Bukkit.getScheduler().runTask(getPlugin(), call);
        }
    }

    private void removeAndPrune(UUID playerId, ConcurrentHashMap<String, Long> playerCooldowns, String cooldownId) {
        playerCooldowns.remove(cooldownId);
        if (playerCooldowns.isEmpty()) {
            cooldowns.remove(playerId, playerCooldowns);
        }
    }

}
