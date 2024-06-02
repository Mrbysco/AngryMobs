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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.util.Optional;

public class AttackNearestTweak implements ITweak {
	public static final ResourceKey<Registry<AttackNearestTweak>> REGISTRY_KEY = ResourceKey.createRegistryKey(
			new ResourceLocation(AngryMobs.MOD_ID, "attack_nearest"));
	public static final Codec<AttackNearestTweak> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
					ResourceLocation.CODEC.fieldOf("entity").forGetter(AttackNearestTweak::entity),
					ResourceLocation.CODEC.fieldOf("target").forGetter(AttackNearestTweak::target),
					Codec.INT.fieldOf("goalPriority").forGetter(AttackNearestTweak::goalPriority),
					Codec.BOOL.fieldOf("checkSight").forGetter(AttackNearestTweak::checkSight))
			.apply(inst, AttackNearestTweak::new));
	public static final Codec<Optional<WithConditions<AttackNearestTweak>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(DIRECT_CODEC);

	protected final ResourceLocation entity;
	protected final ResourceLocation target;
	protected final int goalPriority;
	protected final boolean checkSight;

	public AttackNearestTweak(ResourceLocation entity, ResourceLocation target, int priority, boolean checkSight) {
		this.entity = entity;
		this.target = target;
		this.goalPriority = priority;
		this.checkSight = checkSight;
	}

	@Override
	public String generateId() {
		return "attack_nearest_" + target.getNamespace() + "_" + target.getPath();
	}

	public AttackNearestTweak(EntityType<? extends Mob> entity, EntityType<? extends LivingEntity> target, int priority, boolean checkSight) {
		this(BuiltInRegistries.ENTITY_TYPE.getKey(entity), BuiltInRegistries.ENTITY_TYPE.getKey(target), priority, checkSight);
	}

	@Override
	public void adjust(Entity entity, String id) {
		if (entity instanceof Mob mob) {
			if (canHaveGoal(mob, id)) {
				if (target.toString().equals("minecraft:player")) {
					mob.targetSelector.addGoal(goalPriority, new NearestAttackableTargetGoal<>(mob, Player.class, checkSight));
				} else {
					Entity targetEntity = BuiltInRegistries.ENTITY_TYPE.get(target).create(entity.level());
					if (targetEntity instanceof LivingEntity) {
						Class<? extends LivingEntity> entityClass = ((LivingEntity) targetEntity).getClass();
						mob.targetSelector.addGoal(goalPriority, new NearestAttackableTargetGoal<>(mob, entityClass, checkSight));
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
			if (goal instanceof NearestAttackableTargetGoal nearestAttackable) {
				if (target.toString().equals("minecraft:player")) {
					if (nearestAttackable.targetType == Player.class) {
						AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity already has given AI goal", id, entity()));
						return false;
					}
				} else {
					Entity targetEntity = BuiltInRegistries.ENTITY_TYPE.get(target).create(mob.level());
					if (targetEntity instanceof LivingEntity) {
						Class<? extends LivingEntity> entityClass = ((LivingEntity) targetEntity).getClass();
						if (nearestAttackable.targetType == entityClass) {
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

	public boolean checkSight() {
		return checkSight;
	}
}
