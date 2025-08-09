package dev.twice.twitems.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexUtil {
    private static final Pattern PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public static String translate(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }

        final Matcher matcher = PATTERN.matcher(message);
        while (matcher.find()) {
            final String color = matcher.group(1);
            final String replacement = ChatColor.of("#" + color).toString();
            message = message.replace("&#" + color, replacement);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}