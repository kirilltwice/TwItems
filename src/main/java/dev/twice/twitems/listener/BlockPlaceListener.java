package dev.twice.twitems.listener;

import dev.twice.twitems.ItemsPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataType;

public class BlockPlaceListener implements Listener {

    private final NamespacedKey unplaceableKey;

    public BlockPlaceListener(ItemsPlugin plugin) {
        this.unplaceableKey = new NamespacedKey(plugin, "unplaceable");
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        var item = event.getItemInHand();
        if (item.hasItemMeta()) {
            var pdc = item.getItemMeta().getPersistentDataContainer();
            if (pdc.has(unplaceableKey, PersistentDataType.BYTE)) {
                event.setCancelled(true);
            }
        }
    }
}
