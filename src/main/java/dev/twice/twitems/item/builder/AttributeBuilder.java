package dev.twice.twitems.item.builder;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class AttributeBuilder {

    public void applyAttributes(ItemMeta itemMeta, List<String> attributesList) {
        for (String attributeStr : attributesList) {
            var parts = attributeStr.split(";");
            if (parts.length < 3) continue;

            try {
                var attribute = Attribute.valueOf(parts[0]);
                var value = Double.parseDouble(parts[1]);
                var slot = EquipmentSlot.valueOf(parts[2]);

                var modifier = new AttributeModifier(
                        UUID.randomUUID(),
                        attribute.getKey().getKey(),
                        value,
                        AttributeModifier.Operation.ADD_NUMBER,
                        slot
                );

                itemMeta.addAttributeModifier(attribute, modifier);
            } catch (Exception ignored) {}
        }
    }
}