package dev.sterner.victus.item;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class EdibleItem extends Item {

    public EdibleItem(Properties settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (!world.isClientSide && user instanceof Player player) onEaten(stack, world, player);
        return super.finishUsingItem(stack, world, user);
    }

    abstract void onEaten(ItemStack stack, Level world, Player eater);
}
