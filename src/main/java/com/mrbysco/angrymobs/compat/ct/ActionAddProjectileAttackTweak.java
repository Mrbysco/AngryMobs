package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.impl.entity.MCEntityType;
import com.blamejared.crafttweaker.impl.util.MCResourceLocation;
import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.ProjectileAttackTweak;

public class ActionAddProjectileAttackTweak implements IRuntimeAction {
    public final ProjectileAttackTweak projectileTweak;

    public ActionAddProjectileAttackTweak(MCEntityType entity, MCEntityType projectileEntity, MCResourceLocation soundLocation, int priority, float attackDamage, float velocity) {
        this.projectileTweak = new ProjectileAttackTweak(entity.getInternal().getRegistryName(), projectileEntity.getInternal().getRegistryName(), soundLocation.getInternal(),
                priority, attackDamage, velocity);
    }

    @Override
    public void apply() {
        AITweakRegistry.instance().registerTweak(projectileTweak);
    }

    @Override
    public String describe() {
        return String.format("Added %s tweak for Entity %s", projectileTweak.getName(), projectileTweak.getEntityLocation());
    }
}
