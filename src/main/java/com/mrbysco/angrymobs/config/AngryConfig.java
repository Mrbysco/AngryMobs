package com.mrbysco.angrymobs.config;

import com.mrbysco.angrymobs.AngryMobs;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class AngryConfig {
	public static class Common {
		public final BooleanValue angryAnimals;
		public final BooleanValue angryReinforcements;
		public final BooleanValue aggressiveAnimals;
		public final BooleanValue useAttributes;

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("Common settings")
					.push("Common");

			angryAnimals = builder
					.comment("When true makes all vanilla animals attack you if you hurt them")
					.define("angryAnimals", false);

			angryReinforcements = builder
					.comment("When true makes all vanilla animals call for reinforcement when you hurt them")
					.define("angryReinforcements", false);

			aggressiveAnimals = builder
					.comment("When true makes all vanilla animals attack you on sight")
					.define("aggressiveAnimals", false);

			useAttributes = builder
					.comment("When true makes the melee goals add the mobs attack damage and knockback onto the specified values")
					.define("useAttributes", true);

			builder.pop();
		}
	}

	public static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		AngryMobs.LOGGER.debug("Loaded Angry Mobs' config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		AngryMobs.LOGGER.warn("Angry Mobs' config just got changed on the file system!");
	}
}
