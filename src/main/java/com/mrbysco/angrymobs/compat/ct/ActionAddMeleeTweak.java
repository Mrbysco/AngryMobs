package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.MeleeAttackTweak;
import net.minecraft.world.entity.EntityType;

public class ActionAddMeleeTweak implements IRuntimeAction {
    public final MeleeAttackTweak meleeTweak;

    public ActionAddMeleeTweak(EntityType entity, int priority, double speedIn, float damage, boolean useLongMemory) {
        this.meleeTweak = new MeleeAttackTweak(entity.getRegistryName(), priority, speedIn, damage, useLongMemory);
    }

    @Override
    public void apply() {
        AITweakRegistry.instance().registerTweak(meleeTweak);
    }

    @Override
    public String describe() {
        return String.format("Added %s tweak for Entity %s", meleeTweak.getName(), meleeTweak.getEntityLocation());
    }
}
