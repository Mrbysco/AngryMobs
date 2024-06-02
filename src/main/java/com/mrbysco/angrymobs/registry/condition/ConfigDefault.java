package com.mrbysco.angrymobs.registry.condition;

import com.mojang.serialization.Codec;
import com.mrbysco.angrymobs.config.AngryConfig;
import net.minecraft.util.StringRepresentable;

import java.util.function.BooleanSupplier;

public enum ConfigDefault implements StringRepresentable {
	ANGRY_ANIMALS("angry_animals", AngryConfig.COMMON.angryAnimals::get),
	AGGRESSIVE_ANIMALS("aggressive_animals", AngryConfig.COMMON.aggressiveAnimals::get);

	public static final Codec<ConfigDefault> CODEC = StringRepresentable.fromEnum(ConfigDefault::values);
	private final String name;
	private final BooleanSupplier configSupplier;

	ConfigDefault(String name, BooleanSupplier supplier) {
		this.name = name;
		this.configSupplier = supplier;
	}

	public boolean isEnabled() {
		return this.configSupplier.getAsBoolean();
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}
}
