package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.impl.entity.MCEntityType;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.angrymobs.AITweaks")
public class TweakCT {
    @ZenCodeType.Method
    public static void addMeleeAttackTweak(MCEntityType entity, int priority, double speedIn, float attackDamage, boolean useLongMemory) {
        CraftTweakerAPI.apply(new ActionAddMeleeTweak(entity, priority, speedIn, attackDamage, useLongMemory));
    }

    @ZenCodeType.Method
    public static void addProjectileAttackTweak(MCEntityType entity, MCEntityType projectileEntity, String soundLocation, int priority, float attackDamage, float velocity) {
        CraftTweakerAPI.apply(new ActionAddProjectileAttackTweak(entity, projectileEntity, soundLocation, priority, attackDamage, velocity));
    }

    @ZenCodeType.Method
    public static void addHurtByTargetTweak(MCEntityType entity, int priority, boolean callReinforcements) {
        CraftTweakerAPI.apply(new ActionAddHurtTweak(entity, priority, callReinforcements));
    }

    @ZenCodeType.Method
    public static void addLeapTweak(MCEntityType entity, int priority, float leapMotion) {
        CraftTweakerAPI.apply(new ActionAddLeapTweak(entity, priority, leapMotion));
    }

    @ZenCodeType.Method
    public static void addAttackNearestTweak(MCEntityType entity, MCEntityType targetEntity, int priority, boolean checkSight) {
        CraftTweakerAPI.apply(new ActionAddAttackNearestTweak(entity, targetEntity, priority, checkSight));
    }
}
