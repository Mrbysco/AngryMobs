package com.mrbysco.angrymobs;

import com.mrbysco.angrymobs.config.AngryConfig;
import com.mrbysco.angrymobs.handler.AIHandler;
import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.TweakReloadManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(AngryMobs.MOD_ID)
public class AngryMobs {
    public static final String MOD_ID = "angrymobs";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public AngryMobs() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AngryConfig.serverSpec);
        eventBus.register(AngryConfig.class);

        MinecraftForge.EVENT_BUS.register(new TweakReloadManager());
        MinecraftForge.EVENT_BUS.register(new AIHandler());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void serverStart(FMLServerStartingEvent event) {
        AITweakRegistry.instance().initializeTweaks();
    }
}
