package dev.sterner.victus.hearts.content;

import dev.sterner.victus.Victus;
import dev.sterner.victus.hearts.HeartAspect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public class CheeseAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("cheese"), 19, 600, 0xffd800, CheeseAspect::new);

    public CheeseAspect(Player player) {
        super(player, TYPE);
    }

    @Override
    protected boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        final var effects = new ArrayList<MobEffect>();

        player.getActiveEffectsMap().forEach((effect, instance) -> {
            if (effect.getCategory() != MobEffectCategory.HARMFUL) return;
            effects.add(effect);
        });

        effects.forEach(player::removeEffect);
        return false;
    }
}
