package dev.twice.twitems.command;

import dev.twice.twitems.Main;
import dev.twice.twitems.item.ItemService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CommandRegistry {

    private final Main plugin;
    private final ItemService itemService;

    public void registerAll() {
        registerGiveItemCommand();
        registerReloadCommand();
    }

    private void registerGiveItemCommand() {
        var giveCommand = new GiveItemCommand(itemService);
        plugin.getCommand("giveitem").setExecutor(giveCommand);
        plugin.getCommand("giveitem").setTabCompleter(giveCommand);
    }

    private void registerReloadCommand() {
        var reloadCommand = new ReloadCommand(plugin);
        plugin.getCommand("twitems").setExecutor(reloadCommand);
    }
}