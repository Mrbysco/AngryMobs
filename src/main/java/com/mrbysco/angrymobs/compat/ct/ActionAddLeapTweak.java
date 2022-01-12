package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.LeapAtTargetTweak;
import net.minecraft.world.entity.EntityType;

public class ActionAddLeapTweak implements IRuntimeAction {
    public final LeapAtTargetTweak leapTweak;

    public ActionAddLeapTweak(EntityType entity, int priority, float leapMotion) {
        this.leapTweak = new LeapAtTargetTweak(entity.getRegistryName(), priority, leapMotion);
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
