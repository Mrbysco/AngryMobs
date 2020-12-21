package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.impl.entity.MCEntityType;
import com.blamejared.crafttweaker.impl.util.MCResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.angrymobs.Tweaks")
public class TweakCT {
    @ZenCodeType.Method
    public static void addMeleeAttackTweak(MCEntityType entity, int priority, double speedIn, float damage, boolean useLongMemory) {
        CraftTweakerAPI.apply(new ActionAddMeleeTweak(entity, priority, speedIn, damage, useLongMemory));
    }

    @ZenCodeType.Method
    public static void addProjectileAttackTweak(MCEntityType entity, MCEntityType projectileEntity, MCResourceLocation soundLocation, int priority, float attackDamage, float velocity) {
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
