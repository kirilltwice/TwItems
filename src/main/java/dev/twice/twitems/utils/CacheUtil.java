package dev.twice.twitems.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CacheUtil {

    private static final Map<String, Material> MATERIALS = Arrays.stream(Material.values())
            .collect(Collectors.toUnmodifiableMap(Material::name, m -> m));

    private static final Map<String, Attribute> ATTRIBUTES = Arrays.stream(Attribute.values())
            .collect(Collectors.toUnmodifiableMap(Attribute::name, a -> a));

    private static final Map<String, EquipmentSlot> SLOTS = Arrays.stream(EquipmentSlot.values())
            .collect(Collectors.toUnmodifiableMap(EquipmentSlot::name, s -> s));

    private static final Map<String, ItemStack> ITEMS = new ConcurrentHashMap<>();
    private static final Map<Material, ItemStack> ITEMS_BY_MATERIAL = new ConcurrentHashMap<>();
    private static volatile Set<String> MATERIAL_NAMES = Set.of();

    private CacheUtil() {}

    public static Material getMaterial(String name) {
        return MATERIALS.get(name);
    }

    public static Attribute getAttribute(String name) {
        return ATTRIBUTES.get(name);
    }

    public static EquipmentSlot getSlot(String name) {
        return SLOTS.get(name);
    }

    public static void loadItems(ConfigurationSection section) {
        clearItems();

        if (section == null) return;

        for (String key : section.getKeys(false)) {
            var itemSection = section.getConfigurationSection(key);
            if (itemSection != null) {
                try {
                    var item = createItem(itemSection);
                    if (item != null) {
                        ITEMS.put(key, item);
                        ITEMS_BY_MATERIAL.put(item.getType(), item);
                    }
                } catch (Exception ignored) {}
            }
        }

        updateMaterialNames();
    }

    public static Optional<ItemStack> getItem(String key) {
        var item = ITEMS.get(key);
        return item != null ? Optional.of(item.clone()) : Optional.empty();
    }

    public static Optional<ItemStack> getItemByMaterial(String materialName) {
        try {
            var material = Material.valueOf(materialName);
            var item = ITEMS_BY_MATERIAL.get(material);
            return item != null ? Optional.of(item.clone()) : Optional.empty();
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Set<String> getMaterialNames() {
        return MATERIAL_NAMES;
    }

    public static void clearItems() {
        ITEMS.clear();
        ITEMS_BY_MATERIAL.clear();
        MATERIAL_NAMES = Set.of();
    }

    private static ItemStack createItem(ConfigurationSection section) {
        var materialName = section.getString("material");
        if (materialName == null) return null;

        var material = getMaterial(materialName);
        if (material == null) return null;

        var item = new ItemStack(material);
        var meta = item.getItemMeta();
        if (meta == null) return null;

        var displayName = section.getString("displayName");
        if (displayName != null) {
            meta.setDisplayName(HexUtil.translate(displayName));
        }

        var lore = section.getStringList("lore");
        if (!lore.isEmpty()) {
            meta.setLore(lore.stream().map(HexUtil::translate).toList());
        }

        var attributes = section.getStringList("attributes");
        for (String attrStr : attributes) {
            try {
                var parts = attrStr.split(";");
                if (parts.length >= 3) {
                    var attribute = getAttribute(parts[0]);
                    var value = Double.parseDouble(parts[1]);
                    var slot = getSlot(parts[2]);

                    if (attribute != null && slot != null) {
                        var modifier = new AttributeModifier(
                                UUID.randomUUID(),
                                attribute.getKey().getKey(),
                                value,
                                AttributeModifier.Operation.ADD_NUMBER,
                                slot
                        );
                        meta.addAttributeModifier(attribute, modifier);
                    }
                }
            } catch (Exception ignored) {}
        }

        var enchants = section.getStringList("enchants");
        for (String enchStr : enchants) {
            try {
                var parts = enchStr.split(";");
                if (parts.length >= 2) {
                    var enchant = Enchantment.getByKey(NamespacedKey.minecraft(parts[0].toLowerCase()));
                    var level = Integer.parseInt(parts[1]);

                    if (enchant != null) {
                        meta.addEnchant(enchant, level, true);
                    }
                }
            } catch (Exception ignored) {}
        }

        meta.setUnbreakable(section.getBoolean("unbreakable", false));

        if (section.getBoolean("hideAttributes", false)) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        if (section.getBoolean("hideEnchants", false)) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (section.getBoolean("hideUnbreakable", false)) {
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }

        item.setItemMeta(meta);
        return item;
    }

    private static void updateMaterialNames() {
        MATERIAL_NAMES = ITEMS_BY_MATERIAL.keySet().stream()
                .map(Material::name)
                .collect(Collectors.toUnmodifiableSet());
    }
}