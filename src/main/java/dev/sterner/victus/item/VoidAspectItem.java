package dev.sterner.victus.item;


import dev.sterner.victus.capability.VictusPlayerComponent;
import dev.sterner.victus.registry.VictusItemRegistry;
import dev.sterner.victus.util.VictusParticleEvents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class VoidAspectItem extends EdibleItem {

    public VoidAspectItem() {
        super(new Properties().fireResistant().food(new FoodProperties.Builder().alwaysEat().build()).stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        VictusPlayerComponent component = VictusPlayerComponent.getCapability(user);
        if (component.victusHandler.empty()) return InteractionResultHolder.pass(user.getItemInHand(hand));
        return super.use(world, user, hand);
    }

    @Override
    public MutableComponent getName(ItemStack stack) {
        return VictusItemRegistry.coloredTranslationWithPrefix(getDescriptionId(stack), 0xff2d2d);
    }

    @Override
    void onEaten(ItemStack stack, Level world, Player eater) {
        VictusPlayerComponent component = VictusPlayerComponent.getCapability(eater);
        component.victusHandler.removeAspect(component.player);
        eater.hurt(world.damageSources().magic(), eater.getHealth() + 1 - (component.victusHandler.effectiveSize() + 1) * 2);

        VictusParticleEvents.spawnHeart(world, eater.position(), true);
    }
}
