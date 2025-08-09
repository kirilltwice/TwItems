package dev.twice.twitems.item.data;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

public record EnchantData(
        Enchantment enchantment,
        int level
) {

    public static EnchantData of(Enchantment enchantment, int level) {
        return new EnchantData(enchantment, level);
    }

    public static EnchantData parse(String enchantString) {
        var parts = enchantString.split(";");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid enchant format: " + enchantString);
        }

        var enchantment = Enchantment.getByKey(NamespacedKey.minecraft(parts[0].toLowerCase()));
        if (enchantment == null) {
            throw new IllegalArgumentException("Unknown enchantment: " + parts[0]);
        }

        var level = Integer.parseInt(parts[1]);
        return new EnchantData(enchantment, level);
    }
}