package com.mrbysco.angrymobs.registry.tweaks;

import com.mrbysco.angrymobs.AngryMobs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;

import java.util.function.Predicate;

public class BreakDoorTweak extends BaseTweak {
	protected final int goalPriority;
	protected final Difficulty difficulty;

	public BreakDoorTweak(ResourceLocation entity, int priority, int difficulty) {
		super("break_door", entity);
		this.goalPriority = priority;
		this.difficulty = Difficulty.byId(difficulty);
	}

	public BreakDoorTweak(EntityType<? extends Mob> entity, int priority, int difficulty) {
		this(BuiltInRegistries.ENTITY_TYPE.getKey(entity), priority, difficulty);
	}

	@Override
	public void adjust(Entity entity) {
		if (entity instanceof Mob mob && difficulty != null) {
			mob.goalSelector.availableGoals.forEach(goal -> {
				if (goal.getGoal() instanceof BreakDoorGoal) {
					AngryMobs.LOGGER.info(String.format("Overriding existing AI goal for entity %s using tweak ID %s", getEntityLocation(), getName()));
				}
			});
			mob.goalSelector.availableGoals.removeIf(goal -> goal.getGoal() instanceof BreakDoorGoal);
			Predicate<Difficulty> DOOR_BREAKING_PREDICATE = (dif) -> dif == difficulty;
			mob.goalSelector.addGoal(goalPriority, new BreakDoorGoal(mob, DOOR_BREAKING_PREDICATE));
		} else {
			AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity isn't valid for the tweak", getName(), getEntityLocation()));
		}
	}
}
