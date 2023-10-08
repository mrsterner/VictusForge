package dev.sterner.victus.hearts.content;


import dev.sterner.victus.Victus;
import dev.sterner.victus.capability.VictusPlayerComponent;
import dev.sterner.victus.hearts.HeartAspect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public class EmeraldAspect extends HeartAspect {

    public static final Type TYPE =
            new Type(Victus.id("emerald"), 18, 40, 0x07D069,
                    EmeraldAspect::new
            );

    public EmeraldAspect(Player player) {
        super(player, TYPE);
    }

    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        VictusPlayerComponent component = VictusPlayerComponent.getCapability(player);
        component.victusHandler.rechargeAllByPercentage(.20f);
        return true;
    }

    @Override
    protected void handleBreakClient() {
        VictusPlayerComponent component = VictusPlayerComponent.getCapability(player);
        component.victusHandler.rechargeAllByPercentage(.20f);
    }
}
