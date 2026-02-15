package com.petarmc.petarlib.cooldowns;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a cooldown expires or is cleaned up. Not cancellable.
 */
public class CooldownEndEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final UUID playerId;
    private final String cooldownId;
    private final long endTimestamp;

    public CooldownEndEvent(UUID playerId, String cooldownId, long endTimestamp) {
        super(false);
        this.playerId = playerId;
        this.cooldownId = cooldownId;
        this.endTimestamp = endTimestamp;
    }

    /** @return UUID of the player whose cooldown ended */
    public UUID getPlayerId() {
        return playerId;
    }

    /** @return cooldown identifier */
    public String getCooldownId() {
        return cooldownId;
    }

    /** @return epoch millis when the cooldown ended */
    public long getEndTimestamp() {
        return endTimestamp;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
