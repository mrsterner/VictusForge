package dev.sterner.victus.capability;

import dev.sterner.victus.Victus;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import team.lodestar.lodestone.systems.capability.LodestoneCapability;
import team.lodestar.lodestone.systems.capability.LodestoneCapabilityProvider;

public class VictusEntityCapability implements LodestoneCapability {

    private int flags = 0;

    public static Capability<VictusEntityCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof AreaEffectCloud || event.getObject() instanceof IronGolem) {
            final VictusEntityCapability capability = new VictusEntityCapability();
            event.addCapability(Victus.id("flags"), new LodestoneCapabilityProvider<>(CAPABILITY, () -> capability));
        }
    }

    public void setFlag(int flag) {
        flags |= flag;
    }

    public void clearFlags() {
        flags = 0;
    }

    public boolean flagSet(int flag) {
        return (flags & flag) != 0;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Flags", flags);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.flags = nbt.getInt("Flags");
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(VictusEntityCapability.class);
    }

    public static LazyOptional<VictusEntityCapability> getCapabilityOptional(Entity entity) {
        return entity.getCapability(CAPABILITY);
    }

    public static VictusEntityCapability getCapability(Entity entity) {
        return entity.getCapability(CAPABILITY).orElse(new VictusEntityCapability());
    }
}
