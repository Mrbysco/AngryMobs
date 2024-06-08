package com.mrbysco.angrymobs.registry;

import com.mrbysco.angrymobs.AngryMobs;
import com.mrbysco.angrymobs.tweaks.AttackNearestTweak;
import com.mrbysco.angrymobs.tweaks.AvoidEntityTweak;
import com.mrbysco.angrymobs.tweaks.BreakDoorTweak;
import com.mrbysco.angrymobs.tweaks.HurtByTargetTweak;
import com.mrbysco.angrymobs.tweaks.ITweak;
import com.mrbysco.angrymobs.tweaks.LeapAtTargetTweak;
import com.mrbysco.angrymobs.tweaks.LookAtEntityTweak;
import com.mrbysco.angrymobs.tweaks.MeleeAttackTweak;
import com.mrbysco.angrymobs.tweaks.ProjectileAttackTweak;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = AngryMobs.MOD_ID)
public class TweakRegistry {
	private static final Map<ResourceLocation, List<Holder<? extends ITweak>>> tweakMap = new LinkedHashMap<>();

	@SubscribeEvent
	public static void onTagsUpdated(OnDatapackSyncEvent event) {
		final RegistryAccess registryAccess = event.getPlayerList().getServer().registryAccess();

		tweakMap.clear();

		final Registry<AttackNearestTweak> attackNearestRegistry = registryAccess.registryOrThrow(AttackNearestTweak.REGISTRY_KEY);
		attackNearestRegistry.holders().forEach(TweakRegistry::addTweak);

		final Registry<AvoidEntityTweak> avoidEntityRegistry = registryAccess.registryOrThrow(AvoidEntityTweak.REGISTRY_KEY);
		avoidEntityRegistry.holders().forEach(TweakRegistry::addTweak);

		final Registry<BreakDoorTweak> breakDoorRegistry = registryAccess.registryOrThrow(BreakDoorTweak.REGISTRY_KEY);
		breakDoorRegistry.holders().forEach(TweakRegistry::addTweak);

		final Registry<HurtByTargetTweak> hurtByTargetRegistry = registryAccess.registryOrThrow(HurtByTargetTweak.REGISTRY_KEY);
		hurtByTargetRegistry.holders().forEach(TweakRegistry::addTweak);

		final Registry<LeapAtTargetTweak> leapAtTargetRegistry = registryAccess.registryOrThrow(LeapAtTargetTweak.REGISTRY_KEY);
		leapAtTargetRegistry.holders().forEach(TweakRegistry::addTweak);

		final Registry<LookAtEntityTweak> lookAtEntityRegistry = registryAccess.registryOrThrow(LookAtEntityTweak.REGISTRY_KEY);
		lookAtEntityRegistry.holders().forEach(TweakRegistry::addTweak);

		final Registry<MeleeAttackTweak> meleeAttackRegistry = registryAccess.registryOrThrow(MeleeAttackTweak.REGISTRY_KEY);
		meleeAttackRegistry.holders().forEach(TweakRegistry::addTweak);

		final Registry<ProjectileAttackTweak> projectileAttackRegistry = registryAccess.registryOrThrow(ProjectileAttackTweak.REGISTRY_KEY);
		projectileAttackRegistry.holders().forEach(TweakRegistry::addTweak);
	}

	/**
	 * Add tweak to the map based on the entity location
	 *
	 * @param tweak The tweak to add
	 */
	public static void addTweak(Holder<? extends ITweak> tweak) {
		ResourceLocation entityLocation = tweak.value().entity();
		tweakMap.compute(entityLocation, (key, list) -> {
			list = list == null ? new ArrayList<>() : new ArrayList<>(list);
			list.add(tweak);
			return list;
		});
	}

	/**
	 * Check if the tweak map contains the entity
	 *
	 * @param entityType The entity type to check
	 * @return If the entity is in the map
	 */
	public static boolean containsEntity(EntityType<? extends PathfinderMob> entityType) {
		return tweakMap.containsKey(BuiltInRegistries.ENTITY_TYPE.getKey(entityType));
	}

	/**
	 * Check if the tweak map contains the entity location
	 *
	 * @param entityLocation The entity location to check
	 * @return If the entity is in the map
	 */
	public static boolean containsEntity(ResourceLocation entityLocation) {
		return tweakMap.containsKey(entityLocation);
	}

	/**
	 * Get the tweaks from the entity type
	 *
	 * @param entityType The entity type to get the tweaks from
	 * @return The list of tweaks
	 */
	public static List<Holder<? extends ITweak>> getTweaksFromType(EntityType<? extends PathfinderMob> entityType) {
		return tweakMap.containsKey(BuiltInRegistries.ENTITY_TYPE.getKey(entityType)) ? tweakMap.get(BuiltInRegistries.ENTITY_TYPE.getKey(entityType)) : new ArrayList<>();
	}

	/**
	 * Get the tweaks from the entity location
	 *
	 * @param entityLocation The entity location to get the tweaks from
	 * @return The list of tweaks
	 */
	public static List<Holder<? extends ITweak>> getTweaksFromType(ResourceLocation entityLocation) {
		return tweakMap.containsKey(entityLocation) ? tweakMap.get(entityLocation) : new ArrayList<>();
	}

	/**
	 * Get am immutable copy of the tweak map
	 *
	 * @return The tweak map
	 */
	public static Map<ResourceLocation, List<Holder<? extends ITweak>>> getTweakMap() {
		return Collections.unmodifiableMap(tweakMap);
	}
}
