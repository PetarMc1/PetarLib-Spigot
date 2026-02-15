package com.petarmc.petarlib.notifications;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

public class NotificationManager {
    /**
     * Shows an action bar notification to the player. This will appear above the player's hotbar and disappear after a few seconds.
     * Supports Adventure API Components ONLY!
     *
     * @param player The player to whom the action bar notification will be shown.
     * @param component The component to show as an action bar notification.
     */
    public void sendActionBarNotification(Player player, Component component) {
        player.sendActionBar(component);
    }

    /**
     * Shows an action bar notification to the player. This will appear above the player's hotbar and disappear after a few seconds.
     * Supports legacy color codes (e.g. {@code &c} for red).
     *
     * @param player The player to whom the action bar notification will be shown.
     * @param message The message to show as an action bar notification, can use legacy color.
     */
    public void sendActionBarNotification(Player player, String message) {
        player.sendActionBar(message);
    }

    /**
     * Shows a chat message to the player.
     * Supports Adventure API Components ONLY!
     *
     * @param player The player to whom the chat message will be sent.
     * @param component The component to send as a chat message.
     */
    public void sendChatNotification(Player player, Component component) {
        player.sendMessage(component);
    }

    /**
     * Shows a chat message to the player.
     * Supports legacy color codes (e.g. {@code &c} for red).
     *
     * @param player The player to whom the chat message will be sent.
     * @param message The message to send as a chat message, can use legacy color.
     */
    public void sendChatNotification(Player player, String message) {
        player.sendMessage(message);
    }

    /**
     * Shows a title notification to the player. This will appear in the center of the player's screen and can include a title, subtitle, and fade in/out times.
     * Supports Adventure API Title objects ONLY!
     *
     * @param player The player to whom the title notification will be shown.
     * @param title The Title object to show as a title notification, can include title text, subtitle text, and timing information.
     */
    public void sendTitleNotification(Player player, Title title) {
        player.showTitle(title);
    }

    /**
     * Shows a title notification to the player. This will appear in the center of the player's screen.
     * Supports legacy color codes (e.g. {@code &c} for red).
     *
     * @param player The player to whom the title notification will be shown.
     * @param title The title text to show, can use legacy color.
     * @param subtitle The subtitle text to show, can use legacy color.
     */
    public void sendTitleNotification(Player player, String title, String subtitle) {
        player.sendTitle(title, subtitle);
    }
}
