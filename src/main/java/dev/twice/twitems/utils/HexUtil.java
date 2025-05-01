package dev.twice.twitems.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexUtil {
    private final static Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
    public static String translate(String message) {
        final Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            final String color = matcher.group(1);
            final String replacement = ChatColor.of("#" + color).toString();
            message = message.replace("&#" + color, replacement);
        }
        message = message.replace("\n", "\n");
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}