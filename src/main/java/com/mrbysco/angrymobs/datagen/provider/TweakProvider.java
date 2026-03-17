package com.mrbysco.angrymobs.datagen.provider;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mrbysco.angrymobs.AngryMobs;
import com.mrbysco.angrymobs.tweaks.AttackNearestTweak;
import com.mrbysco.angrymobs.tweaks.AvoidEntityTweak;
import com.mrbysco.angrymobs.tweaks.BreakDoorTweak;
import com.mrbysco.angrymobs.tweaks.HurtByTargetTweak;
import com.mrbysco.angrymobs.tweaks.LeapAtTargetTweak;
import com.mrbysco.angrymobs.tweaks.LookAtEntityTweak;
import com.mrbysco.angrymobs.tweaks.MeleeAttackTweak;
import com.mrbysco.angrymobs.tweaks.ProjectileAttackTweak;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class TweakProvider implements DataProvider {
	private final CompletableFuture<HolderLookup.Provider> registries;
	private final PackOutput output;
	private final String modid;

	private final Map<String, WithConditions<AttackNearestTweak>> toSerializeAddAttackNearestTweak = new HashMap<>();
	private final Map<String, WithConditions<BreakDoorTweak>> toSerializeAddBreakDoorTweak = new HashMap<>();
	private final Map<String, WithConditions<HurtByTargetTweak>> toSerializeAddHurtTweak = new HashMap<>();
	private final Map<String, WithConditions<LeapAtTargetTweak>> toSerializeAddLeapTweak = new HashMap<>();
	private final Map<String, WithConditions<MeleeAttackTweak>> toSerializeAddMeleeTweak = new HashMap<>();
	private final Map<String, WithConditions<ProjectileAttackTweak>> toSerializeAddProjectileAttackTweak = new HashMap<>();
	private final Map<String, WithConditions<AvoidEntityTweak>> toSerializeAvoidEntityTweak = new HashMap<>();
	private final Map<String, WithConditions<LookAtEntityTweak>> toSerializeLookAtEntityTweak = new HashMap<>();


	public TweakProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, String modid) {
		this.output = packOutput;
		this.modid = modid;
		this.registries = registries;
	}

	@Override
	public final CompletableFuture<?> run(CachedOutput cache) {
		return this.registries.thenCompose(registries -> this.run(cache, registries));
	}

	public CompletableFuture<?> run(CachedOutput cache, HolderLookup.Provider registries) {
		start();

		ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();

		saveTweaks(cache, registries, futuresBuilder, toSerializeAddAttackNearestTweak, AttackNearestTweak.CONDITIONAL_CODEC, AttackNearestTweak.REGISTRY_KEY.identifier().getPath());
		saveTweaks(cache, registries, futuresBuilder, toSerializeAddBreakDoorTweak, BreakDoorTweak.CONDITIONAL_CODEC, BreakDoorTweak.REGISTRY_KEY.identifier().getPath());
		saveTweaks(cache, registries, futuresBuilder, toSerializeAddHurtTweak, HurtByTargetTweak.CONDITIONAL_CODEC, HurtByTargetTweak.REGISTRY_KEY.identifier().getPath());
		saveTweaks(cache, registries, futuresBuilder, toSerializeAddLeapTweak, LeapAtTargetTweak.CONDITIONAL_CODEC, LeapAtTargetTweak.REGISTRY_KEY.identifier().getPath());
		saveTweaks(cache, registries, futuresBuilder, toSerializeAddMeleeTweak, MeleeAttackTweak.CONDITIONAL_CODEC, MeleeAttackTweak.REGISTRY_KEY.identifier().getPath());
		saveTweaks(cache, registries, futuresBuilder, toSerializeAddProjectileAttackTweak, ProjectileAttackTweak.CONDITIONAL_CODEC, ProjectileAttackTweak.REGISTRY_KEY.identifier().getPath());
		saveTweaks(cache, registries, futuresBuilder, toSerializeAvoidEntityTweak, AvoidEntityTweak.CONDITIONAL_CODEC, AvoidEntityTweak.REGISTRY_KEY.identifier().getPath());
		saveTweaks(cache, registries, futuresBuilder, toSerializeLookAtEntityTweak, LookAtEntityTweak.CONDITIONAL_CODEC, LookAtEntityTweak.REGISTRY_KEY.identifier().getPath());

		return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
	}

	private <T> void saveTweaks(CachedOutput cache, HolderLookup.Provider registries,
	                            ImmutableList.Builder<CompletableFuture<?>> futuresBuilder,
	                            Map<String, WithConditions<T>> tweakMap,
	                            Codec<Optional<WithConditions<T>>> codec, String folderName) {
		Path folderPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(this.modid).resolve(AngryMobs.MOD_ID).resolve(folderName);
		tweakMap.forEach((name, tweak) -> {
			Path modifierPath = folderPath.resolve(name + ".json");
			futuresBuilder.add(DataProvider.saveStable(cache, registries, codec, Optional.of(tweak), modifierPath));
		});
	}

	protected abstract void start();

	public <T extends AttackNearestTweak> void addAttackNearestTweak(String tweakId, T instance, List<ICondition> conditions) {
		this.toSerializeAddAttackNearestTweak.put(tweakId, new WithConditions<>(conditions, instance));
	}

	public <T extends AttackNearestTweak> void addAttackNearestTweak(String placeID, T instance, ICondition... conditions) {
		addAttackNearestTweak(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends AvoidEntityTweak> void addAvoidEntityTweak(String tweakId, T instance, List<ICondition> conditions) {
		this.toSerializeAvoidEntityTweak.put(tweakId, new WithConditions<>(conditions, instance));
	}

	public <T extends AvoidEntityTweak> void addAvoidEntityTweak(String placeID, T instance, ICondition... conditions) {
		addAvoidEntityTweak(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends BreakDoorTweak> void addBreakDoorTweak(String tweakId, T instance, List<ICondition> conditions) {
		this.toSerializeAddBreakDoorTweak.put(tweakId, new WithConditions<>(conditions, instance));
	}

	public <T extends BreakDoorTweak> void addBreakDoorTweak(String placeID, T instance, ICondition... conditions) {
		addBreakDoorTweak(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends HurtByTargetTweak> void addHurtByTargetTweak(String tweakId, T instance, List<ICondition> conditions) {
		this.toSerializeAddHurtTweak.put(tweakId, new WithConditions<>(conditions, instance));
	}

	public <T extends HurtByTargetTweak> void addHurtByTargetTweak(String placeID, T instance, ICondition... conditions) {
		addHurtByTargetTweak(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends LeapAtTargetTweak> void addLeapAtTargetTweak(String tweakId, T instance, List<ICondition> conditions) {
		this.toSerializeAddLeapTweak.put(tweakId, new WithConditions<>(conditions, instance));
	}

	public <T extends LeapAtTargetTweak> void addLeapAtTargetTweak(String placeID, T instance, ICondition... conditions) {
		addLeapAtTargetTweak(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends MeleeAttackTweak> void addMeleeAttackTweak(String tweakId, T instance, List<ICondition> conditions) {
		this.toSerializeAddMeleeTweak.put(tweakId, new WithConditions<>(conditions, instance));
	}

	public <T extends MeleeAttackTweak> void addMeleeAttackTweak(String placeID, T instance, ICondition... conditions) {
		addMeleeAttackTweak(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends ProjectileAttackTweak> void addProjectileAttackTweak(String tweakId, T instance, List<ICondition> conditions) {
		this.toSerializeAddProjectileAttackTweak.put(tweakId, new WithConditions<>(conditions, instance));
	}

	public <T extends ProjectileAttackTweak> void addProjectileAttackTweak(String placeID, T instance, ICondition... conditions) {
		addProjectileAttackTweak(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends LookAtEntityTweak> void addLookAtEntityTweak(String tweakId, T instance, List<ICondition> conditions) {
		this.toSerializeLookAtEntityTweak.put(tweakId, new WithConditions<>(conditions, instance));
	}

	public <T extends LookAtEntityTweak> void addLookAtEntityTweak(String placeID, T instance, ICondition... conditions) {
		addLookAtEntityTweak(placeID, instance, Arrays.asList(conditions));
	}


	@Override
	public String getName() {
		return "Places: " + modid;
	}
}
