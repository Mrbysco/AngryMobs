package com.mrbysco.angrymobs.tweaks;

import net.minecraft.resources.Identifier;
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
	 * @return Identifier of the entity being tweaked
	 */
	Identifier entity();
}
