package com.mrbysco.angrymobs.handler;

import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.ITweak;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class AIHandler {
	@SubscribeEvent
	public void onEntityCreation(EntityJoinLevelEvent event) {
		if (!event.getLevel().isClientSide()) {
			AITweakRegistry tweakRegistry = AITweakRegistry.instance();
			ResourceLocation registryName = ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType());
			if (tweakRegistry.containsEntity(registryName)) {
				final List<ITweak> tweakList = tweakRegistry.getTweaksFromType(registryName);
				for (ITweak tweak : tweakList) {
					tweak.adjust(event.getEntity());
				}
			}
		}
	}
}