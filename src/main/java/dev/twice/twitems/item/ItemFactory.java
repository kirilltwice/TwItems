package dev.twice.twitems.item;

import dev.twice.twitems.item.data.AttributeData;
import dev.twice.twitems.item.data.EnchantData;
import dev.twice.twitems.item.data.ItemData;
import dev.twice.twitems.utils.HexUtil;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ItemFactory {

    public ItemStack createItem(ItemData data) {
        var item = new ItemStack(data.material());
        var meta = item.getItemMeta();
        if (meta == null) return null;

        if (data.displayName() != null) {
            meta.setDisplayName(HexUtil.translate(data.displayName()));
        }

        if (!data.lore().isEmpty()) {
            meta.setLore(data.lore().stream()
                    .map(HexUtil::translate)
                    .toList());
        }

        applyAttributes(meta, data.attributes());
        applyEnchantments(meta, data.enchants());

        meta.setUnbreakable(data.unbreakable());

        if (!data.flags().isEmpty()) {
            meta.addItemFlags(data.flags().toArray(new ItemFlag[0]));
        }

        item.setItemMeta(meta);
        return item;
    }

    private void applyAttributes(org.bukkit.inventory.meta.ItemMeta meta,
                                 java.util.List<AttributeData> attributes) {
        attributes.forEach(attr -> {
            var modifier = new AttributeModifier(
                    UUID.randomUUID(),
                    attr.attribute().getKey().getKey(),
                    attr.value(),
                    AttributeModifier.Operation.ADD_NUMBER,
                    attr.slot()
            );
            meta.addAttributeModifier(attr.attribute(), modifier);
        });
    }

    private void applyEnchantments(org.bukkit.inventory.meta.ItemMeta meta,
                                   java.util.List<EnchantData> enchants) {
        enchants.forEach(ench ->
                meta.addEnchant(ench.enchantment(), ench.level(), true));
    }
}