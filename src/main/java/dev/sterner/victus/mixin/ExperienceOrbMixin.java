package dev.sterner.victus.mixin;

import dev.sterner.victus.capability.VictusPlayerComponent;
import dev.sterner.victus.hearts.HeartAspect;
import dev.sterner.victus.hearts.content.LapisAspect;
import dev.sterner.victus.packet.VictusPackets;
import dev.sterner.victus.registry.VictusPacketRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {

    @Shadow
    protected abstract int repairPlayerItems(Player player, int amount);

    @Inject(method = "repairPlayerItems", at = @At("HEAD"), cancellable = true)
    private void healIfAspectPresent(Player player, int amount, CallbackInfoReturnable<Integer> cir) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (serverPlayer.getMaxHealth() - serverPlayer.getHealth() < 1f) return;
        if (amount < 3) return;

        final var aspects = VictusPlayerComponent.getCapability(player).victusHandler;
        final int lapisIndex = aspects.findFirstIndex(LapisAspect.TYPE, HeartAspect.IS_ACTIVE);
        if (lapisIndex == -1) return;

        aspects.getAspect(lapisIndex).onBroken(player.level().damageSources().fellOutOfWorld(), 0, player.getHealth());

        VictusPacketRegistry.VICTUS_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                new VictusPackets.S2C(player.getId(),
                        aspects.getAspect(lapisIndex)
                                .onBroken(
                                        player.level().damageSources().fellOutOfWorld(),
                                        0,
                                        player.getHealth()
                                )
                )
        );

        player.heal(1);

        if (amount <= 3) cir.setReturnValue(0);
        cir.setReturnValue(repairPlayerItems(player, amount - 3));
    }
}
