package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.mrbysco.angrymobs.registry.TweakRegistry;
import com.mrbysco.angrymobs.tweaks.AttackNearestTweak;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class ActionAddAttackNearestTweak implements IRuntimeAction {
	public final AttackNearestTweak attackNearestTweak;

	public ActionAddAttackNearestTweak(EntityType<Entity> entity, EntityType<Entity> targetEntity, int priority, boolean checkSight) {
		this.attackNearestTweak = new AttackNearestTweak(BuiltInRegistries.ENTITY_TYPE.getKey(entity), BuiltInRegistries.ENTITY_TYPE.getKey(targetEntity), priority, checkSight);
	}

	@Override
	public void apply() {
		TweakRegistry.addCTTweak(Holder.direct(attackNearestTweak));
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
