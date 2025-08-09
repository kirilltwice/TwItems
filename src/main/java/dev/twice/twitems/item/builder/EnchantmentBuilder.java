package dev.twice.twitems.item.builder;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class EnchantmentBuilder {

    public void applyEnchantments(ItemMeta itemMeta, List<String> enchantmentsList) {
        for (String enchantStr : enchantmentsList) {
            var parts = enchantStr.split(";");
            if (parts.length < 2) continue;

            try {
                var enchantment = Enchantment.getByKey(NamespacedKey.minecraft(parts[0].toLowerCase()));
                if (enchantment == null) continue;

                var level = Integer.parseInt(parts[1]);
                itemMeta.addEnchant(enchantment, level, true);
            } catch (Exception ignored) {}
        }
    }
}
