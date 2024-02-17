package com.mrbysco.angrymobs.handler;

import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.ITweak;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.List;

public class AIHandler {
	@SubscribeEvent
	public void onEntityCreation(EntityJoinLevelEvent event) {
		if (!event.getLevel().isClientSide()) {
			AITweakRegistry tweakRegistry = AITweakRegistry.instance();
			ResourceLocation registryName = BuiltInRegistries.ENTITY_TYPE.getKey(event.getEntity().getType());
			if (tweakRegistry.containsEntity(registryName)) {
				final List<ITweak> tweakList = tweakRegistry.getTweaksFromType(registryName);
				for (ITweak tweak : tweakList) {
					tweak.adjust(event.getEntity());
				}
			}
		}
	}
}