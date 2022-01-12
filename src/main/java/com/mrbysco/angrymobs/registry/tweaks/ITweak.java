package com.mrbysco.angrymobs.registry.tweaks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public interface ITweak {
    /*
     * What action happens when the event is triggered
     */
    void adjust(Entity entity);

    /*
     * @return Unique name for the AI Tweak
     */
    String getName();

    /*
     * @return ResourceLocation of the entity being tweaked
     */
    ResourceLocation getEntityLocation();
}
