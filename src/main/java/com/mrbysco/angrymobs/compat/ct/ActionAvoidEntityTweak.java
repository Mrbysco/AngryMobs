package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.mrbysco.angrymobs.registry.TweakRegistry;
import com.mrbysco.angrymobs.tweaks.AvoidEntityTweak;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class ActionAvoidEntityTweak implements IRuntimeAction {
	public final AvoidEntityTweak attackNearestTweak;

	public ActionAvoidEntityTweak(EntityType<Entity> entity, EntityType<Entity> targetEntity, int priority, float maxDistance, double walkSpeedModifier, double sprintSpeedModifier) {
		this.attackNearestTweak = new AvoidEntityTweak(BuiltInRegistries.ENTITY_TYPE.getKey(entity), BuiltInRegistries.ENTITY_TYPE.getKey(targetEntity), priority, maxDistance, walkSpeedModifier, sprintSpeedModifier);
	}

	@Override
	public void apply() {
		TweakRegistry.addTweak(Holder.direct(attackNearestTweak));
	}

	@Override
	public String describe() {
		return String.format("Added %s tweak for Entity %s", attackNearestTweak.generateId(), attackNearestTweak.entity());
	}

	@Override
	public String systemName() {
		return "AngryMobs";
	}
}
