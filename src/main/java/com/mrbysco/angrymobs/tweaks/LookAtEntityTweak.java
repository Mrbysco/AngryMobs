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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.util.Optional;

public class LookAtEntityTweak implements ITweak {
	public static final ResourceKey<Registry<LookAtEntityTweak>> REGISTRY_KEY = ResourceKey.createRegistryKey(
			AngryMobs.modLoc("look_at_entity"));
	public static final Codec<LookAtEntityTweak> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
					ResourceLocation.CODEC.fieldOf("entity").forGetter(LookAtEntityTweak::entity),
					ResourceLocation.CODEC.fieldOf("target").forGetter(LookAtEntityTweak::target),
					Codec.INT.fieldOf("goalPriority").forGetter(LookAtEntityTweak::goalPriority),
					Codec.FLOAT.fieldOf("lookDistance").forGetter(LookAtEntityTweak::lookDistance))
			.apply(inst, LookAtEntityTweak::new));
	public static final Codec<Optional<WithConditions<LookAtEntityTweak>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(DIRECT_CODEC);


	protected final ResourceLocation entity;
	protected final ResourceLocation target;
	protected final int goalPriority;
	protected final float lookDistance;

	public LookAtEntityTweak(ResourceLocation entity, ResourceLocation target, int priority, float lookDistance) {
		this.entity = entity;
		this.target = target;
		this.goalPriority = priority;
		this.lookDistance = lookDistance;
	}

	@Override
	public String generateId() {
		return entity.getPath() + "look_at_" + target.getNamespace() + "_" + target.getPath();
	}

	@Override
	public void adjust(Entity entity, String id) {
		if (entity instanceof Mob mob) {
			if (canHaveGoal(mob, id)) {
				if (target.toString().equals("minecraft:player")) {
					mob.targetSelector.addGoal(goalPriority, new LookAtPlayerGoal(mob, Player.class, lookDistance));
				} else {
					Entity targetEntity = BuiltInRegistries.ENTITY_TYPE.get(target).create(entity.level());
					if (targetEntity instanceof LivingEntity) {
						Class<? extends LivingEntity> entityClass = ((LivingEntity) targetEntity).getClass();
						mob.targetSelector.addGoal(goalPriority, new LookAtPlayerGoal(mob, entityClass, lookDistance));
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

	public boolean canHaveGoal(Mob mob, String id) {
		for (Goal goal : mob.goalSelector.availableGoals) {
			if (goal instanceof LookAtPlayerGoal lookAtPlayerGoal) {
				if (target.toString().equals("minecraft:player")) {
					if (lookAtPlayerGoal.lookAtType == Player.class) {
						AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity already has given AI goal", id, entity()));
						return false;
					}
				} else {
					Entity targetEntity = BuiltInRegistries.ENTITY_TYPE.get(target).create(mob.level());
					if (targetEntity instanceof LivingEntity) {
						Class<? extends LivingEntity> entityClass = ((LivingEntity) targetEntity).getClass();
						if (lookAtPlayerGoal.lookAtType == entityClass) {
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

	public float lookDistance() {
		return lookDistance;
	}
}
