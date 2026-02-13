package com.petarmc.petarlib.notifications;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NotificationManagerTest {

    private final NotificationManager manager = new NotificationManager();

    @Test
    void sendActionBarNotification_usesAdventureComponent() {
        Player player = mock(Player.class);
        Component comp = Component.text("hi");

        manager.sendActionBarNotification(player, comp);

        verify(player).sendActionBar(comp);
    }

    @Test
    void sendChatNotification_usesAdventureComponent() {
        Player player = mock(Player.class);
        Component comp = Component.text("chat");

        manager.sendChatNotification(player, comp);

        verify(player).sendMessage(comp);
    }

    @Test
    void sendTitleNotification_usesTitle() {
        Player player = mock(Player.class);
        Title title = Title.title(Component.text("t"), Component.text("s"));

        manager.sendTitleNotification(player, title);

        verify(player).showTitle(title);
    }

    @Test
    void sendActionBarNotificationLegacy_usesString() {
        Player player = mock(Player.class);

        manager.sendActionBarNotificationLegacy(player, "legacy");

        verify(player).sendActionBar("legacy");
    }

    @Test
    void sendChatNotificationLegacy_usesString() {
        Player player = mock(Player.class);

        manager.sendChatNotificationLegacy(player, "legacy");

        verify(player).sendMessage("legacy");
    }

    @Test
    void sendTitleNotificationLegacy_usesStrings() {
        Player player = mock(Player.class);

        manager.sendTitleNotificationLegacy(player, "t", "s");

        verify(player).sendTitle("t", "s");
    }
}

