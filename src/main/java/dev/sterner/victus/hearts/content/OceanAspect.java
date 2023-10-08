package dev.sterner.victus.hearts.content;


import dev.sterner.victus.Victus;
import dev.sterner.victus.hearts.HeartAspect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class OceanAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("ocean"), 4, 50, 0x00A2D5, HeartAspect.belowHealthPercentage(.75f), OceanAspect::new);

    public OceanAspect(Player player) {
        super(player, TYPE);
    }

    @Override
    public void update() {
        if (!player.isUnderWater()) return;
        player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 5, 0, true, true));

        if (player.getHealth() > player.getMaxHealth() * .25) return;
        player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 5, 0, true, true));
    }

    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        player.removeEffect(MobEffects.CONDUIT_POWER);
        player.removeEffect(MobEffects.DOLPHINS_GRACE);
        return false;
    }
}
