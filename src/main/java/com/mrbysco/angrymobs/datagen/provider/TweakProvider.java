package com.mrbysco.angrymobs.datagen.provider;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mrbysco.angrymobs.AngryMobs;
import com.mrbysco.angrymobs.tweaks.AttackNearestTweak;
import com.mrbysco.angrymobs.tweaks.AvoidEntityTweak;
import com.mrbysco.angrymobs.tweaks.BreakDoorTweak;
import com.mrbysco.angrymobs.tweaks.HurtByTargetTweak;
import com.mrbysco.angrymobs.tweaks.LeapAtTargetTweak;
import com.mrbysco.angrymobs.tweaks.LookAtEntityTweak;
import com.mrbysco.angrymobs.tweaks.MeleeAttackTweak;
import com.mrbysco.angrymobs.tweaks.ProjectileAttackTweak;
import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
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
	private final PackOutput output;
	private final String modid;

	private final Map<String, JsonElement> toSerializeaddAttackNearestTweak = new HashMap<>();
	private final Map<String, JsonElement> toSerializeaddBreakDoorTweak = new HashMap<>();
	private final Map<String, JsonElement> toSerializeaddHurtTweak = new HashMap<>();
	private final Map<String, JsonElement> toSerializeaddLeapTweak = new HashMap<>();
	private final Map<String, JsonElement> toSerializeaddMeleeTweak = new HashMap<>();
	private final Map<String, JsonElement> toSerializeaddProjectileAttackTweak = new HashMap<>();
	private final Map<String, JsonElement> toSerializeavoidEntityTweak = new HashMap<>();
	private final Map<String, JsonElement> toSerializelookAtEntityTweak = new HashMap<>();


	public TweakProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, String modid) {
		this.output = packOutput;
		this.modid = modid;
	}

	public CompletableFuture<?> run(CachedOutput cache) {
		start();

		ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();

		saveTweaks(cache, futuresBuilder, toSerializeaddAttackNearestTweak, AttackNearestTweak.REGISTRY_KEY.location().getPath());
		saveTweaks(cache, futuresBuilder, toSerializeaddBreakDoorTweak, BreakDoorTweak.REGISTRY_KEY.location().getPath());
		saveTweaks(cache, futuresBuilder, toSerializeaddHurtTweak, HurtByTargetTweak.REGISTRY_KEY.location().getPath());
		saveTweaks(cache, futuresBuilder, toSerializeaddLeapTweak, LeapAtTargetTweak.REGISTRY_KEY.location().getPath());
		saveTweaks(cache, futuresBuilder, toSerializeaddMeleeTweak, MeleeAttackTweak.REGISTRY_KEY.location().getPath());
		saveTweaks(cache, futuresBuilder, toSerializeaddProjectileAttackTweak, ProjectileAttackTweak.REGISTRY_KEY.location().getPath());
		saveTweaks(cache, futuresBuilder, toSerializeavoidEntityTweak, AvoidEntityTweak.REGISTRY_KEY.location().getPath());
		saveTweaks(cache, futuresBuilder, toSerializelookAtEntityTweak, LookAtEntityTweak.REGISTRY_KEY.location().getPath());

		return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
	}

	private void saveTweaks(CachedOutput cache, ImmutableList.Builder<CompletableFuture<?>> futuresBuilder, Map<String, JsonElement> tweakMap, String folderName) {
		Path folderPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(this.modid).resolve(AngryMobs.MOD_ID).resolve(folderName);
		tweakMap.forEach(LamdbaExceptionUtils.rethrowBiConsumer((name, json) -> {
			Path modifierPath = folderPath.resolve(name + ".json");
			futuresBuilder.add(DataProvider.saveStable(cache, json, modifierPath));
		}));
	}

	protected abstract void start();

	public <T extends AttackNearestTweak> void addAttackNearestTweak(String tweakId, T instance, List<ICondition> conditions) {
		JsonElement json = AttackNearestTweak.CONDITIONAL_CODEC.encodeStart(JsonOps.INSTANCE, Optional.of(new WithConditions<>(conditions, instance))).getOrThrow(false, s -> {
		});
		this.toSerializeaddAttackNearestTweak.put(tweakId, json);
	}

	public <T extends AttackNearestTweak> void addAttackNearestTweak(String placeID, T instance, ICondition... conditions) {
		addAttackNearestTweak(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends AvoidEntityTweak> void addAvoidEntityTweak(String tweakId, T instance, List<ICondition> conditions) {
		JsonElement json = AvoidEntityTweak.CONDITIONAL_CODEC.encodeStart(JsonOps.INSTANCE, Optional.of(new WithConditions<>(conditions, instance))).getOrThrow(false, s -> {
		});
		this.toSerializeavoidEntityTweak.put(tweakId, json);
	}

	public <T extends AvoidEntityTweak> void addAvoidEntityTweak(String placeID, T instance, ICondition... conditions) {
		addAvoidEntityTweak(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends BreakDoorTweak> void addBreakDoorTweak(String tweakId, T instance, List<ICondition> conditions) {
		JsonElement json = BreakDoorTweak.CONDITIONAL_CODEC.encodeStart(JsonOps.INSTANCE, Optional.of(new WithConditions<>(conditions, instance))).getOrThrow(false, s -> {
		});
		this.toSerializeaddBreakDoorTweak.put(tweakId, json);
	}

	public <T extends BreakDoorTweak> void addBreakDoorTweak(String placeID, T instance, ICondition... conditions) {
		addBreakDoorTweak(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends HurtByTargetTweak> void addHurtByTargetTweak(String tweakId, T instance, List<ICondition> conditions) {
		JsonElement json = HurtByTargetTweak.CONDITIONAL_CODEC.encodeStart(JsonOps.INSTANCE, Optional.of(new WithConditions<>(conditions, instance))).getOrThrow(false, s -> {
		});
		this.toSerializeaddHurtTweak.put(tweakId, json);
	}

	public <T extends HurtByTargetTweak> void addHurtByTargetTweak(String placeID, T instance, ICondition... conditions) {
		addHurtByTargetTweak(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends LeapAtTargetTweak> void addLeapAtTargetTweak(String tweakId, T instance, List<ICondition> conditions) {
		JsonElement json = LeapAtTargetTweak.CONDITIONAL_CODEC.encodeStart(JsonOps.INSTANCE, Optional.of(new WithConditions<>(conditions, instance))).getOrThrow(false, s -> {
		});
		this.toSerializeaddLeapTweak.put(tweakId, json);
	}

	public <T extends LeapAtTargetTweak> void addLeapAtTargetTweak(String placeID, T instance, ICondition... conditions) {
		addLeapAtTargetTweak(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends MeleeAttackTweak> void addMeleeAttackTweak(String tweakId, T instance, List<ICondition> conditions) {
		JsonElement json = MeleeAttackTweak.CONDITIONAL_CODEC.encodeStart(JsonOps.INSTANCE, Optional.of(new WithConditions<>(conditions, instance))).getOrThrow(false, s -> {
		});
		this.toSerializeaddMeleeTweak.put(tweakId, json);
	}

	public <T extends MeleeAttackTweak> void addMeleeAttackTweak(String placeID, T instance, ICondition... conditions) {
		addMeleeAttackTweak(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends ProjectileAttackTweak> void addProjectileAttackTweak(String tweakId, T instance, List<ICondition> conditions) {
		JsonElement json = ProjectileAttackTweak.CONDITIONAL_CODEC.encodeStart(JsonOps.INSTANCE, Optional.of(new WithConditions<>(conditions, instance))).getOrThrow(false, s -> {
		});
		this.toSerializeaddProjectileAttackTweak.put(tweakId, json);
	}

	public <T extends ProjectileAttackTweak> void addProjectileAttackTweak(String placeID, T instance, ICondition... conditions) {
		addProjectileAttackTweak(placeID, instance, Arrays.asList(conditions));
	}

	public <T extends LookAtEntityTweak> void addLookAtEntityTweak(String tweakId, T instance, List<ICondition> conditions) {
		JsonElement json = LookAtEntityTweak.CONDITIONAL_CODEC.encodeStart(JsonOps.INSTANCE, Optional.of(new WithConditions<>(conditions, instance))).getOrThrow(false, s -> {
		});
		this.toSerializelookAtEntityTweak.put(tweakId, json);
	}

	public <T extends LookAtEntityTweak> void addLookAtEntityTweak(String placeID, T instance, ICondition... conditions) {
		addLookAtEntityTweak(placeID, instance, Arrays.asList(conditions));
	}


	@Override
	public String getName() {
		return "Places: " + modid;
	}
}
