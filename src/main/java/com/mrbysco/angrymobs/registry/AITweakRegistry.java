package com.mrbysco.angrymobs.registry;

import com.mrbysco.angrymobs.config.AngryConfig;
import com.mrbysco.angrymobs.registry.tweaks.AttackNearestTweak;
import com.mrbysco.angrymobs.registry.tweaks.HurtByTargetTweak;
import com.mrbysco.angrymobs.registry.tweaks.ITweak;
import com.mrbysco.angrymobs.registry.tweaks.LeapAtTargetTweak;
import com.mrbysco.angrymobs.registry.tweaks.MeleeAttackTweak;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class AITweakRegistry {
	private final LinkedHashMap<ResourceLocation, List<ITweak>> tweakMap = new LinkedHashMap<>();

	private static AITweakRegistry INSTANCE;

	public static AITweakRegistry instance() {
		if (INSTANCE == null)
			INSTANCE = new AITweakRegistry();
		return INSTANCE;
	}

	public void initializeTweaks() {
		tweakMap.clear();
		if (AngryConfig.SERVER.angryAnimals.get() || AngryConfig.SERVER.aggressiveAnimals.get()) {
			registerTweak(new MeleeAttackTweak(EntityType.CHICKEN, 1, 0.95D, 1.0F, false));
			registerTweak(new LeapAtTargetTweak(EntityType.CHICKEN, 4, 0.3F));
			registerTweak(new MeleeAttackTweak(EntityType.COW, 1, 1.0D, 2.0F, false));
			registerTweak(new MeleeAttackTweak(EntityType.DONKEY, 1, 1.5D, 3.0F, false));
			registerTweak(new MeleeAttackTweak(EntityType.HORSE, 1, 1.5D, 3.0F, false));
			registerTweak(new MeleeAttackTweak(EntityType.MOOSHROOM, 1, 1.0D, 2.0F, false));
			registerTweak(new MeleeAttackTweak(EntityType.MULE, 1, 1.5D, 3.0F, false));
			registerTweak(new MeleeAttackTweak(EntityType.PIG, 1, 1.0D, 2.0F, false));
			registerTweak(new MeleeAttackTweak(EntityType.RABBIT, 1, 1.4D, 1.0F, false));
			registerTweak(new MeleeAttackTweak(EntityType.SHEEP, 1, 1.0D, 2.0F, false));

			if (AngryConfig.SERVER.angryAnimals.get()) {
				boolean reinforcements = AngryConfig.SERVER.angryReinforcements.get();

				registerTweak(new HurtByTargetTweak(EntityType.CHICKEN, 1, reinforcements));
				registerTweak(new HurtByTargetTweak(EntityType.COW, 1, reinforcements));
				registerTweak(new HurtByTargetTweak(EntityType.DONKEY, 1, reinforcements));
				registerTweak(new HurtByTargetTweak(EntityType.HORSE, 1, reinforcements));
				registerTweak(new HurtByTargetTweak(EntityType.MOOSHROOM, 1, reinforcements));
				registerTweak(new HurtByTargetTweak(EntityType.MULE, 1, reinforcements));
				registerTweak(new HurtByTargetTweak(EntityType.PIG, 1, reinforcements));
				registerTweak(new HurtByTargetTweak(EntityType.RABBIT, 1, reinforcements));
				registerTweak(new HurtByTargetTweak(EntityType.SHEEP, 1, reinforcements));
			}
			if (AngryConfig.SERVER.aggressiveAnimals.get()) {
				registerTweak(new AttackNearestTweak(EntityType.CAT, EntityType.PLAYER, 2, true));
				registerTweak(new AttackNearestTweak(EntityType.CHICKEN, EntityType.PLAYER, 2, true));
				registerTweak(new AttackNearestTweak(EntityType.COW, EntityType.PLAYER, 2, true));
				registerTweak(new AttackNearestTweak(EntityType.DONKEY, EntityType.PLAYER, 2, true));
				registerTweak(new AttackNearestTweak(EntityType.FOX, EntityType.PLAYER, 2, true));
				registerTweak(new AttackNearestTweak(EntityType.HORSE, EntityType.PLAYER, 2, true));
				registerTweak(new AttackNearestTweak(EntityType.LLAMA, EntityType.PLAYER, 2, true));
				registerTweak(new AttackNearestTweak(EntityType.MULE, EntityType.PLAYER, 2, true));
				registerTweak(new AttackNearestTweak(EntityType.PANDA, EntityType.PLAYER, 2, true));
				registerTweak(new AttackNearestTweak(EntityType.PIG, EntityType.PLAYER, 2, true));
				registerTweak(new AttackNearestTweak(EntityType.SHEEP, EntityType.PLAYER, 2, true));
				registerTweak(new AttackNearestTweak(EntityType.TRADER_LLAMA, EntityType.PLAYER, 2, true));
				registerTweak(new AttackNearestTweak(EntityType.WOLF, EntityType.PLAYER, 2, true));
			}
		}
	}

	public void registerTweak(ITweak event) {
		ResourceLocation entityLocation = event.getEntityLocation();
		if (tweakMap.containsKey(entityLocation)) {
			List<ITweak> dataList = new ArrayList<>(tweakMap.get(entityLocation));
			dataList.add(event);
			tweakMap.put(entityLocation, dataList);
		} else {
			tweakMap.put(entityLocation, Collections.singletonList(event));
		}
	}

	public boolean containsEntity(EntityType<? extends PathfinderMob> entityType) {
		return tweakMap.containsKey(entityType.getRegistryName());
	}

	public boolean containsEntity(ResourceLocation entityLocation) {
		return tweakMap.containsKey(entityLocation);
	}

	public List<ITweak> getTweaksFromType(EntityType<? extends PathfinderMob> entityType) {
		return tweakMap.containsKey(entityType.getRegistryName()) ? tweakMap.get(entityType.getRegistryName()) : new ArrayList<>();
	}

	public List<ITweak> getTweaksFromType(ResourceLocation entityLocation) {
		return tweakMap.containsKey(entityLocation) ? tweakMap.get(entityLocation) : new ArrayList<>();
	}

	public LinkedHashMap<ResourceLocation, List<ITweak>> getTweakMap() {
		return tweakMap;
	}
}
