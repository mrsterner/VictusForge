package dev.sterner.victus.item;


import dev.sterner.victus.registry.VictusItemRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BlankAspectItem extends Item {
    public BlankAspectItem() {
        super(new Properties());
    }

    @Override
    public void onDestroyed(ItemEntity entity) {
        if (!entity.isOnFire()) return;
        entity.level().playSound(null, entity.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1, 1);
        Containers.dropItemStack(entity.level(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(VictusItemRegistry.VOID_HEART_ASPECT.get()));
    }
}
