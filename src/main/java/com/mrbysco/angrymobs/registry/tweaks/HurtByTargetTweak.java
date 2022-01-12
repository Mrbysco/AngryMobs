package com.mrbysco.angrymobs.registry.tweaks;

import com.mrbysco.angrymobs.AngryMobs;
import com.mrbysco.angrymobs.handler.goals.MobHurtByTargetGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.ResourceLocation;

public class HurtByTargetTweak extends BaseTweak {
    protected final int goalPriority;
    protected final boolean callReinforcements;

    public HurtByTargetTweak(ResourceLocation entity,  int priority, boolean callReinforcements) {
        super("hurt_by_target", entity);
        this.goalPriority = priority;
        this.callReinforcements = callReinforcements;
    }

    public HurtByTargetTweak(EntityType<? extends MobEntity> entity, int priority, boolean callReinforcements) {
        this(entity.getRegistryName(), priority, callReinforcements);
    }

    @Override
    public void adjust(Entity entity) {
        if(entity instanceof MobEntity) {
            MobEntity mob = (MobEntity) entity;
            if(canHaveGoal(mob)) {
                MobHurtByTargetGoal hurtGoal = new MobHurtByTargetGoal(mob);
                if(callReinforcements) {
                    hurtGoal.setCallsForHelp();
                }
                mob.targetSelector.addGoal(goalPriority, hurtGoal);
            }
        } else {
            AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity isn't valid for the tweak", getName(), getEntityLocation()));
        }
    }

    public boolean canHaveGoal(MobEntity mob) {
        for(Goal goal : mob.goalSelector.availableGoals) {
            if(goal instanceof MobHurtByTargetGoal) {
                AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity already has given AI goal", getName(), getEntityLocation()));
                return false;
            }
        }
        return true;
    }
}
