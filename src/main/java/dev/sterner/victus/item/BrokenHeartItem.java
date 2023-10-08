package dev.sterner.victus.item;


import dev.sterner.victus.capability.VictusPlayerComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BrokenHeartItem extends EdibleItem {

    public BrokenHeartItem() {
        super(new Properties().food(new FoodProperties.Builder().alwaysEat().build()).stacksTo(1));
    }

    @Override
    void onEaten(ItemStack stack, Level world, Player eater) {
        VictusPlayerComponent component = VictusPlayerComponent.getCapability(eater);
        while (!component.victusHandler.empty()) {
            eater.spawnAtLocation(component.victusHandler.removeAspect(eater));
        }

        eater.hurt(world.damageSources().magic(), 15f);
    }
}
