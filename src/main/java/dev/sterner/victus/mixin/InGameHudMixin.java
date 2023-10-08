package dev.sterner.victus.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.victus.Victus;
import dev.sterner.victus.capability.VictusPlayerComponent;
import dev.sterner.victus.hearts.HeartAspect;
import dev.sterner.victus.hearts.OverlaySpriteProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Gui.class)
public abstract class InGameHudMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private int screenWidth;
    @Shadow
    private int screenHeight;
    @Shadow
    private int displayHealth;

    @Shadow
    protected abstract void renderHearts(GuiGraphics context, Player player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking);

    @Unique
    int heartX, heartY, heartIndex;

    @Unique
    VictusPlayerComponent aspectComponent = null;

    @Inject(method = "renderHearts", at = @At("HEAD"))
    private void storeAspectComponent(GuiGraphics ntext, Player player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        this.aspectComponent = VictusPlayerComponent.getCapability(player);
    }

    @Inject(method = "renderHearts", at = @At("RETURN"))
    private void releaseAspectComponent(GuiGraphics context, Player player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        this.aspectComponent = null;
    }

    @Inject(method = "renderHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIIZZ)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void storeLocals(GuiGraphics context, Player player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci, Gui.HeartType type, int i, int j, int k, int l, int m, int n, int o, int p, int q) {
        this.heartX = p;
        this.heartY = q;
        this.heartIndex = m;
    }

    @Inject(method = "renderHeart", at = @At("TAIL"), cancellable = true)
    private void renderRechargingOutline(GuiGraphics context, Gui.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
        if (type != Gui.HeartType.CONTAINER) return;
        if (!aspectComponent.victusHandler.recharging() || aspectComponent.victusHandler.getAspect(heartIndex) == null)
            return;

        context.blit(HeartAspect.HEART_ATLAS_TEXTURE, heartX, heartY, 55, 55, 9, 9, 64, 64);
    }

    @Inject(method = "renderHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIIZZ)V", ordinal = 3, shift = At.Shift.AFTER))
    private void renderOverlay(GuiGraphics context, Player player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        final var aspect = aspectComponent.victusHandler.getAspect(heartIndex);
        if (aspect == null) return;

        RenderSystem.setShaderTexture(0, aspect.getAtlas());
        renderAspect(context, heartX, heartY, aspect.getTextureIndex(), aspect.getRechargeProgress());

        if (aspect instanceof OverlaySpriteProvider spriteProvider && spriteProvider.shouldRenderOverlay()) {
            int color = spriteProvider.getOverlayTint();
            RenderSystem.setShaderColor(getComponent(color, 16), getComponent(color, 8), getComponent(color, 0), 1);
            renderAspect(context, heartX, heartY, spriteProvider.getOverlayIndex(), aspect.getRechargeProgress());
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;canHurtPlayer()Z"))
    private void renderHeartsInVictusTab(GuiGraphics context, float tickDelta, CallbackInfo ci) {
        if (!(minecraft.screen instanceof CreativeModeInventoryScreen creativeModeInventoryScreen)) return;
        if (CreativeModeInventoryScreen.selectedTab != Victus.VICTUS_TAB.get()) return;

        final var player = minecraft.player;

        int healthBarX = this.screenWidth / 2 - 91;
        int healthBarY = this.screenHeight - 35;

        int playerHealth = Mth.ceil(player.getHealth());

        float renderMaxHealth = (float) Math.max(player.getAttributeValue(Attributes.MAX_HEALTH), Math.max(this.displayHealth, playerHealth));
        int absorption = Mth.ceil(player.getAbsorptionAmount());

        // evil mojang line calculation code
        // wtf
        int r = Math.max(10 - (Mth.ceil((renderMaxHealth + absorption) / 2.0F / 10.0F) - 2), 3);

        // int r = Math.max(12 - MathHelper.ceil((renderMaxHealth + absorption) * .05f), 3);
        // why does this better version reside in a comment? what do i know

        this.renderHearts(context, player, healthBarX, healthBarY, r, -1, renderMaxHealth, playerHealth, this.displayHealth, absorption, false);
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
