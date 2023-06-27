package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.AttackNearestTweak;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class ActionAddAttackNearestTweak implements IRuntimeAction {
	public final AttackNearestTweak attackNearestTweak;

	public ActionAddAttackNearestTweak(EntityType<Entity> entity, EntityType<Entity> targetEntity, int priority, boolean checkSight) {
		this.attackNearestTweak = new AttackNearestTweak(ForgeRegistries.ENTITY_TYPES.getKey(entity), ForgeRegistries.ENTITY_TYPES.getKey(targetEntity), priority, checkSight);
	}

	@Override
	public void apply() {
		AITweakRegistry.instance().registerTweak(attackNearestTweak);
	}

	@Override
	public String describe() {
		return String.format("Added %s tweak for Entity %s", attackNearestTweak.getName(), attackNearestTweak.getEntityLocation());
	}

	@Override
	public String systemName() {
		return "AngryMobs";
	}
}
