package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.impl.entity.MCEntityType;
import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.AttackNearestTweak;

public class ActionAddAttackNearestTweak implements IRuntimeAction {
    public final AttackNearestTweak attackNearestTweak;

    public ActionAddAttackNearestTweak(MCEntityType entity, MCEntityType targetEntity, int priority, boolean checkSight) {
        this.attackNearestTweak = new AttackNearestTweak(entity.getInternal().getRegistryName(), targetEntity.getInternal().getRegistryName(), priority, checkSight);
    }

    @Override
    public void apply() {
        AITweakRegistry.instance().registerTweak(attackNearestTweak);
    }

    @Override
    public String describe() {
        return String.format("Added %s tweak for Entity %s", attackNearestTweak.getName(), attackNearestTweak.getEntityLocation());
    }
}
