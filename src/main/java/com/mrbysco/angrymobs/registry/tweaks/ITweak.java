package com.mrbysco.angrymobs.registry.tweaks;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

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
