package dev.twice.twitems.item.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.Set;

public record ItemData(
        Material material,
        String displayName,
        List<String> lore,
        List<AttributeData> attributes,
        List<EnchantData> enchants,
        boolean unbreakable,
        Set<ItemFlag> flags
) {

    public static Builder builder(Material material) {
        return new Builder(material);
    }

    public static final class Builder {
        private final Material material;
        private String displayName;
        private List<String> lore = List.of();
        private List<AttributeData> attributes = List.of();
        private List<EnchantData> enchants = List.of();
        private boolean unbreakable = false;
        private Set<ItemFlag> flags = Set.of();

        private Builder(Material material) {
            this.material = material;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder lore(List<String> lore) {
            this.lore = List.copyOf(lore);
            return this;
        }

        public Builder attributes(List<AttributeData> attributes) {
            this.attributes = List.copyOf(attributes);
            return this;
        }

        public Builder enchants(List<EnchantData> enchants) {
            this.enchants = List.copyOf(enchants);
            return this;
        }

        public Builder unbreakable(boolean unbreakable) {
            this.unbreakable = unbreakable;
            return this;
        }

        public Builder flags(Set<ItemFlag> flags) {
            this.flags = Set.copyOf(flags);
            return this;
        }

        public ItemData build() {
            return new ItemData(material, displayName, lore, attributes, enchants, unbreakable, flags);
        }
    }
}