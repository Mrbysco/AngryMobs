package com.mrbysco.angrymobs.registry.tweaks;

import com.mrbysco.angrymobs.AngryMobs;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.util.ResourceLocation;

public class LeapAtTargetTweak extends BaseTweak {
    protected final int goalPriority;
    protected final float leapMotion;

    public LeapAtTargetTweak(ResourceLocation entity, int priority, float leapMotion) {
        super("leap_at_target", entity);
        this.goalPriority = priority;
        this.leapMotion = leapMotion;
    }

    public LeapAtTargetTweak(EntityType<? extends CreatureEntity> entity, int priority, float leapMotion) {
        this(entity.getRegistryName(), priority, leapMotion);
    }

    @Override
    public void adjust(Entity entity) {
        if(entity instanceof MobEntity) {
            MobEntity mob = (MobEntity) entity;
            mob.goalSelector.goals.forEach(goal -> {
                if(goal.getGoal() instanceof LeapAtTargetGoal) {
                    AngryMobs.LOGGER.info(String.format("Overriding existing AI goal for entity %s using tweak ID %s", getEntityLocation(), getName()));
                }
            });
            mob.goalSelector.goals.removeIf(goal -> goal.getGoal() instanceof LeapAtTargetGoal);
            mob.goalSelector.addGoal(goalPriority, new LeapAtTargetGoal(mob, leapMotion));
        } else {
            AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity isn't valid for the tweak", getName(), getEntityLocation()));
        }
    }
}
