package dev.sterner.victus.hearts.content;


import dev.sterner.victus.Victus;
import dev.sterner.victus.hearts.HeartAspect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public class LapisAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("lapis"), 15, 20, 0x0064B8, LapisAspect::new);

    public LapisAspect(Player player) {
        super(player, TYPE);
    }

    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        return false;
    }
}
