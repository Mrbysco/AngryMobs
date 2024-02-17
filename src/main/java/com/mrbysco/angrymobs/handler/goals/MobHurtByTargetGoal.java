package com.mrbysco.angrymobs.handler.goals;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

public class MobHurtByTargetGoal extends TargetGoal {
	private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
	private boolean entityCallsForHelp;
	/**
	 * Store the previous revengeTimer value
	 */
	private int revengeTimerOld;
	private final Class<?>[] excludedReinforcementTypes;
	private Class<?>[] reinforcementTypes;

	public MobHurtByTargetGoal(Mob mobEntityIn, Class<?>... excludeReinforcementTypes) {
		super(mobEntityIn, true);
		this.excludedReinforcementTypes = excludeReinforcementTypes;
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	 * method as well.
	 */
	public boolean canUse() {
		int i = this.mob.getLastHurtByMobTimestamp();
		LivingEntity livingentity = this.mob.getLastHurtByMob();
		if (i != this.revengeTimerOld && livingentity != null) {
			if (livingentity.getType() == EntityType.PLAYER && this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
				return false;
			} else {
				for (Class<?> oclass : this.excludedReinforcementTypes) {
					if (oclass.isAssignableFrom(livingentity.getClass())) {
						return false;
					}
				}

				return this.canAttack(livingentity, HURT_BY_TARGETING);
			}
		} else {
			return false;
		}
	}

	public MobHurtByTargetGoal setCallsForHelp(Class<?>... reinforcementTypes) {
		this.entityCallsForHelp = true;
		this.reinforcementTypes = reinforcementTypes;
		return this;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		this.mob.setTarget(this.mob.getLastHurtByMob());
		this.targetMob = this.mob.getTarget();
		this.revengeTimerOld = this.mob.getLastHurtByMobTimestamp();
		this.unseenMemoryTicks = 300;
		if (this.entityCallsForHelp) {
			this.alertOthers();
		}

		super.start();
	}

	protected void alertOthers() {
		double d0 = this.getFollowDistance();
		AABB axisalignedbb = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(d0, 10.0D, d0);
		List<? extends Mob> list = this.mob.level().getEntitiesOfClass(this.mob.getClass(), axisalignedbb);
		Iterator<? extends Mob> iterator = list.iterator();

		while (true) {
			Mob mobentity;
			while (true) {
				if (!iterator.hasNext()) {
					return;
				}

				mobentity = iterator.next();
				if (this.mob != mobentity && mobentity.getTarget() == null && (!(this.mob instanceof TamableAnimal) || ((TamableAnimal) this.mob).getOwner() == ((TamableAnimal) mobentity).getOwner()) && (this.mob.getLastHurtByMob() == null || !mobentity.isAlliedTo(this.mob.getLastHurtByMob()))) {
					if (this.reinforcementTypes == null) {
						break;
					}

					boolean flag = false;

					for (Class<?> oclass : this.reinforcementTypes) {
						if (mobentity.getClass() == oclass) {
							flag = true;
							break;
						}
					}

					if (!flag) {
						break;
					}
				}
			}

			this.setAttackTarget(mobentity, this.mob.getLastHurtByMob());
		}
	}

	protected void setAttackTarget(Mob mobIn, LivingEntity targetIn) {
		mobIn.setTarget(targetIn);
	}
}