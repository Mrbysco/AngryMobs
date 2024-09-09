package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.mrbysco.angrymobs.registry.TweakRegistry;
import com.mrbysco.angrymobs.tweaks.HurtByTargetTweak;
import net.minecraft.core.Holder;
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
		TweakRegistry.addCTTweak(Holder.direct(hurtByTargetTweak));
	}

	@Override
	public String describe() {
		return String.format("Added %s tweak for Entity %s", hurtByTargetTweak.generateId(), hurtByTargetTweak.entity());
	}

	@Override
	public String systemName() {
		return "AngryMobs";
	}
}
