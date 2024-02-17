package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.BreakDoorTweak;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class ActionAddBreakDoorTweak implements IRuntimeAction {
	public final BreakDoorTweak breakDoorTweak;

	public ActionAddBreakDoorTweak(EntityType<Entity> entity, int priority, int difficulty) {
		this.breakDoorTweak = new BreakDoorTweak(BuiltInRegistries.ENTITY_TYPE.getKey(entity), priority, difficulty);
	}

	@Override
	public void apply() {
		AITweakRegistry.instance().registerTweak(breakDoorTweak);
	}

	@Override
	public String describe() {
		return String.format("Added %s tweak for Entity %s", breakDoorTweak.getName(), breakDoorTweak.getEntityLocation());
	}

	@Override
	public String systemName() {
		return "AngryMobs";
	}
}
