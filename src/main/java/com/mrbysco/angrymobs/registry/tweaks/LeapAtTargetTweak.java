package com.mrbysco.angrymobs.registry.tweaks;

import com.mrbysco.angrymobs.AngryMobs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;

public class LeapAtTargetTweak extends BaseTweak {
	protected final int goalPriority;
	protected final float leapMotion;

	public LeapAtTargetTweak(ResourceLocation entity, int priority, float leapMotion) {
		super("leap_at_target", entity);
		this.goalPriority = priority;
		this.leapMotion = leapMotion;
	}

	public LeapAtTargetTweak(EntityType<? extends PathfinderMob> entity, int priority, float leapMotion) {
		this(entity.getRegistryName(), priority, leapMotion);
	}

	@Override
	public void adjust(Entity entity) {
		if (entity instanceof Mob mob) {
			mob.goalSelector.availableGoals.forEach(goal -> {
				if (goal.getGoal() instanceof LeapAtTargetGoal) {
					AngryMobs.LOGGER.info(String.format("Overriding existing AI goal for entity %s using tweak ID %s", getEntityLocation(), getName()));
				}
			});
			mob.goalSelector.availableGoals.removeIf(goal -> goal.getGoal() instanceof LeapAtTargetGoal);
			mob.goalSelector.addGoal(goalPriority, new LeapAtTargetGoal(mob, leapMotion));
		} else {
			AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity isn't valid for the tweak", getName(), getEntityLocation()));
		}
	}
}
