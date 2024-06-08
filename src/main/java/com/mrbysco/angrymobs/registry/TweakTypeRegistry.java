package com.mrbysco.angrymobs.registry;

import com.mojang.serialization.MapCodec;
import com.mrbysco.angrymobs.AngryMobs;
import com.mrbysco.angrymobs.registry.condition.ConfigEnabledCondition;
import com.mrbysco.angrymobs.tweaks.AttackNearestTweak;
import com.mrbysco.angrymobs.tweaks.AvoidEntityTweak;
import com.mrbysco.angrymobs.tweaks.BreakDoorTweak;
import com.mrbysco.angrymobs.tweaks.HurtByTargetTweak;
import com.mrbysco.angrymobs.tweaks.LeapAtTargetTweak;
import com.mrbysco.angrymobs.tweaks.LookAtEntityTweak;
import com.mrbysco.angrymobs.tweaks.MeleeAttackTweak;
import com.mrbysco.angrymobs.tweaks.ProjectileAttackTweak;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class TweakTypeRegistry {
	public static void onNewRegistry(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(AttackNearestTweak.REGISTRY_KEY, AttackNearestTweak.DIRECT_CODEC, AttackNearestTweak.DIRECT_CODEC);
		event.dataPackRegistry(AvoidEntityTweak.REGISTRY_KEY, AvoidEntityTweak.DIRECT_CODEC, AvoidEntityTweak.DIRECT_CODEC);
		event.dataPackRegistry(BreakDoorTweak.REGISTRY_KEY, BreakDoorTweak.DIRECT_CODEC, BreakDoorTweak.DIRECT_CODEC);
		event.dataPackRegistry(HurtByTargetTweak.REGISTRY_KEY, HurtByTargetTweak.DIRECT_CODEC, HurtByTargetTweak.DIRECT_CODEC);
		event.dataPackRegistry(LeapAtTargetTweak.REGISTRY_KEY, LeapAtTargetTweak.DIRECT_CODEC, LeapAtTargetTweak.DIRECT_CODEC);
		event.dataPackRegistry(LookAtEntityTweak.REGISTRY_KEY, LookAtEntityTweak.DIRECT_CODEC, LookAtEntityTweak.DIRECT_CODEC);
		event.dataPackRegistry(MeleeAttackTweak.REGISTRY_KEY, MeleeAttackTweak.DIRECT_CODEC, MeleeAttackTweak.DIRECT_CODEC);
		event.dataPackRegistry(ProjectileAttackTweak.REGISTRY_KEY, ProjectileAttackTweak.DIRECT_CODEC, ProjectileAttackTweak.DIRECT_CODEC);

		AngryMobs.LOGGER.info("Registered Tweak registries");
	}

	public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS = DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, AngryMobs.MOD_ID);
	public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<ConfigEnabledCondition>> CONFIG_ENABLED = CONDITION_CODECS.register("config_enabled", () -> ConfigEnabledCondition.CODEC);

}
