package com.mrbysco.angrymobs.registry.tweaks;

import com.mrbysco.angrymobs.AngryMobs;
import com.mrbysco.angrymobs.handler.goals.MobMeleeAttackGoal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;

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

	public MeleeAttackTweak(EntityType<? extends Mob> entity, int priority, double speedIn, float damage, boolean useLongMemory) {
		this(entity.getRegistryName(), priority, speedIn, damage, useLongMemory);
	}

	@Override
	public void adjust(Entity entity) {
		if (entity instanceof Mob mob) {

			mob.goalSelector.availableGoals.removeIf(goal -> goal.getGoal() instanceof PanicGoal);
			mob.goalSelector.availableGoals.forEach(goal -> {
				if (goal.getGoal() instanceof MeleeAttackGoal) {
					AngryMobs.LOGGER.info(String.format("Removing existing AI to apply the AI tweak of ID %s for entity %s", getEntityLocation(), getName()));
				}
			});
			mob.goalSelector.availableGoals.removeIf(goal -> goal.getGoal() instanceof MeleeAttackGoal);
			mob.goalSelector.addGoal(goalPriority, new MobMeleeAttackGoal(mob, speedIn, damage, useLongMemory));
		} else {
			AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity isn't valid for the tweak", getName(), getEntityLocation()));
		}
	}
}
