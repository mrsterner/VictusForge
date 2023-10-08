package dev.sterner.victus.hearts.content;


import dev.sterner.victus.Victus;
import dev.sterner.victus.capability.VictusEntityCapability;
import dev.sterner.victus.hearts.HeartAspect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public class IronAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("iron"), 17, 300, 0xB5C2F2, IronAspect::new);
    public static final int NO_DROPS_FLAG = 0x1;

    public IronAspect(Player player) {
        super(player, TYPE);
    }

    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        var golem = EntityType.IRON_GOLEM.create(player.level());

        if (golem != null) {
            VictusEntityCapability.getCapability(golem).setFlag(NO_DROPS_FLAG);
            golem.absMoveTo(player.getX(), player.getY(), player.getZ(), 0, 0);

            player.level().addFreshEntity(golem);
        }

        return false;
    }
}
