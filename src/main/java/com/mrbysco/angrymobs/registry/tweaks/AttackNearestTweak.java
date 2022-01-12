package com.mrbysco.angrymobs.registry.tweaks;

import com.mrbysco.angrymobs.AngryMobs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
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

    public AttackNearestTweak(EntityType<? extends Mob> entity, EntityType<? extends LivingEntity> target, int priority, boolean checkSight) {
        this(entity.getRegistryName(), target.getRegistryName(), priority, checkSight);
    }

    @Override
    public void adjust(Entity entity) {
        if(entity instanceof Mob mob) {
            if(canHaveGoal(mob)) {
                if(targetEntityLocation.toString().equals("minecraft:player")) {
                    mob.targetSelector.addGoal(goalPriority, new NearestAttackableTargetGoal<>(mob, Player.class, checkSight));
                } else {
                    Entity targetEntity = ForgeRegistries.ENTITIES.getValue(targetEntityLocation).create(entity.level);
                    if(targetEntity instanceof LivingEntity) {
                        Class<? extends LivingEntity> entityClass = ((LivingEntity)targetEntity).getClass();
                        mob.targetSelector.addGoal(goalPriority, new NearestAttackableTargetGoal<>(mob, entityClass, checkSight));
                        targetEntity.discard();
                    } else {
                        AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Target entity isn't valid for the tweak", getName(), getEntityLocation()));
                    }
                }
            }
        } else {
            AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity isn't valid for the tweak", getName(), getEntityLocation()));
        }
    }

    public boolean canHaveGoal(Mob mob) {
        for(Goal goal : mob.goalSelector.availableGoals) {
            if(goal instanceof NearestAttackableTargetGoal nearestAttackable) {
                if(targetEntityLocation.toString().equals("minecraft:player")) {
                    if(nearestAttackable.targetType == Player.class) {
                        AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity already has given AI goal", getName(), getEntityLocation()));
                        return false;
                    }
                } else {
                    Entity targetEntity = ForgeRegistries.ENTITIES.getValue(targetEntityLocation).create(mob.level);
                    if(targetEntity instanceof LivingEntity) {
                        Class<? extends LivingEntity> entityClass = ((LivingEntity) targetEntity).getClass();
                        if(nearestAttackable.targetType == entityClass) {
                            AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity already has given AI goal", getName(), getEntityLocation()));
                            targetEntity.discard();
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
