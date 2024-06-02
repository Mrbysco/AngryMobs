package com.mrbysco.angrymobs.registry.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.angrymobs.registry.TweakTypeRegistry;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.List;

/**
 * Condition that checks if any of the given configs are enabled
 *
 * @param configs List of configs to check
 */
public record ConfigEnabledCondition(List<ConfigDefault> configs) implements ICondition {

	public static final Codec<ConfigEnabledCondition> CODEC = RecordCodecBuilder.create(
			builder -> builder
					.group(
							ConfigDefault.CODEC.listOf().fieldOf("config").forGetter(ConfigEnabledCondition::configs))
					.apply(builder, ConfigEnabledCondition::new));

	@Override
	public boolean test(IContext context) {
		for (ConfigDefault config : configs) {
			if (config.isEnabled()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Codec<? extends ICondition> codec() {
		return TweakTypeRegistry.CONFIG_ENABLED.get();
	}

	@Override
	public String toString() {
		return "config_enabled";
	}
}
