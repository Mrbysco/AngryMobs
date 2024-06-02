package com.mrbysco.angrymobs.tweaks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public interface ITweak {
	/*
	 * What action happens when the event is triggered
	 */
	void adjust(Entity entity, String id);

	/*
	 * @return Generate a unique ID for the tweak if needed
	 * This is to allow code based tweaks to be added to the registry
	 */
	String generateId();

	/*
	 * @return ResourceLocation of the entity being tweaked
	 */
	ResourceLocation entity();
}
