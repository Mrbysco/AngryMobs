package com.mrbysco.angrymobs.tweaks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.angrymobs.AngryMobs;
import com.mrbysco.angrymobs.config.AngryConfig;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.util.Optional;
import java.util.function.Predicate;

public class BreakDoorTweak implements ITweak {
	public static final ResourceKey<Registry<BreakDoorTweak>> REGISTRY_KEY = ResourceKey.createRegistryKey(
			AngryMobs.modLoc("break_door"));
	public static final Codec<BreakDoorTweak> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
					ResourceLocation.CODEC.fieldOf("entity").forGetter(BreakDoorTweak::entity),
					Codec.INT.fieldOf("goalPriority").forGetter(BreakDoorTweak::goalPriority),
					Difficulty.CODEC.fieldOf("difficulty").forGetter(BreakDoorTweak::difficulty))
			.apply(inst, BreakDoorTweak::new));
	public static final Codec<Optional<WithConditions<BreakDoorTweak>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(DIRECT_CODEC);

	protected final ResourceLocation entity;
	protected final int goalPriority;
	protected final Difficulty difficulty;

	public BreakDoorTweak(ResourceLocation entity, int priority, Difficulty difficulty) {
		this.entity = entity;
		this.goalPriority = priority;
		this.difficulty = difficulty;
	}

	@Override
	public String generateId() {
		return entity.getNamespace() + "_" + entity.getPath() + "_break_door";
	}

	public BreakDoorTweak(ResourceLocation entity, int priority, int difficulty) {
		this(entity, priority, Difficulty.byId(difficulty));
	}

	public BreakDoorTweak(EntityType<? extends Mob> entity, int priority, int difficultyId) {
		this(BuiltInRegistries.ENTITY_TYPE.getKey(entity), priority, Difficulty.byId(difficultyId));
	}

	@Override
	public void adjust(Entity entity, String id) {
		if (entity instanceof Mob mob && difficulty != null) {
			mob.goalSelector.availableGoals.forEach(goal -> {
				if (goal.getGoal() instanceof BreakDoorGoal) {
					if (AngryConfig.COMMON.enableInfoLog.getAsBoolean()) {
						AngryMobs.LOGGER.info("Overriding existing AI goal for entity {} using tweak ID {}", entity(), id);
					}
				}
			});
			mob.goalSelector.availableGoals.removeIf(goal -> goal.getGoal() instanceof BreakDoorGoal);
			Predicate<Difficulty> DOOR_BREAKING_PREDICATE = (dif) -> dif == difficulty;
			mob.goalSelector.addGoal(goalPriority, new BreakDoorGoal(mob, DOOR_BREAKING_PREDICATE));
		} else {
			AngryMobs.LOGGER.error("Can't apply AI tweak of ID {} for entity {}. Entity isn't valid for the tweak", id, entity());
		}
	}

	@Override
	public ResourceLocation entity() {
		return entity;
	}

	public int goalPriority() {
		return goalPriority;
	}

	public Difficulty difficulty() {
		return difficulty;
	}
}
