package net.savagedev.imlib.utils;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;

public class MessageUtils {
    public static String color(@Nonnull String message) {
        if (message.isEmpty()) {
            return message;
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
