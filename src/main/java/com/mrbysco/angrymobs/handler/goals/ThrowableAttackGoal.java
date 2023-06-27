package com.mrbysco.angrymobs.handler.goals;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.Projectile;

import java.util.EnumSet;
import java.util.function.Supplier;

public class ThrowableAttackGoal extends Goal {
	private final Mob mob;
	private int attackStep;
	private int attackTime;
	private int firedRecentlyTimer;
	private final EntityType<? extends Projectile> projectile;
	private final float ATTACK_DAMAGE;
	private final float velocity;
	private final Supplier<SoundEvent> soundEventSupplier;

	public ThrowableAttackGoal(Mob mobEntity, EntityType<? extends Projectile> projectileType, Supplier<SoundEvent> soundEventSupplier, float attackDamage, float projectileVelocity) {
		this.mob = mobEntity;
		this.projectile = projectileType;
		this.soundEventSupplier = soundEventSupplier;
		this.ATTACK_DAMAGE = attackDamage;
		this.velocity = projectileVelocity;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	 * method as well.
	 */
	public boolean canUse() {
		LivingEntity livingentity = this.mob.getTarget();
		return livingentity != null && livingentity.isAlive() && this.mob.canAttack(livingentity);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		this.attackStep = 0;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void stop() {
		this.firedRecentlyTimer = 0;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		--this.attackTime;
		LivingEntity livingentity = this.mob.getTarget();
		if (livingentity != null) {
			boolean flag = this.mob.getSensing().hasLineOfSight(livingentity);
			if (flag) {
				this.firedRecentlyTimer = 0;
			} else {
				++this.firedRecentlyTimer;
			}

			double d0 = this.mob.distanceToSqr(livingentity);
			if (d0 < 4.0D) {
				if (!flag) {
					return;
				}

				if (this.attackTime <= 0) {
					this.attackTime = 20;
					livingentity.hurt(livingentity.damageSources().mobAttack(this.mob), ATTACK_DAMAGE);
				}

				this.mob.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0D);
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
							this.mob.playSound(soundEventSupplier.get(), 1.0F, 1.0F + (this.mob.getRandom().nextFloat() - this.mob.getRandom().nextFloat()) * 0.4F);
						}

						for (int i = 0; i < 1; ++i) {
							Projectile projectileEntity = projectile.create(this.mob.level);
							if (projectileEntity != null) {
								projectileEntity.setOwner(this.mob);
								projectileEntity.setYRot(this.mob.getYRot() % 360.0F);
								projectileEntity.setXRot(this.mob.getXRot() % 360.0F);
								projectileEntity.moveTo(this.mob.getX(), this.mob.getEyeY() - (double) 0.1F, this.mob.getZ(), this.mob.getYRot(), this.mob.getXRot());

								double projX = livingentity.getX() - this.mob.getX();
								double projY = livingentity.getY(0.3333333333333333D) - projectileEntity.getY();
								double projZ = livingentity.getZ() - this.mob.getZ();
								double projD3 = Math.sqrt(projX * projX + projZ * projZ);
								projectileEntity.shoot(projX, projY + projD3 * (double) 0.2F, projZ, this.velocity, (float) (14 - this.mob.level.getDifficulty().getId() * 4));

								this.mob.level.addFreshEntity(projectileEntity);
							}
						}
					}
				}

				this.mob.getLookControl().setLookAt(livingentity, 10.0F, 10.0F);
			} else if (this.firedRecentlyTimer < 5) {
				this.mob.getMoveControl().setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1.0D);
			}

			super.tick();
		}
	}

	private double getFollowDistance() {
		return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
	}
}