package dev.sterner.victus.hearts.content;


import dev.sterner.victus.Victus;
import dev.sterner.victus.hearts.HeartAspect;
import dev.sterner.victus.registry.VictusItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TotemAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("totem"), 0, 3000, 0xFFD16D, TotemAspect::new);

    private boolean hadTotem = false;

    public TotemAspect(Player player) {
        super(player, TYPE);
    }

    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        final Inventory inventory = player.getInventory();

        if (inventory.contains(new ItemStack(Items.TOTEM_OF_UNDYING))) {
            player.setHealth(player.getHealth() + 15);
            inventory.clearOrCountMatchingItems(stack -> stack.is(Items.TOTEM_OF_UNDYING), 1, player.inventoryMenu.getCraftSlots());

            this.hadTotem = true;
        } else {
            player.setHealth(player.getHealth() + 5);

            this.hadTotem = false;
        }

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, player.getSoundSource(), 1.0F, 1.0F);

        return true;
    }

    @Override
    protected int getRechargeDuration() {
        return hadTotem ? 200 : getType().standardRechargeDuration();
    }

    @Override
    protected void readCustomData(CompoundTag nbt) {
        this.hadTotem = nbt.getBoolean("HadTotem");
    }

    @Override
    protected void writeCustomData(CompoundTag nbt) {
        nbt.putBoolean("HadTotem", hadTotem);
    }

    @Override
    protected void handleBreakClient() {
        Minecraft.getInstance().gameRenderer.displayItemActivation(new ItemStack(VictusItemRegistry.TOTEM_HEART_ASPECT.get()));
        Minecraft.getInstance().particleEngine.createTrackingEmitter(player, ParticleTypes.TOTEM_OF_UNDYING, 30);
    }
}
