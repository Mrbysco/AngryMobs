package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.impl.entity.MCEntityType;
import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.LeapAtTargetTweak;

public class ActionAddLeapTweak implements IRuntimeAction {
    public final LeapAtTargetTweak leapTweak;

    public ActionAddLeapTweak(MCEntityType entity, int priority, float leapMotion) {
        this.leapTweak = new LeapAtTargetTweak(entity.getInternal().getRegistryName(), priority, leapMotion);
    }

    @Override
    public void apply() {
        AITweakRegistry.instance().registerTweak(leapTweak);
    }

    @Override
    public String describe() {
        return String.format("Added %s tweak for Entity %s", leapTweak.getName(), leapTweak.getEntityLocation());
    }
}
