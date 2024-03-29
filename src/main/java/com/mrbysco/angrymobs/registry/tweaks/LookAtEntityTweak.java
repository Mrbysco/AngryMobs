package com.mrbysco.angrymobs.registry.tweaks;

import com.mrbysco.angrymobs.AngryMobs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;

public class LookAtEntityTweak extends BaseTweak {
	protected final ResourceLocation targetEntityLocation;
	protected final int goalPriority;
	protected final float lookDistance;

	public LookAtEntityTweak(ResourceLocation entity, ResourceLocation target, int priority, float lookDistance) {
		super("look_at_" + target.getNamespace(), entity);
		this.targetEntityLocation = target;
		this.goalPriority = priority;
		this.lookDistance = lookDistance;
	}

	@Override
	public void adjust(Entity entity) {
		if (entity instanceof Mob mob) {
			if (canHaveGoal(mob)) {
				if (targetEntityLocation.toString().equals("minecraft:player")) {
					mob.targetSelector.addGoal(goalPriority, new LookAtPlayerGoal(mob, Player.class, lookDistance));
				} else {
					Entity targetEntity = ForgeRegistries.ENTITY_TYPES.getValue(targetEntityLocation).create(entity.level());
					if (targetEntity instanceof LivingEntity) {
						Class<? extends LivingEntity> entityClass = ((LivingEntity) targetEntity).getClass();
						mob.targetSelector.addGoal(goalPriority, new LookAtPlayerGoal(mob, entityClass, lookDistance));
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
		for (Goal goal : mob.goalSelector.availableGoals) {
			if (goal instanceof LookAtPlayerGoal lookAtPlayerGoal) {
				if (targetEntityLocation.toString().equals("minecraft:player")) {
					if (lookAtPlayerGoal.lookAtType == Player.class) {
						AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity already has given AI goal", getName(), getEntityLocation()));
						return false;
					}
				} else {
					Entity targetEntity = ForgeRegistries.ENTITY_TYPES.getValue(targetEntityLocation).create(mob.level());
					if (targetEntity instanceof LivingEntity) {
						Class<? extends LivingEntity> entityClass = ((LivingEntity) targetEntity).getClass();
						if (lookAtPlayerGoal.lookAtType == entityClass) {
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
