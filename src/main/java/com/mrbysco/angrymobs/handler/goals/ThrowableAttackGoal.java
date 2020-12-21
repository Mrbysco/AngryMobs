package com.mrbysco.angrymobs.handler.goals;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;
import java.util.function.Supplier;

public class ThrowableAttackGoal extends Goal {
    private final MobEntity mob;
    private int attackStep;
    private int attackTime;
    private int firedRecentlyTimer;
    private final EntityType<? extends ProjectileEntity> projectile;
    private final float ATTACK_DAMAGE;
    private final float velocity;
    private final Supplier<SoundEvent> soundEventSupplier;

    public ThrowableAttackGoal(MobEntity mobEntity, EntityType<? extends ProjectileEntity> projectileType, Supplier<SoundEvent> soundEventSupplier, float attackDamage, float projectileVelocity) {
        this.mob = mobEntity;
        this.projectile = projectileType;
        this.soundEventSupplier = soundEventSupplier;
        this.ATTACK_DAMAGE = attackDamage;
        this.velocity = projectileVelocity;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute() {
        LivingEntity livingentity = this.mob.getAttackTarget();
        return livingentity != null && livingentity.isAlive() && this.mob.canAttack(livingentity);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.attackStep = 0;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.firedRecentlyTimer = 0;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        --this.attackTime;
        LivingEntity livingentity = this.mob.getAttackTarget();
        if (livingentity != null) {
            boolean flag = this.mob.getEntitySenses().canSee(livingentity);
            if (flag) {
                this.firedRecentlyTimer = 0;
            } else {
                ++this.firedRecentlyTimer;
            }

            double d0 = this.mob.getDistanceSq(livingentity);
            if (d0 < 4.0D) {
                if (!flag) {
                    return;
                }

                if (this.attackTime <= 0) {
                    this.attackTime = 20;
                    livingentity.attackEntityFrom(DamageSource.causeMobDamage(this.mob), ATTACK_DAMAGE);
                }

                this.mob.getMoveHelper().setMoveTo(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ(), 1.0D);
            } else if (d0 < this.getFollowDistance() * this.getFollowDistance() && flag) {
                if (this.attackTime <= 0) {
                    ++this.attackStep;
                    if (this.attackStep == 1) {
                        this.attackTime = 60;
                    } else if (this.attackStep <= 4) {
                        this.attackTime = 6;
                    } else {
                        this.attackTime = 100;
                        this.attackStep = 0;
                    }

                    if (this.attackStep > 1) {
                        if (soundEventSupplier.get() != null && !this.mob.isSilent()) {
                            this.mob.playSound(soundEventSupplier.get(), 1.0F, 1.0F + (this.mob.getRNG().nextFloat() - this.mob.getRNG().nextFloat()) * 0.4F);
                        }

                        for(int i = 0; i < 1; ++i) {
                            ProjectileEntity projectileEntity = projectile.create(this.mob.world);
                            if(projectileEntity != null) {
                                projectileEntity.setShooter(this.mob);
                                projectileEntity.rotationYaw = this.mob.rotationYaw % 360.0F;
                                projectileEntity.rotationPitch = this.mob.rotationPitch % 360.0F;
                                projectileEntity.setLocationAndAngles(this.mob.getPosX(), this.mob.getPosYEye() - (double)0.1F, this.mob.getPosZ(), this.mob.rotationYaw, this.mob.rotationPitch);

                                double projX = livingentity.getPosX() - this.mob.getPosX();
                                double projY = livingentity.getPosYHeight(0.3333333333333333D) - projectileEntity.getPosY();
                                double projZ = livingentity.getPosZ() - this.mob.getPosZ();
                                double projD3 = (double)MathHelper.sqrt(projX * projX + projZ * projZ);
                                projectileEntity.shoot(projX, projY + projD3 * (double)0.2F, projZ, this.velocity, (float)(14 - this.mob.world.getDifficulty().getId() * 4));

                                this.mob.world.addEntity(projectileEntity);
                            }
                        }
                    }
                }

                this.mob.getLookController().setLookPositionWithEntity(livingentity, 10.0F, 10.0F);
            } else if (this.firedRecentlyTimer < 5) {
                this.mob.getMoveHelper().setMoveTo(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ(), 1.0D);
            }

            super.tick();
        }
    }

    private double getFollowDistance() {
        return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
    }
}