package com.mrbysco.angrymobs;

import com.mojang.logging.LogUtils;
import com.mrbysco.angrymobs.config.AngryConfig;
import com.mrbysco.angrymobs.handler.AIHandler;
import com.mrbysco.angrymobs.handler.AttributeHandler;
import com.mrbysco.angrymobs.registry.TweakReloadManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(AngryMobs.MOD_ID)
public class AngryMobs {
	public static final String MOD_ID = "angrymobs";
	public static final Logger LOGGER = LogUtils.getLogger();

	public AngryMobs(IEventBus eventBus) {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AngryConfig.commonSpec);
		eventBus.register(AngryConfig.class);

		eventBus.addListener(AttributeHandler::addEntityAttributes);

		NeoForge.EVENT_BUS.register(new TweakReloadManager());
		NeoForge.EVENT_BUS.register(new AIHandler());
	}
}
