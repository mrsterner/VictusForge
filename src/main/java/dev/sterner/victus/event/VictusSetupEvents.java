package dev.sterner.victus.event;

import dev.sterner.victus.capability.VictusEntityCapability;
import dev.sterner.victus.capability.VictusPlayerComponent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class VictusSetupEvents {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        VictusPlayerComponent.registerCapabilities(event);
        VictusEntityCapability.registerCapabilities(event);
    }
}
