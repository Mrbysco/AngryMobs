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
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class AngryDatagen {

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new AngryTweaks(packOutput, event.getLookupProvider()));
		}
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
			addMeleeAttackTweak("chicken_melee", new MeleeAttackTweak(EntityType.CHICKEN, 1, 0.95D, 1.0F, false), eitherCondition);
			addLeapAtTargetTweak("chicken_leaps", new LeapAtTargetTweak(EntityType.CHICKEN, 4, 0.3F), eitherCondition);
			addMeleeAttackTweak("cow_melee", new MeleeAttackTweak(EntityType.COW, 1, 1.0D, 2.0F, false), eitherCondition);
			addMeleeAttackTweak("donkey_melee", new MeleeAttackTweak(EntityType.DONKEY, 1, 1.5D, 3.0F, false), eitherCondition);
			addMeleeAttackTweak("horse_melee", new MeleeAttackTweak(EntityType.HORSE, 1, 1.5D, 3.0F, false), eitherCondition);
			addMeleeAttackTweak("mooshroom_melee", new MeleeAttackTweak(EntityType.MOOSHROOM, 1, 1.0D, 2.0F, false), eitherCondition);
			addMeleeAttackTweak("mule_melee", new MeleeAttackTweak(EntityType.MULE, 1, 1.5D, 3.0F, false), eitherCondition);
			addMeleeAttackTweak("pig_melee", new MeleeAttackTweak(EntityType.PIG, 1, 1.0D, 2.0F, false), eitherCondition);
			addMeleeAttackTweak("rabbit_melee", new MeleeAttackTweak(EntityType.RABBIT, 1, 1.4D, 1.0F, false), eitherCondition);
			addMeleeAttackTweak("sheep_melee", new MeleeAttackTweak(EntityType.SHEEP, 1, 1.0D, 2.0F, false), eitherCondition);

			var angryCondition = new ConfigEnabledCondition(List.of(ConfigDefault.ANGRY_ANIMALS));
			addHurtByTargetTweak("chicken_hurt_by_player", new HurtByTargetTweak(EntityType.CHICKEN, 1, false), angryCondition);
			addHurtByTargetTweak("cow_hurt_by_player", new HurtByTargetTweak(EntityType.COW, 1, false), angryCondition);
			addHurtByTargetTweak("donkey_hurt_by_player", new HurtByTargetTweak(EntityType.DONKEY, 1, false), angryCondition);
			addHurtByTargetTweak("horse_hurt_by_player", new HurtByTargetTweak(EntityType.HORSE, 1, false), angryCondition);
			addHurtByTargetTweak("mooshroom_hurt_by_player", new HurtByTargetTweak(EntityType.MOOSHROOM, 1, false), angryCondition);
			addHurtByTargetTweak("mule_hurt_by_player", new HurtByTargetTweak(EntityType.MULE, 1, false), angryCondition);
			addHurtByTargetTweak("pig_hurt_by_player", new HurtByTargetTweak(EntityType.PIG, 1, false), angryCondition);
			addHurtByTargetTweak("rabbit_hurt_by_player", new HurtByTargetTweak(EntityType.RABBIT, 1, false), angryCondition);
			addHurtByTargetTweak("sheep_hurt_by_player", new HurtByTargetTweak(EntityType.SHEEP, 1, false), angryCondition);

			var aggressiveCondition = new ConfigEnabledCondition(List.of(ConfigDefault.ANGRY_ANIMALS));
			addAttackNearestTweak("cat_attack_nearest_player", new AttackNearestTweak(EntityType.CAT, EntityType.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("chicken_attack_nearest_player", new AttackNearestTweak(EntityType.CHICKEN, EntityType.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("cow_attack_nearest_player", new AttackNearestTweak(EntityType.COW, EntityType.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("donkey_attack_nearest_player", new AttackNearestTweak(EntityType.DONKEY, EntityType.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("fox_attack_nearest_player", new AttackNearestTweak(EntityType.FOX, EntityType.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("horse_attack_nearest_player", new AttackNearestTweak(EntityType.HORSE, EntityType.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("llama_attack_nearest_player", new AttackNearestTweak(EntityType.LLAMA, EntityType.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("mule_attack_nearest_player", new AttackNearestTweak(EntityType.MULE, EntityType.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("panda_attack_nearest_player", new AttackNearestTweak(EntityType.PANDA, EntityType.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("pig_attack_nearest_player", new AttackNearestTweak(EntityType.PIG, EntityType.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("sheep_attack_nearest_player", new AttackNearestTweak(EntityType.SHEEP, EntityType.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("trader_llama_attack_nearest_player", new AttackNearestTweak(EntityType.TRADER_LLAMA, EntityType.PLAYER, 2, true), aggressiveCondition);
			addAttackNearestTweak("wolf_attack_nearest_player", new AttackNearestTweak(EntityType.WOLF, EntityType.PLAYER, 2, true), aggressiveCondition);
		}
	}
}
