package dev.sterner.victus.hearts;

import dev.sterner.victus.Victus;
import dev.sterner.victus.capability.VictusPlayerComponent;
import dev.sterner.victus.item.HeartAspectItem;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.function.Function;
import java.util.function.Predicate;

public class HeartAspect implements ItemLike {

    public static final Predicate<HeartAspect> IS_ACTIVE = HeartAspect::active;
    public static final Predicate<HeartAspect> IS_NOT_ACTIVE = heartAspect -> !heartAspect.active();

    public static final ResourceLocation HEART_ATLAS_TEXTURE = Victus.id("textures/gui/hearts_alt.png");
    protected static final Predicate<Player> NEVER_UPDATE = p -> false;
    protected static final Predicate<Player> ALWAYS_UPDATE = p -> true;

    public Player player;
    private final Type type;
    private int cooldown;

    public HeartAspect(Player player, Type type) {
        this.player = player;
        this.type = type;
        this.cooldown = getRechargeDuration();
    }

    public final CompoundTag toNbt() {
        final var containerNbt = new CompoundTag();
        containerNbt.putString("Type", getType().id.toString());
        containerNbt.putInt("Cooldown", cooldown);

        final var dataNbt = new CompoundTag();
        writeCustomData(dataNbt);
        containerNbt.put("CustomData", dataNbt);

        return containerNbt;
    }

    public final void readNbt(CompoundTag nbt) {
        this.cooldown = nbt.getInt("Cooldown");
        readCustomData(nbt.getCompound("CustomData"));
    }

    protected void readCustomData(CompoundTag nbt) {

    }

    protected void writeCustomData(CompoundTag nbt) {

    }

    public void tick(int rechargeRate) {
        if (this.cooldown > -1) this.cooldown -= rechargeRate;
        if (this.cooldown < -1) this.cooldown = -1;
        else if (this.active() && this.type.updateCondition.test(player)) update();
    }

    /**
     * Can be implemented for behaviour ran every tick this heart is active
     */
    protected void update() {

    }

    /**
     * Creates an update condition that applies to players below the given absolute health
     */
    protected static Predicate<Player> belowHealth(float health) {
        return p -> p.getHealth() <= health;
    }

    /**
     * Creates an update condition that applies to players below the given percentage
     * of their max health
     */
    protected static Predicate<Player> belowHealthPercentage(float percentage) {
        return p -> p.getHealth() <= p.getMaxHealth() * percentage;
    }

    /**
     * Called on the client to sync a broken aspect state with the server
     * and play possible client-side effects
     *
     * @param callHandler If the implementor's handler method should be called
     */

    public final void onBrokenClient(boolean callHandler) {
        if (this.active() && callHandler) this.handleBreakClient();

        this.cooldown = getRechargeDuration();
    }

    public static Runnable createBreakEvent(Minecraft client, int index, boolean callHandler) {
        VictusPlayerComponent component = VictusPlayerComponent.getCapability(client.player);
        return () -> component.victusHandler.getAspect(index).onBrokenClient(callHandler);
    }

    /**
     * Call this to signal that this aspect has received damage
     * Will delegate to implementation for effects and put this
     * aspect on cooldown
     *
     * @param source         The {@link DamageSource} that caused this aspect to break
     * @param damage         The damage that was inflicted by said source
     * @param originalHealth The original health the player had before being damaged
     * @return {@code true} if the client should receive a break event
     */
    public final boolean onBroken(DamageSource source, float damage, float originalHealth) {
        boolean shouldCallClient = false;
        if (this.active()) shouldCallClient = this.handleBreak(source, damage, originalHealth);

        this.cooldown = this.getRechargeDuration();
        return shouldCallClient;
    }

    /**
     * @return Whether this aspect is currently ready to apply effects when damaged
     */
    public final boolean active() {
        return this.cooldown == -1;
    }

    /**
     * Instantly recharges this aspect by the given percentage of its recharge
     * duration, provided that it currently is on cooldown
     *
     * @param percentage The percentage of the cooldown to skip
     */
    public final void rechargeByPercentage(float percentage) {
        if (this.active()) return;

        this.cooldown -= this.getRechargeDuration() * percentage;
        if (this.cooldown < -1) this.cooldown = -1;
    }

    /**
     * @return The percentage that this aspect has already recharged
     */
    public final float getRechargeProgress() {
        return cooldown != -1 ? (getRechargeDuration() - cooldown) / (float) getRechargeDuration() : 1;
    }

    /**
     * @return The time this aspect takes to recharge, in ticks
     */
    protected int getRechargeDuration() {
        return type.standardRechargeDuration;
    }

    /**
     * @return The atlas texture this aspect uses and for which the texture index is correct
     */
    public ResourceLocation getAtlas() {
        return HEART_ATLAS_TEXTURE;
    }

    public final Type getType() {
        return type;
    }

    /**
     * Called when this aspect has been broken
     *
     * @param source         The {@link DamageSource} that caused this aspect to break
     * @param damage         The damage that was inflicted by said source
     * @param originalHealth The original health the player had before being damaged
     * @return {@code true} if the client should receive a break event
     */
    protected boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        return false;
    }

    /**
     * Called on the client when this aspect has been broken
     */
    protected void handleBreakClient() {

    }

    /**
     * @return The index of this aspect's texture into the heart atlas
     */
    public int getTextureIndex() {
        return type.textureIndex;
    }

    @Override
    public Item asItem() {
        return HeartAspectItem.getItem(type);
    }

    public record Type(ResourceLocation id, int textureIndex, int standardRechargeDuration, int color,
                       Predicate<Player> updateCondition,
                       Function<Player, HeartAspect> factory) {

        /**
         * @param id                       The registry ID of this type
         * @param textureIndex             The index into the atlas texture where this aspect's texture is located
         * @param standardRechargeDuration The default recharge duration of this aspect type, can be overridden dynamically
         * @param color                    The name color of the aspect item
         * @param updateCondition          The conditions under which to call the {@link HeartAspect#update()} function,
         * @param factory                  The aspect factory
         * @see HeartAspect#belowHealth(float)
         * @see HeartAspect#belowHealthPercentage(float)
         */
        public Type {
        }

        /**
         * Convenience constructor that does not take an update condition and uses {@link HeartAspect#NEVER_UPDATE} as the default
         */
        public Type(ResourceLocation id, int textureIndex, int standardRechargeDuration, int color, Function<Player, HeartAspect> factory) {
            this(id, textureIndex, standardRechargeDuration, color, NEVER_UPDATE, factory);
        }

    }
}
