package dev.sterner.victus.hearts;


import dev.sterner.victus.hearts.content.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class HeartAspectRegistry {

    public static final Map<ResourceLocation, HeartAspect.Type> REGISTRY = new HashMap<>();

    public static void registerDefaults() {
        register(GrilledAspect.TYPE);
        register(BundleAspect.TYPE);
        register(CreeperAspect.TYPE);
        register(DiamondAspect.TYPE);
        register(LightAspect.TYPE);
        register(OceanAspect.TYPE);
        register(TotemAspect.TYPE);
        register(PotionAspect.TYPE);
        register(ArcheryAspect.TYPE);
        register(BlazingAspect.TYPE);
        register(DraconicAspect.TYPE);
        register(EmeraldAspect.TYPE);
        register(EvokingAspect.TYPE);
        register(GoldenAspect.TYPE);
        register(IcyAspect.TYPE);
        register(IronAspect.TYPE);
        register(LapisAspect.TYPE);
        register(SweetAspect.TYPE);
        register(CheeseAspect.TYPE);
    }

    public static void register(HeartAspect.Type type) {
        if (REGISTRY.containsKey(type.id()))
            throw new IllegalArgumentException("Tried to register " + type.id() + " twice!");
        REGISTRY.put(type.id(), type);
    }

    public static HeartAspect forId(ResourceLocation id, Player player) {
        if (!REGISTRY.containsKey(id)) return null;
        return REGISTRY.get(id).factory().apply(player);
    }

}
