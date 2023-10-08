package dev.sterner.victus.hearts.content;


import dev.sterner.victus.Victus;
import dev.sterner.victus.hearts.HeartAspect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class SweetAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("sweet"), 13, 50, 0xB5C2F2, SweetAspect::new);

    public SweetAspect(Player player) {
        super(player, TYPE);
    }

    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 150, 1, true, false));
        return false;
    }
}
