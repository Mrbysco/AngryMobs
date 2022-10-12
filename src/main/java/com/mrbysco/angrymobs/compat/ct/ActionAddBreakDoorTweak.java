package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.BreakDoorTweak;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class ActionAddBreakDoorTweak implements IRuntimeAction {
	public final BreakDoorTweak breakDoorTweak;

	public ActionAddBreakDoorTweak(EntityType entity, int priority, int difficulty) {
		this.breakDoorTweak = new BreakDoorTweak(ForgeRegistries.ENTITY_TYPES.getKey(entity), priority, difficulty);
	}

	@Override
	public void apply() {
		AITweakRegistry.instance().registerTweak(breakDoorTweak);
	}

	@Override
	public String describe() {
		return String.format("Added %s tweak for Entity %s", breakDoorTweak.getName(), breakDoorTweak.getEntityLocation());
	}
}
