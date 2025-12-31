package dev.twice.twitems;

import dev.twice.twitems.command.GiveItemCommand;
import dev.twice.twitems.command.ReloadCommand;
import dev.twice.twitems.config.ConfigService;
import dev.twice.twitems.item.ItemService;
import dev.twice.twitems.listener.BlockPlaceListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemsPlugin extends JavaPlugin {

    @Getter
    private static ItemsPlugin instance;

    @Getter
    private ConfigService configService;

    @Getter
    private ItemService itemService;

    @Override
    public void onEnable() {
        instance = this;

        configService = new ConfigService(this);
        itemService = new ItemService(configService);

        configService.initialize();
        itemService.loadItems();

        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);

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
        configService.reload();
        itemService.reloadItems();
    }
}