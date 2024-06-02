package com.mrbysco.angrymobs.tweaks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.angrymobs.AngryMobs;
import com.mrbysco.angrymobs.handler.goals.MobHurtByTargetGoal;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.util.Optional;

public class HurtByTargetTweak implements ITweak {
	public static final ResourceKey<Registry<HurtByTargetTweak>> REGISTRY_KEY = ResourceKey.createRegistryKey(
			new ResourceLocation(AngryMobs.MOD_ID, "hurt_by_target"));
	public static final Codec<HurtByTargetTweak> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
					ResourceLocation.CODEC.fieldOf("entity").forGetter(HurtByTargetTweak::entity),
					Codec.INT.fieldOf("goalPriority").forGetter(HurtByTargetTweak::goalPriority),
					Codec.BOOL.fieldOf("callReinforcements").forGetter(HurtByTargetTweak::callReinforcements))
			.apply(inst, HurtByTargetTweak::new));
	public static final Codec<Optional<WithConditions<HurtByTargetTweak>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(DIRECT_CODEC);

	protected final ResourceLocation entity;
	protected final int goalPriority;
	protected final boolean callReinforcements;

	public HurtByTargetTweak(ResourceLocation entity, int priority, boolean callReinforcements) {
		this.entity = entity;
		this.goalPriority = priority;
		this.callReinforcements = callReinforcements;
	}

	@Override
	public String generateId() {
		return entity.getNamespace() + "_" + entity.getPath() + "_hurt_by_target";
	}

	public HurtByTargetTweak(EntityType<? extends Mob> entity, int priority, boolean callReinforcements) {
		this(BuiltInRegistries.ENTITY_TYPE.getKey(entity), priority, callReinforcements);
	}

	@Override
	public void adjust(Entity entity, String id) {
		if (entity instanceof Mob mob) {
			if (canHaveGoal(mob, id)) {
				MobHurtByTargetGoal hurtGoal = new MobHurtByTargetGoal(mob);
				if (callReinforcements) {
					hurtGoal.setCallsForHelp();
				}
				mob.targetSelector.addGoal(goalPriority, hurtGoal);
			}
		} else {
			AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity isn't valid for the tweak", id, entity()));
		}
	}

	public boolean canHaveGoal(Mob mob, String id) {
		for (Goal goal : mob.goalSelector.availableGoals) {
			if (goal instanceof MobHurtByTargetGoal) {
				AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity already has given AI goal", id, entity()));
				return false;
			}
		}
		return true;
	}

	@Override
	public ResourceLocation entity() {
		return entity;
	}

	public int goalPriority() {
		return goalPriority;
	}

	public boolean callReinforcements() {
		return callReinforcements;
	}
}
