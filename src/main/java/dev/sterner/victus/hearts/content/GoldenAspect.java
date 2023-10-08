package dev.sterner.victus.hearts.content;


import dev.sterner.victus.Victus;
import dev.sterner.victus.capability.VictusPlayerComponent;
import dev.sterner.victus.hearts.HeartAspect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class GoldenAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("golden"), 16, 100, 0xFFF77B, GoldenAspect::new);

    public GoldenAspect(Player player) {
        super(player, TYPE);
    }

    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        VictusPlayerComponent component = VictusPlayerComponent.getCapability(player);
        int index = findIndex(component);

        float percentage = 1f - ((index + 0f) / (player.getMaxHealth() / 2));
        int level = Math.max(0, Math.round(percentage * 5) - 1);

        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 600, level));

        return false;
    }

    private int findIndex(VictusPlayerComponent component) {
        for (int i = 0; i < component.victusHandler.effectiveSize(); i++) {
            if (component.victusHandler.getAspect(i) == this) return i;
        }
        return -1;
    }
}
