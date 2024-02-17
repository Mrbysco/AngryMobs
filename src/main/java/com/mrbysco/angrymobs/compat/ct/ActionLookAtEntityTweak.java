package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.LookAtEntityTweak;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class ActionLookAtEntityTweak implements IRuntimeAction {
	public final LookAtEntityTweak lookAtPlayerTweak;

	public ActionLookAtEntityTweak(EntityType<Entity> entity, EntityType<Entity> targetEntity, int priority, float lookDistance) {
		this.lookAtPlayerTweak = new LookAtEntityTweak(BuiltInRegistries.ENTITY_TYPE.getKey(entity), BuiltInRegistries.ENTITY_TYPE.getKey(targetEntity), priority, lookDistance);
	}

	@Override
	public void apply() {
		AITweakRegistry.instance().registerTweak(lookAtPlayerTweak);
	}

	@Override
	public String describe() {
		return String.format("Added %s tweak for Entity %s", lookAtPlayerTweak.getName(), lookAtPlayerTweak.getEntityLocation());
	}

	@Override
	public String systemName() {
		return "AngryMobs";
	}
}
