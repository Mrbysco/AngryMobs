package com.mrbysco.angrymobs.compat.ct;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.mrbysco.angrymobs.registry.AITweakRegistry;
import com.mrbysco.angrymobs.registry.tweaks.ProjectileAttackTweak;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class ActionAddProjectileAttackTweak implements IRuntimeAction {
	public final ProjectileAttackTweak projectileTweak;

	public ActionAddProjectileAttackTweak(EntityType entity, EntityType projectileEntity, String soundLocation, int priority, float attackDamage, float velocity) {
		this.projectileTweak = new ProjectileAttackTweak(ForgeRegistries.ENTITY_TYPES.getKey(entity), ForgeRegistries.ENTITY_TYPES.getKey(projectileEntity), new ResourceLocation(soundLocation),
				priority, attackDamage, velocity);
	}

	@Override
	public void apply() {
		AITweakRegistry.instance().registerTweak(projectileTweak);
	}

	@Override
	public String describe() {
		return String.format("Added %s tweak for Entity %s", projectileTweak.getName(), projectileTweak.getEntityLocation());
	}
}
