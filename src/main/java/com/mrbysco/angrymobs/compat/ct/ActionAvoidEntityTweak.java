package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.AvoidEntityTweak;
import net.minecraft.world.entity.EntityType;

public class ActionAvoidEntityTweak implements IRuntimeAction {
	public final AvoidEntityTweak attackNearestTweak;

	public ActionAvoidEntityTweak(EntityType entity, EntityType targetEntity, int priority, float maxDistance, double walkSpeedModifier, double sprintSpeedModifier) {
		this.attackNearestTweak = new AvoidEntityTweak(entity.getRegistryName(), targetEntity.getRegistryName(), priority, maxDistance, walkSpeedModifier, sprintSpeedModifier);
	}

	@Override
	public void apply() {
		AITweakRegistry.instance().registerTweak(attackNearestTweak);
	}

	@Override
	public String describe() {
		return String.format("Added %s tweak for Entity %s", attackNearestTweak.getName(), attackNearestTweak.getEntityLocation());
	}
}
