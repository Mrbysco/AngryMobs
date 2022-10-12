package com.mrbysco.angrymobs.registry.tweaks;

import com.mrbysco.angrymobs.AngryMobs;
import com.mrbysco.angrymobs.handler.goals.MobHurtByTargetGoal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraftforge.registries.ForgeRegistries;

public class HurtByTargetTweak extends BaseTweak {
	protected final int goalPriority;
	protected final boolean callReinforcements;

	public HurtByTargetTweak(ResourceLocation entity, int priority, boolean callReinforcements) {
		super("hurt_by_target", entity);
		this.goalPriority = priority;
		this.callReinforcements = callReinforcements;
	}

	public HurtByTargetTweak(EntityType<? extends Mob> entity, int priority, boolean callReinforcements) {
		this(ForgeRegistries.ENTITY_TYPES.getKey(entity), priority, callReinforcements);
	}

	@Override
	public void adjust(Entity entity) {
		if (entity instanceof Mob mob) {
			if (canHaveGoal(mob)) {
				MobHurtByTargetGoal hurtGoal = new MobHurtByTargetGoal(mob);
				if (callReinforcements) {
					hurtGoal.setCallsForHelp();
				}
				mob.targetSelector.addGoal(goalPriority, hurtGoal);
			}
		} else {
			AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity isn't valid for the tweak", getName(), getEntityLocation()));
		}
	}

	public boolean canHaveGoal(Mob mob) {
		for (Goal goal : mob.goalSelector.availableGoals) {
			if (goal instanceof MobHurtByTargetGoal) {
				AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity already has given AI goal", getName(), getEntityLocation()));
				return false;
			}
		}
		return true;
	}
}
