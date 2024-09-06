package com.mrbysco.angrymobs.tweaks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.angrymobs.AngryMobs;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.util.Optional;

public class LeapAtTargetTweak implements ITweak {
	public static final ResourceKey<Registry<LeapAtTargetTweak>> REGISTRY_KEY = ResourceKey.createRegistryKey(
			AngryMobs.modLoc("leap_at_target"));
	public static final Codec<LeapAtTargetTweak> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
					ResourceLocation.CODEC.fieldOf("entity").forGetter(LeapAtTargetTweak::entity),
					Codec.INT.fieldOf("goalPriority").forGetter(LeapAtTargetTweak::goalPriority),
					Codec.FLOAT.fieldOf("leapMotion").forGetter(LeapAtTargetTweak::leapMotion))
			.apply(inst, LeapAtTargetTweak::new));
	public static final Codec<Optional<WithConditions<LeapAtTargetTweak>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(DIRECT_CODEC);

	protected final ResourceLocation entity;
	protected final int goalPriority;
	protected final float leapMotion;

	public LeapAtTargetTweak(ResourceLocation entity, int priority, float leapMotion) {
		this.entity = entity;
		this.goalPriority = priority;
		this.leapMotion = leapMotion;
	}

	@Override
	public String generateId() {
		return entity.getNamespace() + "_" + entity.getPath() + "_leap_at_target";
	}

	public LeapAtTargetTweak(EntityType<? extends PathfinderMob> entity, int priority, float leapMotion) {
		this(BuiltInRegistries.ENTITY_TYPE.getKey(entity), priority, leapMotion);
	}

	@Override
	public void adjust(Entity entity, String id) {
		if (entity instanceof Mob mob) {
			mob.goalSelector.availableGoals.forEach(goal -> {
				if (goal.getGoal() instanceof LeapAtTargetGoal) {
					AngryMobs.LOGGER.info("Overriding existing AI goal for entity {} using tweak ID {}", entity(), id);
				}
			});
			mob.goalSelector.availableGoals.removeIf(goal -> goal.getGoal() instanceof LeapAtTargetGoal);
			mob.goalSelector.addGoal(goalPriority, new LeapAtTargetGoal(mob, leapMotion));
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

	public float leapMotion() {
		return leapMotion;
	}
}
