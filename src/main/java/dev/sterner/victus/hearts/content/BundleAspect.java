package dev.sterner.victus.hearts.content;


import dev.sterner.victus.Victus;
import dev.sterner.victus.hearts.HeartAspect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public class BundleAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("bundle"), 2, 100, 0xAE0000, BundleAspect::new);

    public BundleAspect(Player player) {
        super(player, TYPE);
    }

    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        player.heal(4f);
        return false;
    }
}
