package dev.sterner.victus.registry;

import dev.sterner.victus.Victus;
import dev.sterner.victus.capability.SyncVictusPlayerComponentDataPacket;
import dev.sterner.victus.packet.VictusPackets;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Victus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class VictusPacketRegistry {

    public static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel VICTUS_CHANNEL = NetworkRegistry.newSimpleChannel(Victus.id("main"), () -> VictusPacketRegistry.PROTOCOL_VERSION, VictusPacketRegistry.PROTOCOL_VERSION::equals, VictusPacketRegistry.PROTOCOL_VERSION::equals);

    @SuppressWarnings("UnusedAssignment")
    @SubscribeEvent
    public static void registerNetworkStuff(FMLCommonSetupEvent event) {
        int index = 0;
        SyncVictusPlayerComponentDataPacket.register(VICTUS_CHANNEL, index++);

        VictusPackets.C2S.register(VICTUS_CHANNEL, index++);
        VictusPackets.S2C.register(VICTUS_CHANNEL, index++);
    }
}
