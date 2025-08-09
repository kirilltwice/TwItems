package dev.twice.twitems.item.builder;

import dev.twice.twitems.utils.HexUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {

    private final ItemStack itemStack;
    @Getter
    private final ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setDisplayName(String name) {
        if (itemMeta != null) {
            itemMeta.setDisplayName(HexUtil.translate(name));
        }
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if (itemMeta != null && !lore.isEmpty()) {
            itemMeta.setLore(lore.stream()
                    .map(HexUtil::translate)
                    .toList());
        }
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        if (itemMeta != null) {
            itemMeta.setUnbreakable(unbreakable);
        }
        return this;
    }

    public ItemBuilder hideAttributes() {
        if (itemMeta != null) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        return this;
    }

    public ItemBuilder hideEnchants() {
        if (itemMeta != null) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        return this;
    }

    public ItemStack build() {
        if (itemMeta != null) {
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }
}