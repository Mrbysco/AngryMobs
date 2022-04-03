package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.AttackNearestTweak;
import net.minecraft.world.entity.EntityType;

public class ActionAddAttackNearestTweak implements IRuntimeAction {
	public final AttackNearestTweak attackNearestTweak;

	public ActionAddAttackNearestTweak(EntityType entity, EntityType targetEntity, int priority, boolean checkSight) {
		this.attackNearestTweak = new AttackNearestTweak(entity.getRegistryName(), targetEntity.getRegistryName(), priority, checkSight);
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
