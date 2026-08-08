package com.mrbysco.angrymobs.datagen;

import com.mrbysco.angrymobs.AngryMobs;
import com.mrbysco.angrymobs.datagen.provider.TweakProvider;
import com.mrbysco.angrymobs.registry.condition.ConfigDefault;
import com.mrbysco.angrymobs.registry.condition.ConfigEnabledCondition;
import com.mrbysco.angrymobs.tweaks.AttackNearestTweak;
import com.mrbysco.angrymobs.tweaks.HurtByTargetTweak;
import com.mrbysco.angrymobs.tweaks.LeapAtTargetTweak;
import com.mrbysco.angrymobs.tweaks.MeleeAttackTweak;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class AngryDatagen {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();

		generator.addProvider(true, new AngryLanguageProvider(packOutput));

		generator.addProvider(true, new AngryTweaks(packOutput, event.getLookupProvider()));
	}

	public static class AngryLanguageProvider extends LanguageProvider {
		public AngryLanguageProvider(PackOutput packOutput) {
			super(packOutput, AngryMobs.MOD_ID, "en_us");
		}

		@Override
		protected void addTranslations() {
			addConfig("title", "Angry Mobs Config", null);
			addConfig("Common", "Common settings", null);
			addConfig("angryAnimals", "Angry Animals", "When true makes all vanilla animals attack you if you hurt them");
			addConfig("aggressiveAnimals", "Aggressive Animals", "When true makes all vanilla animals attack you on sight");
			addConfig("useAttributes", "Use Attributes", "When true makes the melee goals add the mobs attack damage and knockback onto the specified values");
		}

		/**
		 * Add the translation for a config entry
		 *
		 * @param path        The path of the config entry
		 * @param name        The name of the config entry
		 * @param description The description of the config entry (optional in case of targeting "title" or similar entries that have no tooltip)
		 */
		private void addConfig(String path, String name, @Nullable String description) {
			this.add("angrymobs.configuration." + path, name);
			if (description != null && !description.isEmpty())
				this.add("angrymobs.configuration." + path + ".tooltip", description);
		}
	}

	public static class AngryTweaks extends TweakProvider {
		public AngryTweaks(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
			super(packOutput, lookupProvider, AngryMobs.MOD_ID);
		}

		@Override
		protected void start() {
			var eitherCondition = new ConfigEnabledCondition(List.of(ConfigDefault.ANGRY_ANIMALS, ConfigDefault.AGGRESSIVE_ANIMALS));
			addMeleeAttackTweak("chicken_melee", new MeleeAttackTweak(EntityTypes.CHICKEN, 1, 0.95D, 1.0F, false), eitherCondition);
			addLeapAtTargetTweak("chicken_leaps", new LeapAtTargetTweak(EntityTypes.CHICKEN, 4, 0.3F), eitherCondition);
			addMeleeAttackTweak("cow_melee", new MeleeAttackTweak(EntityTypes.COW, 1, 1.0D, 2.0F, false), eitherCondition);
			addMeleeAttackTweak("donkey_melee", new MeleeAttackTweak(EntityTypes.DONKEY, 1, 1.5D, 3.0F, false), eitherCondition);
			addMeleeAttackTweak("horse_melee", new MeleeAttackTweak(EntityTypes.HORSE, 1, 1.5D, 3.0F, false), eitherCondition);
			addMeleeAttackTweak("mooshroom_melee", new MeleeAttackTweak(EntityTypes.MOOSHROOM, 1, 1.0D, 2.0F, false), eitherCondition);
			addMeleeAttackTweak("mule_melee", new MeleeAttackTweak(EntityTypes.MULE, 1, 1.5D, 3.0F, false), eitherCondition);
			addMeleeAttackTweak("pig_melee", new MeleeAttackTweak(EntityTypes.PIG, 1, 1.0D, 2.0F, false), eitherCondition);
			addMeleeAttackTweak("rabbit_melee", new MeleeAttackTweak(EntityTypes.RABBIT, 1, 1.4D, 1.0F, false), eitherCondition);
			addMeleeAttackTweak("sheep_melee", new MeleeAttackTweak(EntityTypes.SHEEP, 1, 1.0D, 2.0F, false), eitherCondition);

			var angryCondition = new ConfigEnabledCondition(List.of(ConfigDefault.ANGRY_ANIMALS));
			addHurtByTargetTweak("chicken_hurt_by_player", new HurtByTargetTweak(EntityTypes.CHICKEN, 1, false), angryCondition);
			addHurtByTargetTweak("cow_hurt_by_player", new HurtByTargetTweak(EntityTypes.COW, 1, false), angryCondition);
			addHurtByTargetTweak("donkey_hurt_by_player", new HurtByTargetTweak(EntityTypes.DONKEY, 1, false), angryCondition);
			addHurtByTargetTweak("horse_hurt_by_player", new HurtByTargetTweak(EntityTypes.HORSE, 1, false), angryCondition);
			addHurtByTargetTweak("mooshroom_hurt_by_player", new HurtByTargetTweak(EntityTypes.MOOSHROOM, 1, false), angryCondition);
			addHurtByTargetTweak("mule_hurt_by_player", new HurtByTargetTweak(EntityTypes.MULE, 1, false), angryCondition);
			addHurtByTargetTweak("pig_hurt_by_player", new HurtByTargetTweak(EntityTypes.PIG, 1, false), angryCondition);
			addHurtByTargetTweak("rabbit_hurt_by_player", new HurtByTargetTweak(EntityTypes.RABBIT, 1, false), angryCondition);
			addHurtByTargetTweak("sheep_hurt_by_player", new HurtByTargetTweak(EntityTypes.SHEEP, 1, false), angryCondition);

			var aggressiveCondition = new ConfigEnabledCondition(List.of(ConfigDefault.AGGRESSIVE_ANIMALS));
			addAttackNearestTweak("cat_attack_nearest_player", new AttackNearestTweak(EntityTypes.CAT, EntityTypes.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("chicken_attack_nearest_player", new AttackNearestTweak(EntityTypes.CHICKEN, EntityTypes.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("cow_attack_nearest_player", new AttackNearestTweak(EntityTypes.COW, EntityTypes.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("donkey_attack_nearest_player", new AttackNearestTweak(EntityTypes.DONKEY, EntityTypes.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("fox_attack_nearest_player", new AttackNearestTweak(EntityTypes.FOX, EntityTypes.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("horse_attack_nearest_player", new AttackNearestTweak(EntityTypes.HORSE, EntityTypes.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("llama_attack_nearest_player", new AttackNearestTweak(EntityTypes.LLAMA, EntityTypes.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("mule_attack_nearest_player", new AttackNearestTweak(EntityTypes.MULE, EntityTypes.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("panda_attack_nearest_player", new AttackNearestTweak(EntityTypes.PANDA, EntityTypes.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("pig_attack_nearest_player", new AttackNearestTweak(EntityTypes.PIG, EntityTypes.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("sheep_attack_nearest_player", new AttackNearestTweak(EntityTypes.SHEEP, EntityTypes.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("trader_llama_attack_nearest_player", new AttackNearestTweak(EntityTypes.TRADER_LLAMA, EntityTypes.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("wolf_attack_nearest_player", new AttackNearestTweak(EntityTypes.WOLF, EntityTypes.PLAYER, 2, true), aggressiveCondition);
		}
	}
}
