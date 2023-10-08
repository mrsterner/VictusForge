package dev.sterner.victus.capability;

import dev.sterner.victus.Victus;
import dev.sterner.victus.handler.VictusHandler;
import dev.sterner.victus.registry.VictusPacketRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;
import team.lodestar.lodestone.systems.capability.LodestoneCapability;
import team.lodestar.lodestone.systems.capability.LodestoneCapabilityProvider;

public class VictusPlayerComponent implements LodestoneCapability {


    public VictusHandler victusHandler;
    public Player player;

    public VictusPlayerComponent(Player player) {
        victusHandler = new VictusHandler(player);
        this.player = player;
    }

    public static Capability<VictusPlayerComponent> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(VictusPlayerComponent.class);
    }


    public static void attachPlayerCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            final VictusPlayerComponent capability = new VictusPlayerComponent(player);
            event.addCapability(Victus.id("player_data"), new LodestoneCapabilityProvider<>(VictusPlayerComponent.CAPABILITY, () -> capability));
        }
    }


    public static void syncPlayerCapability(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof Player player) {
            if (player.level() instanceof ServerLevel) {
                syncTracking(player);
            }
        }
    }

    public static void playerClone(PlayerEvent.Clone event) {
        VictusPlayerComponent originalCapability = VictusPlayerComponent.getCapabilityOptional(event.getOriginal()).orElse(new VictusPlayerComponent(event.getEntity()));
        VictusPlayerComponent capability = VictusPlayerComponent.getCapabilityOptional(event.getEntity()).orElse(new VictusPlayerComponent(event.getEntity()));
        capability.deserializeNBT(originalCapability.serializeNBT());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.put("VictusData", victusHandler.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains("VictusData")) {
            victusHandler.deserializeNBT(nbt.getCompound("VictusData"), player);
        }
    }

    public static void syncSelf(ServerPlayer player) {
        sync(player, PacketDistributor.PLAYER.with(() -> player));
    }

    public static void syncTrackingAndSelf(Player player) {
        sync(player, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player));
    }

    public static void syncTracking(Player player) {
        sync(player, PacketDistributor.TRACKING_ENTITY.with(() -> player));
    }


    public static void sync(Player player, PacketDistributor.PacketTarget target) {
        getCapabilityOptional(player).ifPresent(c -> VictusPacketRegistry.VICTUS_CHANNEL.send(target, new SyncVictusPlayerComponentDataPacket(player.getUUID(), c.serializeNBT())));
    }

    public static LazyOptional<VictusPlayerComponent> getCapabilityOptional(Player player) {
        return player.getCapability(CAPABILITY);
    }

    public static VictusPlayerComponent getCapability(Player player) {
        return player.getCapability(CAPABILITY).orElse(new VictusPlayerComponent(player));
    }


}
