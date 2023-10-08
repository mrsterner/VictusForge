package dev.sterner.victus.hearts.content;


import dev.sterner.victus.Victus;
import dev.sterner.victus.hearts.HeartAspect;
import dev.sterner.victus.util.VictusVexExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

public class EvokingAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("evoking"), 12, 100, 0x48545F, EvokingAspect::new);

    public EvokingAspect(Player player) {
        super(player, TYPE);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {

        for (int i = 0; i < 3; i++) {
            var vex = new Vex(EntityType.VEX, player.level());
            ((VictusVexExtension) vex).replaceTargetGoal(player);

            Vec3 vexPos = getRandomOffsetSpecific(player.level(), player.position().add(0, 2, 0), 2, 1, 2);
            vex.absMoveTo(vexPos.x, vexPos.y, vexPos.z, 0, 0);

            vex.finalizeSpawn(((ServerLevelAccessor) player.level()), player.level().getCurrentDifficultyAt(BlockPos.containing(vexPos)), MobSpawnType.MOB_SUMMONED, null, null);
            vex.setLimitedLife(250);

            player.level().addFreshEntity(vex);
            vex.spawnAnim();
        }

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 1, 1);

        return false;
    }

    public static Vec3 getRandomOffsetSpecific(Level world, Vec3 center, double deviationX, double deviationY, double deviationZ) {

        final var r = world.getRandom();

        double x = center.x() + (r.nextDouble() - 0.5) * deviationX;
        double y = center.y() + (r.nextDouble() - 0.5) * deviationY;
        double z = center.z() + (r.nextDouble() - 0.5) * deviationZ;

        return new Vec3(x, y, z);
    }
}
