package dev.sterner.victus.hearts.content;

import dev.sterner.victus.Victus;
import dev.sterner.victus.hearts.HeartAspect;
import dev.sterner.victus.util.VictusParticleEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class BlazingAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("blazing"), 9, 50, 0xFD6A2C, BlazingAspect::new);

    public BlazingAspect(Player player) {
        super(player, TYPE);
    }

    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        VictusParticleEvents.spawnBlazing(player.level(), player.position());

        var entities = player.level().getEntitiesOfClass(LivingEntity.class, new AABB(player.blockPosition()).inflate(4), (p) -> p != player && !(p instanceof TamableAnimal tameable && tameable.isOwnedBy(player)));

        for (int i = 0; i < 4; i++) {
            if (entities.size() < 1) return false;
            var entity = entities.remove(player.level().random.nextInt(entities.size()));
            entity.hurt(entity.level().damageSources().inFire(), 3);
            entity.setSecondsOnFire(4);
        }

        return false;
    }
}
