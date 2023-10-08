package dev.sterner.victus.hearts.content;


import dev.sterner.victus.Victus;
import dev.sterner.victus.hearts.HeartAspect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ArcheryAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("archery"), 14, 40, 0x71413B, ArcheryAspect::new);

    public ArcheryAspect(Player player) {
        super(player, TYPE);
    }

    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {

        var entities = player.level().getEntitiesOfClass(LivingEntity.class, new AABB(player.blockPosition()).inflate(3), (p) -> p != player && !(p instanceof TamableAnimal tameable && tameable.isOwnedBy(player)));

        for (int i = 0; i < 3; i++) {
            if (entities.size() < 1) return false;
            var entity = entities.remove(player.level().random.nextInt(entities.size()));

            var arrow = new Arrow(EntityType.ARROW, player.level());
            Vec3 arrowVelocity = entity.position().subtract(player.position()).scale(.25);
            Vec3 arrowPos = player.position().add(arrowVelocity.scale(0.25f)).add(0, player.getEyeHeight(player.getPose()), 0);

            arrow.absMoveTo(arrowPos.x, arrowPos.y, arrowPos.z, 0, 45);
            arrow.setDeltaMovement(arrowVelocity);

            arrow.setKnockback(2);

            player.level().addFreshEntity(arrow);
        }

        return false;
    }
}
