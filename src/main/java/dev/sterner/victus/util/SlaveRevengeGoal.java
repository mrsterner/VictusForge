package dev.sterner.victus.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class SlaveRevengeGoal extends HurtByTargetGoal {

    private final Entity owner;

    public SlaveRevengeGoal(PathfinderMob mob, Entity owner) {
        super(mob);
        this.owner = owner;
    }

    @Override
    public boolean canUse() {
        return this.mob.getLastHurtByMob() != owner && super.canUse();
    }
}