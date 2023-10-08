package dev.sterner.victus.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class VictusParticleEvents {
    public static void spawnHeart(Level world, Vec3 position, boolean b) {
        spawn(
                b ? ParticleTypes.ANGRY_VILLAGER : ParticleTypes.HEART,
                world,
                position,
                new Vec3(0,.1,0),
                20,
                1.5,
                false

        );
    }

    public static void spawnBlazing(Level world, Vec3 position) {
        spawn(
                ParticleTypes.FLAME,
                world,
                position,
                new Vec3(.15,.15,.15),
                50,
                3,
                true

        );
    }

    public static void spawnConvert(Level world, Vec3 position) {
        spawn(
                ParticleTypes.POOF,
                world,
                position,
                new Vec3(.35,.35,.35),
                20,
                0,
                false

        );
    }


    private static void addParticle(ParticleOptions particle, Level world, Vec3 location, Vec3 velocity, boolean randomizeVelocity) {
        if (randomizeVelocity) {
            velocity = getRandomOffsetSpecific(world, Vec3.ZERO, 0, 0, 0);
        }

        world.addParticle(particle, location.x, location.y, location.z, velocity.x, velocity.y, velocity.z);
    }

    public static void spawn(ParticleOptions particle, Level world, Vec3 pos, Vec3 velocity, int particleCount, double deviation, boolean randVel) {
        Vec3 location;

        for (int i = 0; i < particleCount; i++) {
            location = getRandomOffsetSpecific(world, pos, deviation, deviation, deviation);
            addParticle(particle, world, location, velocity, randVel);
        }

    }

    public static Vec3 getRandomOffsetSpecific(Level world, Vec3 center, double deviationX, double deviationY, double deviationZ) {

        final var r = world.getRandom();

        double x = center.x() + (r.nextDouble() - 0.5) * deviationX;
        double y = center.y() + (r.nextDouble() - 0.5) * deviationY;
        double z = center.z() + (r.nextDouble() - 0.5) * deviationZ;

        return new Vec3(x, y, z);
    }
}
