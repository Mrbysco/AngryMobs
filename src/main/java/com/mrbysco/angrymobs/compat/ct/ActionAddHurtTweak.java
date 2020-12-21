package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.impl.entity.MCEntityType;
import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.HurtByTargetTweak;

public class ActionAddHurtTweak implements IRuntimeAction {
    public final HurtByTargetTweak hurtByTargetTweak;

    public ActionAddHurtTweak(MCEntityType entity, int priority, boolean callReinforcements) {
        this.hurtByTargetTweak = new HurtByTargetTweak(entity.getInternal().getRegistryName(), priority, callReinforcements);
    }

    @Override
    public void apply() {
        AITweakRegistry.instance().registerTweak(hurtByTargetTweak);
    }

    @Override
    public String describe() {
        return String.format("Added %s tweak for Entity %s", hurtByTargetTweak.getName(), hurtByTargetTweak.getEntityLocation());
    }
}
