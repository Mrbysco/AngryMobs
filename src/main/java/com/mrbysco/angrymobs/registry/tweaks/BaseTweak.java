package com.mrbysco.angrymobs.registry.tweaks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class BaseTweak implements ITweak {
	private final String uniqueID;
	protected final ResourceLocation entityLocation;

	public BaseTweak(String ID, ResourceLocation entityLocation) {
		this.uniqueID = entityLocation.toString() + "_" + ID;
		this.entityLocation = entityLocation;
	}

	@Override
	public void adjust(Entity entity) {

	}

	@Override
	public String getName() {
		return this.uniqueID;
	}

	public ResourceLocation getEntityLocation() {
		return entityLocation;
	}
}
