package dev.sterner.victus.hearts.content;


import dev.sterner.victus.Victus;
import dev.sterner.victus.hearts.HeartAspect;
import dev.sterner.victus.registry.VictusItemRegistry;
import dev.sterner.victus.util.VictusParticleEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class LightAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("light"), 5, 1200, 0xFFFFFF, LightAspect::new);

    public LightAspect(Player player) {
        super(player, TYPE);
    }

    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        if (!source.is(DamageTypeTags.IS_FALL)) return false;

        this.player.setHealth(originalHealth);
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, player.getSoundSource(), 1.0F, 2.0F);
        return true;
    }

    @Override
    protected void handleBreakClient() {
        Minecraft.getInstance().gameRenderer.displayItemActivation(new ItemStack(VictusItemRegistry.LIGHT_HEART_ASPECT.get()));

        VictusParticleEvents.spawn(
                ParticleTypes.POOF,
                player.level(),
                player.position().add(0,1,0),
                new Vec3(0,.1,0),
                40,
                3,
                false

        );

        Minecraft.getInstance().particleEngine.createTrackingEmitter(player, ParticleTypes.POOF, 10);
    }
}
