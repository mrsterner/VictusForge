package dev.sterner.victus.mixin;

import dev.sterner.victus.util.WohooHeartGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Evoker.class)
public abstract class EvokerMixin extends Mob {
    protected EvokerMixin(EntityType<? extends Mob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void injectAspectGoal(CallbackInfo ci) {
        final var wohooGoal = new WohooHeartGoal((Evoker) (Object) this);
        this.goalSelector.addGoal(2, wohooGoal);
        this.targetSelector.addGoal(1, wohooGoal.lookGoal);
    }
}
