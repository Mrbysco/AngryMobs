package com.mrbysco.angrymobs.handler;

import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.ITweak;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class AIHandler {
    @SubscribeEvent
    public void onEntityCreation(EntityJoinWorldEvent event) {
        if(!event.getWorld().isClientSide()) {
            AITweakRegistry tweakRegistry = AITweakRegistry.instance();
            ResourceLocation registryName = event.getEntity().getType().getRegistryName();
            if(tweakRegistry.containsEntity(registryName)) {
                final List<ITweak> tweakList = tweakRegistry.getTweaksFromType(registryName);
                for(ITweak tweak : tweakList) {
                    tweak.adjust(event.getEntity());
                }
            }
        }
    }
}