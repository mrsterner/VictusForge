package dev.sterner.victus.mixin;

import dev.sterner.victus.capability.VictusEntityCapability;
import dev.sterner.victus.capability.VictusPlayerComponent;
import dev.sterner.victus.hearts.HeartAspect;
import dev.sterner.victus.hearts.content.IcyAspect;
import dev.sterner.victus.hearts.content.IronAspect;
import dev.sterner.victus.registry.VictusItemRegistry;
import dev.sterner.victus.registry.VictusStatusEffectRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "dropFromLootTable", at = @At("TAIL"))
    private void dropEmptyAspect(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        if (!causedByPlayer) return;
        var entity = (LivingEntity) (Object) this;

        if (entity instanceof Enemy) {
            if (entity.level().random.nextFloat() > 0.5f) return;
            if (!entity.hasEffect(VictusStatusEffectRegistry.HEARTBLEED.get())) return;

            entity.spawnAtLocation(VictusItemRegistry.BLANK_HEART_ASPECT.get());
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "dropFromLootTable", at = @At("HEAD"), cancellable = true)
    private void preventGolemDrops(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        if (!((Object) this instanceof IronGolem ironGolem && VictusEntityCapability.getCapability(ironGolem).flagSet(IronAspect.NO_DROPS_FLAG)))
            return;
        ci.cancel();
    }

    @SuppressWarnings("ConstantConditions")
    @ModifyVariable(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;onGround()Z", ordinal = 2), ordinal = 0)
    private float removeSlipperiness(float t) {
        if (!((Object) this instanceof Player player)) return t;
        if (!VictusPlayerComponent.getCapability(player).victusHandler.hasAspect(IcyAspect.TYPE, HeartAspect.IS_ACTIVE))
            return t;

        return 1f;
    }

}