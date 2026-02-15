package com.petarmc.petarlib.cooldowns;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a cooldown is about to start. Cancel to prevent the cooldown being applied.
 */
public class CooldownStartEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID playerId;
    private final String cooldownId;
    private final long durationMillis;
    private final long endTimestamp;
    private boolean cancelled;

    public CooldownStartEvent(UUID playerId, String cooldownId, long durationMillis, long endTimestamp) {
        super(false);
        this.playerId = playerId;
        this.cooldownId = cooldownId;
        this.durationMillis = durationMillis;
        this.endTimestamp = endTimestamp;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getCooldownId() {
        return cooldownId;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

