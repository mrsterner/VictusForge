package dev.sterner.victus.common;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class TrueDamageStatusEffect extends MobEffect {
    public TrueDamageStatusEffect() {
        super(MobEffectCategory.HARMFUL, 4393481);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.hurt(entity.level().damageSources().magic(), 6 << amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration >= 1;
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }
}
