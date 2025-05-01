package dev.twice.twitems.config;

import dev.twice.twitems.Main;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import dev.twice.twitems.utils.HexUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class ConfigManager {

    private final Main plugin;
    private FileConfiguration config;

    @Getter
    private final Map<String, ItemStack> itemMap = new HashMap<>();

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();

        CompletableFuture.runAsync(this::parseItems);
    }

    private void parseItems() {
        final ConfigurationSection section = config.getConfigurationSection("items");
        if (section == null) {
            return;
        }

        for (String key : section.getKeys(false)) {
            final ConfigurationSection itemSection = section.getConfigurationSection(key);
            if (itemSection == null) continue;

            try {
                String materialName = itemSection.getString("material");
                if (materialName == null) {
                    continue;
                }

                Material material;
                try {
                    material = Material.valueOf(materialName);
                } catch (IllegalArgumentException e) {
                    continue;
                }

                final ItemStack itemStack = new ItemStack(material);
                final ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta == null) continue;

                String displayName = itemSection.getString("displayName");
                if (displayName != null) {
                    itemMeta.setDisplayName(HexUtil.translate(displayName));
                }

                List<String> loreList = itemSection.getStringList("lore");
                if (!loreList.isEmpty()) {
                    List<String> translatedLore = new ArrayList<>();
                    for (String line : loreList) {
                        translatedLore.add(HexUtil.translate(line));
                    }
                    itemMeta.setLore(translatedLore);
                }

                parseAttributes(itemMeta, itemSection.getStringList("attributes"));

                parseEnchantments(itemMeta, itemSection.getStringList("enchants"));

                if (itemSection.getBoolean("unbreakable")) {
                    itemMeta.setUnbreakable(true);
                }

                if (itemSection.getBoolean("hideAttributes")) {
                    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                }

                if (itemSection.getBoolean("hideEnchants")) {
                    itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }

                itemStack.setItemMeta(itemMeta);

                itemMap.put(key, itemStack);
            } catch (Exception e) {
            }
        }
    }

    private void parseAttributes(ItemMeta itemMeta, List<String> attributesList) {
        for (String attributeStr : attributesList) {
            String[] parts = attributeStr.split(";");

            if (parts.length < 3) {
                continue;
            }

            try {
                Attribute attribute = Attribute.valueOf(parts[0]);
                double value = Double.parseDouble(parts[1]);
                EquipmentSlot slot = EquipmentSlot.valueOf(parts[2]);

                AttributeModifier modifier = new AttributeModifier(
                        UUID.randomUUID(),
                        attribute.getKey().getKey(),
                        value,
                        AttributeModifier.Operation.ADD_NUMBER,
                        slot
                );

                itemMeta.addAttributeModifier(attribute, modifier);
            } catch (Exception e) {
            }
        }
    }

    private void parseEnchantments(ItemMeta itemMeta, List<String> enchantmentsList) {
        for (String enchantStr : enchantmentsList) {
            String[] parts = enchantStr.split(";");

            if (parts.length < 2) {
                continue;
            }

            try {

                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(parts[0].toLowerCase()));

                if (enchantment == null) {
                    continue;
                }

                int level = Integer.parseInt(parts[1]);
                itemMeta.addEnchant(enchantment, level, true);
            } catch (Exception e) {
            }
        }
    }

    public ItemStack getItemByMaterial(String materialName) {
        return itemMap.values().stream()
                .filter(item -> item.getType().name().equals(materialName))
                .findFirst()
                .orElse(null);
    }

    public List<ItemStack> getAllItems() {
        return new ArrayList<>(itemMap.values());
    }
}