package com.mrbysco.angrymobs.registry.tweaks;

import com.mrbysco.angrymobs.AngryMobs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;

public class AvoidEntityTweak extends BaseTweak {
	protected final ResourceLocation targetEntityLocation;
	protected final int goalPriority;
	protected final float maxDistance;
	protected final double walkSpeedModifier;
	protected final double sprintSpeedModifier;

	public AvoidEntityTweak(ResourceLocation entity, ResourceLocation target, int priority, float maxDistance, double walkSpeedModifier, double sprintSpeedModifier) {
		super("avoid_entity_" + target.getNamespace(), entity);
		this.targetEntityLocation = target;
		this.goalPriority = priority;
		this.maxDistance = maxDistance;
		this.walkSpeedModifier = walkSpeedModifier;
		this.sprintSpeedModifier = sprintSpeedModifier;
	}

	@Override
	public void adjust(Entity entity) {
		if (entity instanceof PathfinderMob pathfinderMob) {
			if (canHaveGoal(pathfinderMob)) {
				if (targetEntityLocation.toString().equals("minecraft:player")) {
					pathfinderMob.targetSelector.addGoal(goalPriority, new AvoidEntityGoal<>(pathfinderMob, Player.class, maxDistance, walkSpeedModifier, sprintSpeedModifier));
				} else {
					Entity targetEntity = ForgeRegistries.ENTITY_TYPES.getValue(targetEntityLocation).create(entity.level);
					if (targetEntity instanceof LivingEntity) {
						Class<? extends LivingEntity> entityClass = ((LivingEntity) targetEntity).getClass();
						pathfinderMob.targetSelector.addGoal(goalPriority, new AvoidEntityGoal<>(pathfinderMob, entityClass, maxDistance, walkSpeedModifier, sprintSpeedModifier));
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

	public boolean canHaveGoal(PathfinderMob pathfinderMob) {
		for (Goal goal : pathfinderMob.goalSelector.availableGoals) {
			if (goal instanceof AvoidEntityGoal avoidEntity) {
				if (targetEntityLocation.toString().equals("minecraft:player")) {
					if (avoidEntity.avoidClass == Player.class) {
						AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity already has given AI goal", getName(), getEntityLocation()));
						return false;
					}
				} else {
					Entity targetEntity = ForgeRegistries.ENTITY_TYPES.getValue(targetEntityLocation).create(pathfinderMob.level);
					if (targetEntity instanceof LivingEntity) {
						Class<? extends LivingEntity> entityClass = ((LivingEntity) targetEntity).getClass();
						if (avoidEntity.avoidClass == entityClass) {
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
