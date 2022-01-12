package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import net.minecraft.world.entity.EntityType;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@ZenRegister
@Name("mods.angrymobs.AITweaks")
public class TweakCT {
    @Method
    public static void addMeleeAttackTweak(EntityType entity, int priority, double speedIn, float attackDamage, boolean useLongMemory) {
        CraftTweakerAPI.apply(new ActionAddMeleeTweak(entity, priority, speedIn, attackDamage, useLongMemory));
    }

    @Method
    public static void addProjectileAttackTweak(EntityType entity, EntityType projectileEntity, String soundLocation, int priority, float attackDamage, float velocity) {
        CraftTweakerAPI.apply(new ActionAddProjectileAttackTweak(entity, projectileEntity, soundLocation, priority, attackDamage, velocity));
    }

    @Method
    public static void addHurtByTargetTweak(EntityType entity, int priority, boolean callReinforcements) {
        CraftTweakerAPI.apply(new ActionAddHurtTweak(entity, priority, callReinforcements));
    }

    @Method
    public static void addLeapTweak(EntityType entity, int priority, float leapMotion) {
        CraftTweakerAPI.apply(new ActionAddLeapTweak(entity, priority, leapMotion));
    }

    @Method
    public static void addAttackNearestTweak(EntityType entity, EntityType targetEntity, int priority, boolean checkSight) {
        CraftTweakerAPI.apply(new ActionAddAttackNearestTweak(entity, targetEntity, priority, checkSight));
    }
}
