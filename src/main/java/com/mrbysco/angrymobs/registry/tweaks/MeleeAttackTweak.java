package com.mrbysco.angrymobs.registry.tweaks;

import com.mrbysco.angrymobs.AngryMobs;
import com.mrbysco.angrymobs.handler.goals.MobMeleeAttackGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.util.ResourceLocation;

public class MeleeAttackTweak extends BaseTweak {
    protected final int goalPriority;
    protected final double speedIn;
    protected final float damage;
    protected final boolean useLongMemory;

    public MeleeAttackTweak(ResourceLocation entity, int priority, double speedIn, float damage, boolean useLongMemory) {
        super("melee_attack", entity);
        this.goalPriority = priority;
        this.speedIn = speedIn;
        this.damage = damage;
        this.useLongMemory = useLongMemory;
    }

    public MeleeAttackTweak(EntityType<? extends MobEntity> entity, int priority, double speedIn, float damage, boolean useLongMemory) {
        this(entity.getRegistryName(), priority, speedIn, damage, useLongMemory);
    }

    @Override
    public void adjust(Entity entity) {
        if(entity instanceof MobEntity) {
            MobEntity mob = (MobEntity) entity;

            mob.goalSelector.goals.removeIf(goal -> goal.getGoal() instanceof PanicGoal);
            mob.goalSelector.goals.forEach(goal -> {
                if(goal.getGoal() instanceof MeleeAttackGoal) {
                    AngryMobs.LOGGER.info(String.format("Removing existing AI to apply the AI tweak of ID %s for entity %s", getEntityLocation(), getName()));
                }
            });
            mob.goalSelector.goals.removeIf(goal -> goal.getGoal() instanceof MeleeAttackGoal);
            mob.goalSelector.addGoal(goalPriority, new MobMeleeAttackGoal(mob, speedIn, damage, useLongMemory));
        } else {
            AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity isn't valid for the tweak", getName(), getEntityLocation()));
        }
    }
}
