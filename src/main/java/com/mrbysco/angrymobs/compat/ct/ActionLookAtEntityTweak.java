package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.LookAtEntityTweak;
import net.minecraft.world.entity.EntityType;

public class ActionLookAtEntityTweak implements IRuntimeAction {
	public final LookAtEntityTweak lookAtPlayerTweak;

	public ActionLookAtEntityTweak(EntityType entity, EntityType targetEntity, int priority, float lookDistance) {
		this.lookAtPlayerTweak = new LookAtEntityTweak(entity.getRegistryName(), targetEntity.getRegistryName(), priority, lookDistance);
	}

	@Override
	public void apply() {
		AITweakRegistry.instance().registerTweak(lookAtPlayerTweak);
	}

	@Override
	public String describe() {
		return String.format("Added %s tweak for Entity %s", lookAtPlayerTweak.getName(), lookAtPlayerTweak.getEntityLocation());
	}
}
