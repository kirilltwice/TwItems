package dev.twice.twitems.item;

import dev.twice.twitems.item.data.AttributeData;
import dev.twice.twitems.item.data.EnchantData;
import dev.twice.twitems.item.data.ItemData;
import dev.twice.twitems.utils.CacheUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class ItemParser {

    public ItemData parseItemData(ConfigurationSection section) {
        var materialName = section.getString("material");
        if (materialName == null) return null;

        var material = CacheUtil.getMaterial(materialName);
        if (material == null) return null;

        var displayName = section.getString("displayName");
        var lore = section.getStringList("lore");
        var attributes = parseAttributes(section.getStringList("attributes"));
        var enchants = parseEnchants(section.getStringList("enchants"));
        var unbreakable = section.getBoolean("unbreakable", false);
        var flags = parseFlags(section);

        return new ItemData(material, displayName, lore, attributes, enchants, unbreakable, flags);
    }

    private List<AttributeData> parseAttributes(List<String> attributesList) {
        return attributesList.stream()
                .map(this::parseAttribute)
                .filter(attr -> attr != null)
                .toList();
    }

    private AttributeData parseAttribute(String attributeStr) {
        try {
            var parts = attributeStr.split(";");
            if (parts.length < 3) return null;

            var attribute = CacheUtil.getAttribute(parts[0]);
            var value = Double.parseDouble(parts[1]);
            var slot = CacheUtil.getSlot(parts[2]);

            return (attribute != null && slot != null) ?
                    new AttributeData(attribute, value, slot) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private List<EnchantData> parseEnchants(List<String> enchantsList) {
        return enchantsList.stream()
                .map(this::parseEnchant)
                .filter(ench -> ench != null)
                .toList();
    }

    private EnchantData parseEnchant(String enchantStr) {
        try {
            var parts = enchantStr.split(";");
            if (parts.length < 2) return null;

            var enchantment = Enchantment.getByKey(NamespacedKey.minecraft(parts[0].toLowerCase()));
            var level = Integer.parseInt(parts[1]);

            return enchantment != null ? new EnchantData(enchantment, level) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private Set<ItemFlag> parseFlags(ConfigurationSection section) {
        var flags = EnumSet.noneOf(ItemFlag.class);

        if (section.getBoolean("hideAttributes", false)) {
            flags.add(ItemFlag.HIDE_ATTRIBUTES);
        }
        if (section.getBoolean("hideEnchants", false)) {
            flags.add(ItemFlag.HIDE_ENCHANTS);
        }
        if (section.getBoolean("hideUnbreakable", false)) {
            flags.add(ItemFlag.HIDE_UNBREAKABLE);
        }

        return Set.copyOf(flags);
    }
}