package com.mrbysco.angrymobs.registry.tweaks;

import com.mrbysco.angrymobs.AngryMobs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class AttackNearestTweak extends BaseTweak {
    protected final ResourceLocation targetEntityLocation;
    protected final int goalPriority;
    protected final boolean checkSight;

    public AttackNearestTweak(ResourceLocation entity, ResourceLocation target, int priority, boolean checkSight) {
        super("attack_nearest_" + target.getNamespace(), entity);
        this.targetEntityLocation = target;
        this.goalPriority = priority;
        this.checkSight = checkSight;
    }

    public AttackNearestTweak(EntityType<? extends MobEntity> entity, EntityType<? extends LivingEntity> target, int priority, boolean checkSight) {
        this(entity.getRegistryName(), target.getRegistryName(), priority, checkSight);
    }

    @Override
    public void adjust(Entity entity) {
        if(entity instanceof MobEntity) {
            MobEntity mob = (MobEntity) entity;
            if(canHaveGoal(mob)) {
                if(targetEntityLocation.toString().equals("minecraft:player")) {
                    mob.targetSelector.addGoal(goalPriority, new NearestAttackableTargetGoal<>(mob, PlayerEntity.class, checkSight));
                } else {
                    Entity targetEntity = ForgeRegistries.ENTITIES.getValue(targetEntityLocation).create(entity.world);
                    if(targetEntity instanceof LivingEntity) {
                        Class<? extends LivingEntity> entityClass = ((LivingEntity)targetEntity).getClass();
                        mob.targetSelector.addGoal(goalPriority, new NearestAttackableTargetGoal<>(mob, entityClass, checkSight));
                        targetEntity.remove();
                    } else {
                        AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Target entity isn't valid for the tweak", getName(), getEntityLocation()));
                    }
                }
            }
        } else {
            AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity isn't valid for the tweak", getName(), getEntityLocation()));
        }
    }

    public boolean canHaveGoal(MobEntity mob) {
        for(Goal goal : mob.goalSelector.goals) {
            if(goal instanceof NearestAttackableTargetGoal) {
                NearestAttackableTargetGoal nearestAttackable = (NearestAttackableTargetGoal)goal;
                if(targetEntityLocation.toString().equals("minecraft:player")) {
                    if(nearestAttackable.targetClass == PlayerEntity.class) {
                        AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity already has given AI goal", getName(), getEntityLocation()));
                        return false;
                    }
                } else {
                    Entity targetEntity = ForgeRegistries.ENTITIES.getValue(targetEntityLocation).create(mob.world);
                    if(targetEntity instanceof LivingEntity) {
                        Class<? extends LivingEntity> entityClass = ((LivingEntity) targetEntity).getClass();
                        if(nearestAttackable.targetClass == entityClass) {
                            AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity already has given AI goal", getName(), getEntityLocation()));
                            targetEntity.remove();
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
