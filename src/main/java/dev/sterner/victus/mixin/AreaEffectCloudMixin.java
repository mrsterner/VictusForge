package dev.sterner.victus.mixin;

import dev.sterner.victus.capability.VictusEntityCapability;
import dev.sterner.victus.hearts.content.DraconicAspect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(AreaEffectCloud.class)
public class AreaEffectCloudMixin {

    @Shadow
    @Nullable
    private LivingEntity owner;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
    private void removeOwner(CallbackInfo ci, boolean bl, float f, boolean $$23, List<MobEffectInstance> list, List<Entity> entities) {
        if (!VictusEntityCapability.getCapability((AreaEffectCloud) (Object) this).flagSet(DraconicAspect.IGNORE_OWNER_FLAG))
            return;
        entities.remove(owner);
    }
}