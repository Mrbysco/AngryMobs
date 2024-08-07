package com.mrbysco.angrymobs;

import com.mojang.logging.LogUtils;
import com.mrbysco.angrymobs.config.AngryConfig;
import com.mrbysco.angrymobs.handler.AIHandler;
import com.mrbysco.angrymobs.handler.AttributeHandler;
import com.mrbysco.angrymobs.registry.TweakTypeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(AngryMobs.MOD_ID)
public class AngryMobs {
	public static final String MOD_ID = "angrymobs";
	public static final Logger LOGGER = LogUtils.getLogger();

	public AngryMobs(IEventBus eventBus, ModContainer container, Dist dist) {
		container.registerConfig(ModConfig.Type.COMMON, AngryConfig.commonSpec);
		eventBus.register(AngryConfig.class);

		TweakTypeRegistry.CONDITION_CODECS.register(eventBus);

		eventBus.addListener(TweakTypeRegistry::onNewRegistry);
		eventBus.addListener(AttributeHandler::addEntityAttributes);

		NeoForge.EVENT_BUS.register(new AIHandler());

		if (dist.isClient()) {
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}
	}

	public static ResourceLocation modLoc(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
