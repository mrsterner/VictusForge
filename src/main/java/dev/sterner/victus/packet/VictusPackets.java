package dev.sterner.victus.packet;

import dev.sterner.victus.capability.VictusPlayerComponent;
import dev.sterner.victus.hearts.HeartAspect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.systems.network.LodestoneClientPacket;
import team.lodestar.lodestone.systems.network.LodestoneServerPacket;

import java.util.function.Supplier;

public class VictusPackets {
    public static class C2S extends LodestoneServerPacket {

        private final int entityId;

        public C2S(int entityId) {
            this.entityId = entityId;
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeInt(entityId);
        }

        @Override
        public void execute(Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context ctx = contextSupplier.get();
            ctx.enqueueWork(() -> {
                ServerPlayer player = ctx.getSender();

                if (player != null && !player.isCreative()) return;

                HeartAspect h = VictusPlayerComponent.getCapability(player).victusHandler.removeAspect(player);
                player.drop(h != null);
            });
        }

        public static void register(SimpleChannel instance, int index) {
            instance.registerMessage(index, C2S.class, C2S::encode, C2S::decode, C2S::handle);
        }

        public static C2S decode(FriendlyByteBuf buf) {
            return new C2S(buf.readInt());
        }

    }

    public static class S2C extends LodestoneClientPacket {

        private final int index;
        private final boolean callHandler;

        public S2C(int entityId, boolean callHandler) {
            this.index = entityId;
            this.callHandler = callHandler;
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeVarInt(index);
            buf.writeBoolean(callHandler);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void execute(Supplier<NetworkEvent.Context> context) {
            Minecraft.getInstance().execute(HeartAspect.createBreakEvent(Minecraft.getInstance(), index, callHandler));
        }

        public static void register(SimpleChannel instance, int index) {
            instance.registerMessage(index, S2C.class, S2C::encode, S2C::decode, S2C::handle);
        }

        public static S2C decode(FriendlyByteBuf buf) {
            return new S2C(buf.readVarInt(), buf.readBoolean());
        }
    }

}
