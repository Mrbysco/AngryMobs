package com.mrbysco.angrymobs;

import com.mojang.logging.LogUtils;
import com.mrbysco.angrymobs.config.AngryConfig;
import com.mrbysco.angrymobs.handler.AIHandler;
import com.mrbysco.angrymobs.handler.AttributeHandler;
import com.mrbysco.angrymobs.registry.TweakReloadManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(AngryMobs.MOD_ID)
public class AngryMobs {
	public static final String MOD_ID = "angrymobs";
	public static final Logger LOGGER = LogUtils.getLogger();

	public AngryMobs() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AngryConfig.commonSpec);
		eventBus.register(AngryConfig.class);

		eventBus.addListener(AttributeHandler::addEntityAttributes);

		MinecraftForge.EVENT_BUS.register(new TweakReloadManager());
		MinecraftForge.EVENT_BUS.register(new AIHandler());
	}
}
