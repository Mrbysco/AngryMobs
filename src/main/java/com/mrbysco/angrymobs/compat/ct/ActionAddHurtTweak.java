package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.HurtByTargetTweak;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class ActionAddHurtTweak implements IRuntimeAction {
	public final HurtByTargetTweak hurtByTargetTweak;

	public ActionAddHurtTweak(EntityType<Entity> entity, int priority, boolean callReinforcements) {
		this.hurtByTargetTweak = new HurtByTargetTweak(BuiltInRegistries.ENTITY_TYPE.getKey(entity), priority, callReinforcements);
	}

	@Override
	public void apply() {
		AITweakRegistry.instance().registerTweak(hurtByTargetTweak);
	}

	@Override
	public String describe() {
		return String.format("Added %s tweak for Entity %s", hurtByTargetTweak.getName(), hurtByTargetTweak.getEntityLocation());
	}

	@Override
	public String systemName() {
		return "AngryMobs";
	}
}
