package com.mrbysco.angrymobs.handler.goals;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class MobMeleeAttackGoal extends Goal {
	protected final Mob attacker;
	private final double speedTowardsTarget;
	private final boolean longMemory;
	private Path path;
	private double targetX;
	private double targetY;
	private double targetZ;
	private int delayCounter;
	private int ticksUntilNextAttack;
	private long lastCanUseCheck;
	private int failedPathFindingPenalty = 0;
	private boolean canPenalize = false;
	private final float ATTACK_DAMAGE;

	public MobMeleeAttackGoal(Mob creature, double speedIn, float damage, boolean useLongMemory) {
		this.attacker = creature;
		this.speedTowardsTarget = speedIn;
		this.ATTACK_DAMAGE = damage;
		this.longMemory = useLongMemory;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	 * method as well.
	 */
	public boolean canUse() {
		long i = this.attacker.level.getGameTime();
		if (i - this.lastCanUseCheck < 20L) {
			return false;
		} else {
			this.lastCanUseCheck = i;
			LivingEntity livingentity = this.attacker.getTarget();
			if (livingentity == null) {
				return false;
			} else if (!livingentity.isAlive()) {
				return false;
			} else {
				if (canPenalize) {
					if (--this.delayCounter <= 0) {
						this.path = this.attacker.getNavigation().createPath(livingentity, 0);
						this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);
						return this.path != null;
					} else {
						return true;
					}
				}
				this.path = this.attacker.getNavigation().createPath(livingentity, 0);
				if (this.path != null) {
					return true;
				} else {
					return this.getAttackReachSqr(livingentity) >= this.attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
				}
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean canContinueToUse() {
		LivingEntity livingentity = this.attacker.getTarget();
		if (livingentity == null) {
			return false;
		} else if (!livingentity.isAlive()) {
			return false;
		} else if (!this.longMemory) {
			return !this.attacker.getNavigation().isDone();
		} else if (!this.attacker.isWithinRestriction(livingentity.blockPosition())) {
			return false;
		} else {
			return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player) livingentity).isCreative();
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		this.attacker.getNavigation().moveTo(this.path, this.speedTowardsTarget);
		this.attacker.setAggressive(true);
		this.delayCounter = 0;
		this.ticksUntilNextAttack = 0;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void stop() {
		LivingEntity livingentity = this.attacker.getTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
			this.attacker.setTarget((LivingEntity) null);
		}

		this.attacker.setAggressive(false);
		this.attacker.getNavigation().stop();
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		LivingEntity livingentity = this.attacker.getTarget();
		if (livingentity != null) {
			this.attacker.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
			double d0 = this.attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
			this.delayCounter = Math.max(this.delayCounter - 1, 0);
			if ((this.longMemory || this.attacker.getSensing().hasLineOfSight(livingentity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRandom().nextFloat() < 0.05F)) {
				this.targetX = livingentity.getX();
				this.targetY = livingentity.getY();
				this.targetZ = livingentity.getZ();
				this.delayCounter = 4 + this.attacker.getRandom().nextInt(7);
				if (this.canPenalize) {
					this.delayCounter += failedPathFindingPenalty;
					if (this.attacker.getNavigation().getPath() != null) {
						net.minecraft.world.level.pathfinder.Node finalPathPoint = this.attacker.getNavigation().getPath().getEndNode();
						if (finalPathPoint != null && livingentity.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
							failedPathFindingPenalty = 0;
						else
							failedPathFindingPenalty += 10;
					} else {
						failedPathFindingPenalty += 10;
					}
				}
				if (d0 > 1024.0D) {
					this.delayCounter += 10;
				} else if (d0 > 256.0D) {
					this.delayCounter += 5;
				}

				if (!this.attacker.getNavigation().moveTo(livingentity, this.speedTowardsTarget)) {
					this.delayCounter += 15;
				}
			}

			this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
			this.checkAndPerformAttack(livingentity, d0);
		}
	}

	protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
		double d0 = this.getAttackReachSqr(enemy);
		if (distToEnemySqr <= d0 && this.getTicksUntilNextAttack() <= 0) {
			this.resetAttackCooldown();
			this.attacker.swing(InteractionHand.MAIN_HAND);
			enemy.hurt(DamageSource.mobAttack(this.attacker), ATTACK_DAMAGE);
		}
	}

	protected void resetAttackCooldown() {
		this.ticksUntilNextAttack = 20;
	}

	protected boolean isTimeToAttack() {
		return this.ticksUntilNextAttack <= 0;
	}

	protected int getTicksUntilNextAttack() {
		return this.ticksUntilNextAttack;
	}

	protected int getAttackInterval() {
		return 20;
	}

	protected double getAttackReachSqr(LivingEntity attackTarget) {
		return (double) (this.attacker.getBbWidth() * 2.0F * this.attacker.getBbWidth() * 2.0F + attackTarget.getBbWidth());
	}
}
