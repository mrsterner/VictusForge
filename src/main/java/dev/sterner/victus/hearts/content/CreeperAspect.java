package dev.sterner.victus.hearts.content;


import dev.sterner.victus.Victus;
import dev.sterner.victus.hearts.HeartAspect;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Level;

public class CreeperAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("creeper"), 6, 200, 0x53BC5E, CreeperAspect::new);

    private static final ResourceKey<DamageType> SUICIDE_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, Victus.id("suicide"));

    public CreeperAspect(Player player) {
        super(player, TYPE);
    }

    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        player.getServer().execute(() -> {
            player.level().explode(
                    null,
                    new DamageSource(player.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SUICIDE_DAMAGE_TYPE)),
                    new EntityBasedExplosionDamageCalculator(player),
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    3f,
                    false,
                    Level.ExplosionInteraction.NONE
            );
        });
        return false;
    }

}
