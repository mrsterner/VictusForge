package dev.sterner.victus.mixin;

import dev.sterner.victus.util.SlaveRevengeGoal;
import dev.sterner.victus.util.VictusVexExtension;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Vex.class)
public class VexMixin extends Mob implements VictusVexExtension {
    protected VexMixin(EntityType<? extends Mob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
    }

    @Override
    public void replaceTargetGoal(Player owner) {
        goalSelector.getAvailableGoals().removeIf(prioritizedGoal -> prioritizedGoal.getGoal() instanceof NearestAttackableTargetGoal<?> || prioritizedGoal.getGoal() instanceof HurtByTargetGoal);
        this.targetSelector.addGoal(1, new SlaveRevengeGoal((Vex) (Object) this, owner));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false,
                livingEntity -> livingEntity != owner && !(livingEntity instanceof Vex)));
    }
}
