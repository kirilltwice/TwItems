package dev.twice.twitems.config;

import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public class ConfigService {

    private final Plugin plugin;
    private FileConfiguration config;

    public void initialize() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public void reload() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public ConfigurationSection getItemsSection() {
        return config.getConfigurationSection("items");
    }
}