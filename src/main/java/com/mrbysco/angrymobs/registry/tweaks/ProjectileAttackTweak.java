package com.mrbysco.angrymobs.registry.tweaks;

import com.mrbysco.angrymobs.AngryMobs;
import com.mrbysco.angrymobs.handler.goals.ThrowableAttackGoal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.registries.ForgeRegistries;

public class ProjectileAttackTweak extends BaseTweak {
	protected final ResourceLocation projectileEntityLocation;
	protected final ResourceLocation soundLocation;
	protected final int goalPriority;
	protected final float attackDamage;
	protected final float velocity;

	public ProjectileAttackTweak(ResourceLocation entity, ResourceLocation projectileEntity, ResourceLocation soundLocation, int priority, float attackDamage, float velocity) {
		super("throwing_" + projectileEntity.getNamespace(), entity);
		this.projectileEntityLocation = projectileEntity;
		this.soundLocation = soundLocation;
		this.goalPriority = priority;
		this.attackDamage = attackDamage;
		this.velocity = velocity;
	}

	public ProjectileAttackTweak(EntityType<? extends Mob> entity, EntityType<? extends Projectile> throwableType, SoundEvent soundEvent, int priority, float attackDamage, float velocity) {
		this(ForgeRegistries.ENTITY_TYPES.getKey(entity), ForgeRegistries.ENTITY_TYPES.getKey(throwableType), ForgeRegistries.SOUND_EVENTS.getKey(soundEvent), priority, attackDamage, velocity);
	}

	@Override
	public void adjust(Entity entity) {
		if (entity instanceof Mob mob) {
			EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(projectileEntityLocation);
			if (type != null) {
				Entity foundEntity = type.create(entity.level);
				if (foundEntity instanceof Projectile throwable) {
					mob.goalSelector.availableGoals.removeIf(goal -> goal.getGoal() instanceof PanicGoal);

					mob.goalSelector.availableGoals.forEach(goal -> {
						if (goal.getGoal() instanceof RangedBowAttackGoal) {
							AngryMobs.LOGGER.info(String.format("Removing existing AI to apply the AI tweak of ID %s for entity %s", getEntityLocation(), getName()));
						}
					});
					mob.goalSelector.availableGoals.removeIf(goal -> goal.getGoal() instanceof RangedBowAttackGoal);

					SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(soundLocation);
					mob.targetSelector.addGoal(goalPriority, new ThrowableAttackGoal(mob, (EntityType<? extends Projectile>) throwable.getType(), () -> sound, attackDamage, velocity));
					foundEntity.discard();
				} else {
					AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Projectile entity isn't valid for the tweak", getName(), getEntityLocation()));
				}
			} else {
				AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Projectile entity could not be found", getName(), getEntityLocation()));
			}
		} else {
			AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity isn't valid for the tweak", getName(), getEntityLocation()));
		}
	}
}
