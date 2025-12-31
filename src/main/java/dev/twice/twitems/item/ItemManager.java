package dev.twice.twitems.item;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ItemManager {

    private final Map<String, ItemStack> itemsByKey = new ConcurrentHashMap<>();
    private final Map<Material, ItemStack> itemsByMaterial = new ConcurrentHashMap<>();
    private volatile Set<String> materialNames = Set.of();

    public void addItem(String key, ItemStack item) {
        itemsByKey.put(key, item);
        itemsByMaterial.put(item.getType(), item);
        updateMaterialNames();
    }

    public Optional<ItemStack> getItem(String key) {
        var item = itemsByKey.get(key);
        return item != null ? Optional.of(item.clone()) : Optional.empty();
    }

    public Optional<ItemStack> getItemByMaterial(String materialName) {
        try {
            var material = Material.valueOf(materialName);
            var item = itemsByMaterial.get(material);
            return item != null ? Optional.of(item.clone()) : Optional.empty();
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public void clear() {
        itemsByKey.clear();
        itemsByMaterial.clear();
        materialNames = Set.of();
    }

    private void updateMaterialNames() {
        materialNames = itemsByMaterial.keySet().stream()
                .map(Material::name)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }
}