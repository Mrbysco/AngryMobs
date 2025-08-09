//package com.mrbysco.angrymobs.compat.ct;
//
//import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
//import com.mrbysco.angrymobs.registry.TweakRegistry;
//import com.mrbysco.angrymobs.tweaks.MeleeAttackTweak;
//import net.minecraft.core.Holder;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//
//public class ActionAddMeleeTweak implements IRuntimeAction {
//	public final MeleeAttackTweak meleeTweak;
//
//	public ActionAddMeleeTweak(EntityType<Entity> entity, int priority, double speedIn, float damage, float knockback, boolean useLongMemory) {
//		this.meleeTweak = new MeleeAttackTweak(BuiltInRegistries.ENTITY_TYPE.getKey(entity), priority, speedIn, damage, knockback, useLongMemory);
//	}
//
//	@Override
//	public void apply() {
//		TweakRegistry.addCTTweak(Holder.direct(meleeTweak));
//	}
//
//	@Override
//	public String describe() {
//		return String.format("Added %s tweak for Entity %s", meleeTweak.generateId(), meleeTweak.entity());
//	}
//
//	@Override
//	public String systemName() {
//		return "AngryMobs";
//	}
//}
