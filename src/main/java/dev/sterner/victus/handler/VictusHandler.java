package dev.sterner.victus.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.victus.capability.VictusPlayerComponent;
import dev.sterner.victus.hearts.HeartAspect;
import dev.sterner.victus.hearts.HeartAspectRegistry;
import dev.sterner.victus.hearts.OverlaySpriteProvider;
import dev.sterner.victus.hearts.content.IronAspect;
import dev.sterner.victus.packet.VictusPackets;
import dev.sterner.victus.registry.VictusPacketRegistry;
import dev.sterner.victus.registry.VictusStatusEffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class VictusHandler {

    public List<HeartAspect> getAspects() {
        return aspects;
    }

    private final List<HeartAspect> aspects = new ArrayList<>();
    @Deprecated
    private final Player player;


    public VictusHandler(Player player) {
        this.player = player;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        final var list = new ListTag();
        aspects.forEach(heart -> {
            list.add(heart.toNbt());
        });
        tag.put("Aspects", list);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag, Player player) {
        aspects.clear();

        tag.getList("Aspects", Tag.TAG_COMPOUND).forEach(element -> {

            final var compound = (CompoundTag) element;
            final var typeString = compound.getString("Type");

            final var id = ResourceLocation.tryParse(typeString);

            final var aspect = HeartAspectRegistry.forId(id, player);

            aspect.readNbt(compound);

            aspects.add(aspect);
        });
    }

    public boolean acceptsNew(Player player) {
        return this.aspects.size() < capacity(player);
    }

    public boolean addAspect(HeartAspect aspect, Player player) {
        if (this.aspects.size() >= capacity(player)) return false;

        this.aspects.add(aspect);

        VictusPlayerComponent.syncTrackingAndSelf(player);
        return true;
    }

    public HeartAspect removeAspect(Player player) {
        if (this.aspects.size() < 1) return null;

        final var removedAspect = this.aspects.remove(this.aspects.size() - 1);
        VictusPlayerComponent.syncTrackingAndSelf(player);
        return removedAspect;
    }

    public void tick(TickEvent.PlayerTickEvent event) {
        final int rechargeRate = event.player.hasEffect(VictusStatusEffectRegistry.RESURGENCE.get()) ? 2 + event.player.getEffect(VictusStatusEffectRegistry.RESURGENCE.get()).getAmplifier() : 1;

        for (int i = 0; i < aspects.size(); i++) {
            HeartAspect aspect = this.aspects.get(i);
            if (event.player.getHealth() <= i * 2 + 1) return;

            aspect.tick(rechargeRate);
            if (!aspect.active()) return;
        }

        if (event.player.getHealth() >= event.player.getMaxHealth() * .35f) return;
        if (!hasAspect(IronAspect.TYPE, HeartAspect.IS_NOT_ACTIVE)) return;

        var golems = event.player.level().getEntitiesOfClass(IronGolem.class, new AABB(event.player.blockPosition()).inflate(10), Entity::isAlive);
        golems.forEach(ironGolemEntity -> ironGolemEntity.setTarget(event.player));

    }

    public boolean recharging() {
        for (int i = 0; i < effectiveSize(); i++) {
            if (!aspects.get(i).active()) return true;
        }
        return false;
    }

    /**
     * Instantly recharges all inactive aspects in this component
     * by the given amount of their respective recharge duration
     *
     * @param percentage The percentage of each aspect's recharge duration to skip
     */
    public void rechargeAllByPercentage(float percentage) {
        for (var aspect : aspects) {
            aspect.rechargeByPercentage(percentage);
        }
    }

    /**
     * Damages the aspect at the current index
     * Also recursively backtracks to damage all aspects
     * prior to the given index
     *
     * @param index          The aspect to damage
     * @param source         The {@link DamageSource} that caused this aspect to break
     * @param damage         The damage that was inflicted by said source
     * @param originalHealth The original health the player had before being damaged
     */
    public void damageAspect(Player player, int index, DamageSource source, float damage, float originalHealth) {
        var aspect = getAspect(index);
        if (aspect == null) return;

        final int nextIndex = index + 1;
        final var nextAspect = getAspect(nextIndex);
        if (nextAspect != null) damageAspect(player, nextIndex, source, damage, originalHealth);
        VictusPlayerComponent.syncTrackingAndSelf(player);

        VictusPacketRegistry.VICTUS_CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                new VictusPackets.S2C(index, aspect.onBroken(source, damage, originalHealth))
        );
    }

    @Nullable
    public HeartAspect getAspect(int index) {
        return index < 0 || index > effectiveSize() - 1 ? null : aspects.get(index);
    }

    public int findFirstIndex(HeartAspect.Type type, Predicate<HeartAspect> filter) {
        for (int i = 0; i < this.effectiveSize(); i++) {
            final var aspect = this.getAspect(i);
            if (aspect.getType() == type && filter.test(aspect)) return i;
        }
        return -1;
    }

    public boolean hasAspect(HeartAspect.Type type, Predicate<HeartAspect> filter) {
        for (var aspect : this.aspects) {
            if (aspect.getType() != type) continue;
            if (!filter.test(aspect)) continue;
            return true;
        }
        return false;
    }

    /**
     * @return The current maximum size of this component
     */
    public int capacity(Player player) {
        return (int) (player.getMaxHealth() / 2);
    }

    /**
     * @return The amount of aspects this component currently has, capped at the current capacity
     */
    public int effectiveSize() {
        return Math.min(aspects.size(), capacity(player));
    }

    /**
     * @return The actual amount of aspects technically stored in this component, without respecting the capacity cap
     */
    public int realSize() {
        return aspects.size();
    }

    public boolean empty() {
        return this.aspects.size() == 0;
    }

    public class ClientOnly {

        static int x, y, heartIndex;

        public static void renderAspetcs(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {


            VictusPlayerComponent component = VictusPlayerComponent.getCapability(Minecraft.getInstance().player);

            if (!component.victusHandler.recharging() || component.victusHandler.getAspect(heartIndex) == null) return;

            guiGraphics.blit(HeartAspect.HEART_ATLAS_TEXTURE, x, y, 55, 55, 9, 9, 64, 64);


            final var aspect = component.victusHandler.getAspect(heartIndex);
            if (aspect == null) return;

            RenderSystem.setShaderTexture(0, aspect.getAtlas());
            renderAspect(guiGraphics, x, y, aspect.getTextureIndex(), aspect.getRechargeProgress());

            if (aspect instanceof OverlaySpriteProvider spriteProvider && spriteProvider.shouldRenderOverlay()) {
                int color = spriteProvider.getOverlayTint();
                RenderSystem.setShaderColor(getComponent(color, 16), getComponent(color, 8), getComponent(color, 0), 1);
                renderAspect(guiGraphics, x, y, spriteProvider.getOverlayIndex(), aspect.getRechargeProgress());
                RenderSystem.setShaderColor(1, 1, 1, 1);
            }
        }

        private static void renderAspect(GuiGraphics context, int x, int y, int textureIndex, float rechargeProgress) {
            int u = textureIndex % 8 * 8;
            int v = textureIndex / 8 * 8;
            context.blit(HeartAspect.HEART_ATLAS_TEXTURE, x + 1, y + 1, u, v, Math.round(rechargeProgress * 7), 7, 64, 64);
        }

        private static float getComponent(int rgb, int shift) {
            return ((rgb >> shift) & 0xFF) / 255f;
        }
    }
}
