package com.petarmc.petarlib.notifications;


import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class NotificationManager {

    /**
     * Shows an action bar notification to the player. This will appear above the player's hotbar and disappear after a few seconds.
     *
     * @param msg
     * @param p
     */
    public void showActionBarNotif(String msg, Player p) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg));
    }

    /**
     * Sends a chat message to the player. This is not an action bar notification, but a regular chat message.
     *
     * @param msg
     * @param p
     */
    public void sendChatNotif(String msg, Player p) {
        p.sendMessage(msg);
    }




}
