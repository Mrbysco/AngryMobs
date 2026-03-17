package com.mrbysco.angrymobs.tweaks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.angrymobs.AngryMobs;
import com.mrbysco.angrymobs.config.AngryConfig;
import com.mrbysco.angrymobs.handler.goals.MobMeleeAttackGoal;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.util.Optional;

public class MeleeAttackTweak implements ITweak {
	public static final ResourceKey<Registry<MeleeAttackTweak>> REGISTRY_KEY = ResourceKey.createRegistryKey(
			AngryMobs.modLoc("melee_attack"));
	public static final Codec<MeleeAttackTweak> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
					Identifier.CODEC.fieldOf("entity").forGetter(MeleeAttackTweak::entity),
					Codec.INT.fieldOf("goalPriority").forGetter(MeleeAttackTweak::goalPriority),
					Codec.DOUBLE.fieldOf("speedModifier").forGetter(MeleeAttackTweak::speed),
					Codec.FLOAT.fieldOf("attackDamage").forGetter(MeleeAttackTweak::damage),
					Codec.FLOAT.optionalFieldOf("knockback", 0.0F).forGetter(MeleeAttackTweak::knockback),
					Codec.BOOL.fieldOf("useLongMemory").forGetter(MeleeAttackTweak::useLongMemory))
			.apply(inst, MeleeAttackTweak::new));

	public static final Codec<Optional<WithConditions<MeleeAttackTweak>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(DIRECT_CODEC);

	protected final Identifier entity;
	protected final int goalPriority;
	protected final double speedModifier;
	protected final float attackDamage;
	protected final float knockback;
	protected final boolean useLongMemory;

	public MeleeAttackTweak(Identifier entity, int priority, double speedModifier, float damage, float knockback, boolean useLongMemory) {
		this.entity = entity;
		this.goalPriority = priority;
		this.speedModifier = speedModifier;
		this.attackDamage = damage;
		this.knockback = knockback;
		this.useLongMemory = useLongMemory;
	}

	@Override
	public String generateId() {
		return "melee_attack_" + entity.getNamespace() + "_" + entity.getPath();
	}

	public MeleeAttackTweak(EntityType<? extends Mob> entity, int priority, double speedModifier, float damage, float knockback, boolean useLongMemory) {
		this(BuiltInRegistries.ENTITY_TYPE.getKey(entity), priority, speedModifier, damage, knockback, useLongMemory);
	}

	public MeleeAttackTweak(EntityType<? extends Mob> entity, int priority, double speedModifier, float damage, boolean useLongMemory) {
		this(BuiltInRegistries.ENTITY_TYPE.getKey(entity), priority, speedModifier, damage, 0.0F, useLongMemory);
	}

	@Override
	public void adjust(Entity entity, String id) {
		if (entity instanceof Mob mob) {
			mob.goalSelector.availableGoals.removeIf(goal -> goal.getGoal() instanceof PanicGoal);
			mob.goalSelector.availableGoals.forEach(goal -> {
				if (goal.getGoal() instanceof MeleeAttackGoal) {
					if (AngryConfig.COMMON.enableInfoLog.getAsBoolean()) {
						AngryMobs.LOGGER.info("Removing existing AI to apply the AI tweak of ID {} for entity {}", entity(), id);
					}
				}
			});
			mob.goalSelector.availableGoals.removeIf(goal -> goal.getGoal() instanceof MeleeAttackGoal);
			mob.goalSelector.addGoal(goalPriority, new MobMeleeAttackGoal(mob, speedModifier, attackDamage, knockback, useLongMemory));
		} else {
			AngryMobs.LOGGER.error("Can't apply AI tweak of ID {} for entity {}. Entity isn't valid for the tweak", id, entity());
		}
	}

	@Override
	public Identifier entity() {
		return entity;
	}

	public int goalPriority() {
		return goalPriority;
	}

	public double speed() {
		return speedModifier;
	}

	public float damage() {
		return attackDamage;
	}

	public float knockback() {
		return knockback;
	}

	public boolean useLongMemory() {
		return useLongMemory;
	}
}
