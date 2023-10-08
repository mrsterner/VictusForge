package dev.sterner.victus.hearts.content;


import dev.sterner.victus.Victus;
import dev.sterner.victus.hearts.HeartAspect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class IcyAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("icy"), 10, 40, 0x00A2D5, IcyAspect::new);

    public IcyAspect(Player player) {
        super(player, TYPE);
    }

    @Override
    protected boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 0, true, false));
        return false;
    }
}
