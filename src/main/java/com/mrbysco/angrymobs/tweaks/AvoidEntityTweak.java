package com.mrbysco.angrymobs.tweaks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.angrymobs.AngryMobs;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.util.Optional;

public class AvoidEntityTweak implements ITweak {
	public static final ResourceKey<Registry<AvoidEntityTweak>> REGISTRY_KEY = ResourceKey.createRegistryKey(
			AngryMobs.modLoc("avoid_entity"));
	public static final Codec<AvoidEntityTweak> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
					ResourceLocation.CODEC.fieldOf("entity").forGetter(AvoidEntityTweak::entity),
					ResourceLocation.CODEC.fieldOf("target").forGetter(AvoidEntityTweak::target),
					Codec.INT.fieldOf("goalPriority").forGetter(AvoidEntityTweak::goalPriority),
					Codec.FLOAT.fieldOf("maxDistance").forGetter(AvoidEntityTweak::maxDistance),
					Codec.DOUBLE.fieldOf("walkSpeedModifier").forGetter(AvoidEntityTweak::walkSpeedModifier),
					Codec.DOUBLE.fieldOf("sprintSpeedModifier").forGetter(AvoidEntityTweak::sprintSpeedModifier))
			.apply(inst, AvoidEntityTweak::new));
	public static final Codec<Optional<WithConditions<AvoidEntityTweak>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(DIRECT_CODEC);

	protected final ResourceLocation entity;
	protected final ResourceLocation target;
	protected final int goalPriority;
	protected final float maxDistance;
	protected final double walkSpeedModifier;
	protected final double sprintSpeedModifier;

	public AvoidEntityTweak(ResourceLocation entity, ResourceLocation target, int priority, float maxDistance, double walkSpeedModifier, double sprintSpeedModifier) {
		this.entity = entity;
		this.target = target;
		this.goalPriority = priority;
		this.maxDistance = maxDistance;
		this.walkSpeedModifier = walkSpeedModifier;
		this.sprintSpeedModifier = sprintSpeedModifier;
	}

	@Override
	public String generateId() {
		return "avoid_entity_" + target.getNamespace() + "_" + target.getPath();
	}

	@Override
	public void adjust(Entity entity, String id) {
		if (entity instanceof PathfinderMob pathfinderMob) {
			if (canHaveGoal(pathfinderMob, id)) {
				if (target.toString().equals("minecraft:player")) {
					pathfinderMob.targetSelector.addGoal(goalPriority, new AvoidEntityGoal<>(pathfinderMob, Player.class, maxDistance, walkSpeedModifier, sprintSpeedModifier));
				} else {
					Entity targetEntity = BuiltInRegistries.ENTITY_TYPE.get(target).create(entity.level());
					if (targetEntity instanceof LivingEntity) {
						Class<? extends LivingEntity> entityClass = ((LivingEntity) targetEntity).getClass();
						pathfinderMob.targetSelector.addGoal(goalPriority, new AvoidEntityGoal<>(pathfinderMob, entityClass, maxDistance, walkSpeedModifier, sprintSpeedModifier));
						targetEntity.discard();
					} else {
						AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Target entity isn't valid for the tweak", id, entity()));
					}
				}
			}
		} else {
			AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity isn't valid for the tweak", id, entity()));
		}
	}

	public boolean canHaveGoal(PathfinderMob pathfinderMob, String id) {
		for (Goal goal : pathfinderMob.goalSelector.availableGoals) {
			if (goal instanceof AvoidEntityGoal avoidEntity) {
				if (target.toString().equals("minecraft:player")) {
					if (avoidEntity.avoidClass == Player.class) {
						AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity already has given AI goal", id, entity()));
						return false;
					}
				} else {
					Entity targetEntity = BuiltInRegistries.ENTITY_TYPE.get(target).create(pathfinderMob.level());
					if (targetEntity instanceof LivingEntity) {
						Class<? extends LivingEntity> entityClass = ((LivingEntity) targetEntity).getClass();
						if (avoidEntity.avoidClass == entityClass) {
							AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity already has given AI goal", id, entity()));
							targetEntity.discard();
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public ResourceLocation entity() {
		return entity;
	}

	public ResourceLocation target() {
		return target;
	}

	public int goalPriority() {
		return goalPriority;
	}

	public float maxDistance() {
		return maxDistance;
	}

	public double walkSpeedModifier() {
		return walkSpeedModifier;
	}

	public double sprintSpeedModifier() {
		return sprintSpeedModifier;
	}
}
