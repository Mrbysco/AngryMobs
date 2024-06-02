package com.mrbysco.angrymobs.handler;

import com.mrbysco.angrymobs.registry.TweakRegistry;
import com.mrbysco.angrymobs.tweaks.ITweak;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.List;

public class AIHandler {
	@SubscribeEvent
	public void onEntityCreation(EntityJoinLevelEvent event) {
		if (!event.getLevel().isClientSide()) {
			ResourceLocation registryName = BuiltInRegistries.ENTITY_TYPE.getKey(event.getEntity().getType());
			if (TweakRegistry.containsEntity(registryName)) {
				final List<Holder<? extends ITweak>> tweakList = TweakRegistry.getTweaksFromType(registryName);
				for (Holder<? extends ITweak> tweak : tweakList) {
					tweak.value().adjust(event.getEntity(), getTweakID(tweak));
				}
			}
		}
	}

	/**
	 * Get the tweak ID from the holder or generate a new one
	 *
	 * @param tweakHolder The holder of the tweak
	 * @return The tweak ID
	 */
	private String getTweakID(Holder<? extends ITweak> tweakHolder) {
		var unwrapped = tweakHolder.unwrapKey();
		return unwrapped.map(resourceKey -> resourceKey.location().toString()).orElseGet(() -> tweakHolder.value().generateId());
	}
}