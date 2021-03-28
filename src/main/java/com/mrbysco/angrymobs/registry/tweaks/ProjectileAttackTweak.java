package com.mrbysco.angrymobs.registry.tweaks;

import com.mrbysco.angrymobs.AngryMobs;
import com.mrbysco.angrymobs.handler.goals.ThrowableAttackGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
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

    public ProjectileAttackTweak(EntityType<? extends MobEntity> entity, EntityType<? extends ProjectileEntity> throwableType, SoundEvent soundEvent, int priority, float attackDamage, float velocity) {
        this(entity.getRegistryName(), throwableType.getRegistryName(), soundEvent.getRegistryName(), priority, attackDamage, velocity);
    }

    @Override
    public void adjust(Entity entity) {
        if(entity instanceof MobEntity) {
            MobEntity mob = (MobEntity) entity;
            Entity foundEntity = ForgeRegistries.ENTITIES.getValue(projectileEntityLocation).create(entity.world);
            if(foundEntity instanceof ProjectileEntity) {
                mob.goalSelector.goals.removeIf(goal -> goal.getGoal() instanceof PanicGoal);

                mob.goalSelector.goals.forEach(goal -> {
                    if(goal.getGoal() instanceof RangedBowAttackGoal) {
                        AngryMobs.LOGGER.info(String.format("Removing existing AI to apply the AI tweak of ID %s for entity %s", getEntityLocation(), getName()));
                    }
                });
                mob.goalSelector.goals.removeIf(goal -> goal.getGoal() instanceof RangedBowAttackGoal);

                ProjectileEntity throwable = (ProjectileEntity)foundEntity ;
                SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(soundLocation);
                mob.targetSelector.addGoal(goalPriority, new ThrowableAttackGoal(mob, (EntityType<? extends ProjectileEntity>) throwable.getType(), () -> sound, attackDamage, velocity));
                foundEntity.remove();
            } else {
                AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Projectile entity isn't valid for the tweak", getName(), getEntityLocation()));
            }
        } else {
            AngryMobs.LOGGER.error(String.format("Can't apply AI tweak of ID %s for entity %s. Entity isn't valid for the tweak", getName(), getEntityLocation()));
        }
    }
}
