package dev.sterner.victus.event;

import dev.sterner.victus.capability.VictusEntityCapability;
import dev.sterner.victus.capability.VictusPlayerComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class VictusRuntimeEvents {
    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        VictusPlayerComponent.attachPlayerCapability(event);
        VictusEntityCapability.attachEntityCapability(event);
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        VictusPlayerComponent.syncPlayerCapability(event);
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        VictusPlayerComponent.playerClone(event);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        VictusPlayerComponent.getCapability(event.player).victusHandler.tick(event);
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            float health = player.getHealth();
            int affectedAspect = Math.max(0, (int) Math.ceil((health + 1) / 2d) - 1);
            float amount = event.getAmount();
            VictusPlayerComponent.getCapability(player).victusHandler.damageAspect(player, affectedAspect, event.getSource(), amount, health + amount);
        }

    }
}
