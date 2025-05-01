package dev.twice.twitems;

import dev.twice.twitems.config.ConfigManager;
import dev.twice.twitems.command.GiveItemCommand;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Getter
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(this);
        configManager.loadConfig();

        getCommand("giveitem").setExecutor(new GiveItemCommand());
    }
}