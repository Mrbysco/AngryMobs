//package com.mrbysco.angrymobs.compat.ct;
//
//import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
//import com.mrbysco.angrymobs.registry.TweakRegistry;
//import com.mrbysco.angrymobs.tweaks.ProjectileAttackTweak;
//import net.minecraft.core.Holder;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.resources.Identifier;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//
//public class ActionAddProjectileAttackTweak implements IRuntimeAction {
//	public final ProjectileAttackTweak projectileTweak;
//
//	public ActionAddProjectileAttackTweak(EntityType<Entity> entity, EntityType<Entity> projectileEntity, String soundLocation, int priority, float attackDamage, float velocity) {
//		this.projectileTweak = new ProjectileAttackTweak(BuiltInRegistries.ENTITY_TYPE.getKey(entity), BuiltInRegistries.ENTITY_TYPE.getKey(projectileEntity), BuiltInRegistries.SOUND_EVENT.get(Identifier.tryParse(soundLocation)),
//				priority, attackDamage, velocity);
//	}
//
//	@Override
//	public void apply() {
//		TweakRegistry.addCTTweak(Holder.direct(projectileTweak));
//	}
//
//	@Override
//	public String describe() {
//		return String.format("Added %s tweak for Entity %s", projectileTweak.generateId(), projectileTweak.entity());
//	}
//
//	@Override
//	public String systemName() {
//		return "AngryMobs";
//	}
//}
