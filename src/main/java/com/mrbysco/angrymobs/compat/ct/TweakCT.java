package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Name("mods.angrymobs.AITweaks")
public class TweakCT {
	@Method
	public static void addMeleeAttackTweak(EntityType<Entity> entity, int priority, double speedIn, float attackDamage, boolean useLongMemory) {
		CraftTweakerAPI.apply(new ActionAddMeleeTweak(entity, priority, speedIn, attackDamage, useLongMemory));
	}

	@Method
	public static void addProjectileAttackTweak(EntityType<Entity> entity, EntityType<Entity> projectileEntity, String soundLocation, int priority, float attackDamage, float velocity) {
		CraftTweakerAPI.apply(new ActionAddProjectileAttackTweak(entity, projectileEntity, soundLocation, priority, attackDamage, velocity));
	}

	@Method
	public static void addHurtByTargetTweak(EntityType<Entity> entity, int priority, boolean callReinforcements) {
		CraftTweakerAPI.apply(new ActionAddHurtTweak(entity, priority, callReinforcements));
	}

	@Method
	public static void addLeapTweak(EntityType<Entity> entity, int priority, float leapMotion) {
		CraftTweakerAPI.apply(new ActionAddLeapTweak(entity, priority, leapMotion));
	}

	@Method
	public static void addAttackNearestTweak(EntityType<Entity> entity, EntityType<Entity> targetEntity, int priority, boolean checkSight) {
		CraftTweakerAPI.apply(new ActionAddAttackNearestTweak(entity, targetEntity, priority, checkSight));
	}

	@Method
	public static void addAvoidEntityTweak(EntityType<Entity> entity, EntityType<Entity> targetEntity, int priority, float maxDistance, double walkSpeedModifier, double sprintSpeedModifier) {
		CraftTweakerAPI.apply(new ActionAvoidEntityTweak(entity, targetEntity, priority, maxDistance, walkSpeedModifier, sprintSpeedModifier));
	}

	@Method
	public static void addLookAtEntityTweak(EntityType<Entity> entity, EntityType<Entity> targetEntity, int priority, float lookDistance) {
		CraftTweakerAPI.apply(new ActionLookAtEntityTweak(entity, targetEntity, priority, lookDistance));
	}

	@Method
	public static void addBreakDoorTweak(EntityType entity, int priority, int difficulty) {
		CraftTweakerAPI.apply(new ActionAddBreakDoorTweak(entity, priority, difficulty));
	}
}
