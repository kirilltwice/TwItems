package dev.twice.twitems.item;

import dev.twice.twitems.config.ConfigManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class ItemService {

    private final ConfigManager configManager;
    private final ItemParser itemParser = new ItemParser();
    private final ItemFactory itemFactory = new ItemFactory();
    private final ItemManager itemManager = new ItemManager();

    public void loadItems() {
        itemManager.clear();

        var section = configManager.getItemsSection();
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            var itemSection = section.getConfigurationSection(key);
            if (itemSection == null) continue;

            var itemData = itemParser.parseItemData(itemSection);
            if (itemData == null) continue;

            var item = itemFactory.createItem(itemData);
            if (item != null) {
                itemManager.addItem(key, item);
            }
        }
    }

    public void reloadItems() {
        loadItems();
    }

    public Optional<ItemStack> getItem(String key) {
        return itemManager.getItem(key);
    }

    public Optional<ItemStack> getItemByMaterial(String materialName) {
        return itemManager.getItemByMaterial(materialName);
    }

    public Set<String> getMaterialNames() {
        return itemManager.getMaterialNames();
    }

    public void shutdown() {
        itemManager.clear();
    }
}