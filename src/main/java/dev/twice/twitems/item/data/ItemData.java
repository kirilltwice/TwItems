package dev.twice.twitems.item.data;

import lombok.Builder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.Set;

@Builder
public record ItemData(
        Material material,
        String displayName,
        List<String> lore,
        List<AttributeData> attributes,
        List<EnchantData> enchants,
        boolean unbreakable,
        Set<ItemFlag> flags,
        String texture,
        boolean placeable
) {
}