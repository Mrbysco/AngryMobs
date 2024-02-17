package com.mrbysco.angrymobs.handler;

import com.mrbysco.angrymobs.AngryMobs;
import com.mrbysco.angrymobs.config.attributes.AttributeConfigHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

public class AttributeHandler {
	public static void addEntityAttributes(EntityAttributeModificationEvent event) {
		//Load config
		AttributeConfigHandler.loadConfig();

		if (AttributeConfigHandler.additionMap.isEmpty()) return;

		for (EntityType<? extends LivingEntity> entityType : event.getTypes()) {
			ResourceLocation entityLocation = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
			if (entityLocation != null) {
				var values = AttributeConfigHandler.additionMap.getOrDefault(entityLocation.toString(), null);
				if (values != null) {
					ResourceLocation attributeLocation = ResourceLocation.tryParse(values.attribute());
					if (attributeLocation != null) {
						Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(attributeLocation);
						if (attribute != null) {
							if (!event.has(entityType, attribute)) {
								AngryMobs.LOGGER.info("Adding attribute: {} with value: {} to entity: {}", attributeLocation, values.value(), entityLocation);
								event.add(entityType, attribute, values.value());
							} else {
								AngryMobs.LOGGER.error("Attribute: {} already exists for entity: {}, ignoring entry!", attributeLocation, entityLocation);
							}
						} else {
							AngryMobs.LOGGER.error("Attribute: {} does not exist!", attributeLocation);
						}
					} else {
						AngryMobs.LOGGER.error("Attribute location {} doesn't match the format: modid:attribute_name", values.attribute());
					}
				}
			}
		}
	}
}
