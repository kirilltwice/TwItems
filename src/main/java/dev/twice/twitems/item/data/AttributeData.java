package dev.twice.twitems.item.data;

import dev.twice.twitems.utils.CacheUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.EquipmentSlot;

public record AttributeData(
        Attribute attribute,
        double value,
        EquipmentSlot slot
) {

    public static AttributeData of(Attribute attribute, double value, EquipmentSlot slot) {
        return new AttributeData(attribute, value, slot);
    }

    public static AttributeData parse(String attributeString) {
        var parts = attributeString.split(";");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid attribute format: " + attributeString);
        }

        var attribute = CacheUtil.getAttribute(parts[0]);
        if (attribute == null) {
            throw new IllegalArgumentException("Unknown attribute: " + parts[0]);
        }

        var value = Double.parseDouble(parts[1]);

        var slot = CacheUtil.getSlot(parts[2]);
        if (slot == null) {
            throw new IllegalArgumentException("Unknown slot: " + parts[2]);
        }

        return new AttributeData(attribute, value, slot);
    }
}