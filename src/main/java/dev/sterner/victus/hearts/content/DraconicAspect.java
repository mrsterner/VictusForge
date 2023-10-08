package dev.sterner.victus.hearts.content;


import dev.sterner.victus.Victus;
import dev.sterner.victus.capability.VictusEntityCapability;
import dev.sterner.victus.hearts.HeartAspect;
import dev.sterner.victus.registry.VictusStatusEffectRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.player.Player;

public class DraconicAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("draconic"), 11, 100, 0xFF55FF, DraconicAspect::new);
    public static final int IGNORE_OWNER_FLAG = 0x1;

    public DraconicAspect(Player player) {
        super(player, TYPE);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        var areaEffectCloud = new AreaEffectCloud(this.player.level(), this.player.getX(), this.player.getY(), this.player.getZ());

        VictusEntityCapability.getCapability(areaEffectCloud).setFlag(IGNORE_OWNER_FLAG);

        areaEffectCloud.setOwner(this.player);
        areaEffectCloud.setParticle(ParticleTypes.DRAGON_BREATH);

        areaEffectCloud.setRadius(2f);
        areaEffectCloud.setDuration(150);
        areaEffectCloud.setRadiusPerTick((7f - areaEffectCloud.getRadius()) / areaEffectCloud.getDuration());
        areaEffectCloud.addEffect(new MobEffectInstance(VictusStatusEffectRegistry.TRUE_DAMAGE.get(), 1, 0));

        player.level().addFreshEntity(areaEffectCloud);
        return false;
    }
}
