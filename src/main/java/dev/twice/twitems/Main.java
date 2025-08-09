package dev.twice.twitems;

import dev.twice.twitems.command.GiveItemCommand;
import dev.twice.twitems.command.ReloadCommand;
import dev.twice.twitems.config.ConfigManager;
import dev.twice.twitems.item.ItemService;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Getter
    private ConfigManager configManager;

    @Getter
    private ItemService itemService;

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(this);
        itemService = new ItemService(configManager);

        configManager.initialize();
        itemService.loadItems();

        setupCommands();
    }

    @Override
    public void onDisable() {
        if (itemService != null) {
            itemService.shutdown();
        }
        instance = null;
    }

    private void setupCommands() {
        var giveCmd = new GiveItemCommand(itemService);
        getCommand("giveitem").setExecutor(giveCmd);
        getCommand("giveitem").setTabCompleter(giveCmd);

        var reloadCmd = new ReloadCommand(this);
        getCommand("twitems").setExecutor(reloadCmd);
    }

    public void reload() {
        configManager.reload();
        itemService.reloadItems();
    }
}