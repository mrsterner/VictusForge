package dev.sterner.victus.item;

import dev.sterner.victus.capability.VictusPlayerComponent;
import dev.sterner.victus.hearts.HeartAspect;
import dev.sterner.victus.registry.VictusItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeartAspectItem extends EdibleItem {

    private static final Map<HeartAspect.Type, HeartAspectItem> HEART_ASPECT_ITEMS = new HashMap<>();

    private final HeartAspect.Type aspectType;

    public HeartAspectItem(HeartAspect.Type aspectType) {
        super(new Properties().food(new FoodProperties.Builder().alwaysEat().nutrition(4).saturationMod(.5f).build()).stacksTo(1));
        this.aspectType = aspectType;

        HeartAspectItem.HEART_ASPECT_ITEMS.put(aspectType, this);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        final var playerStack = user.getItemInHand(hand);
        VictusPlayerComponent component = VictusPlayerComponent.getCapability(user);

        if (!component.victusHandler.acceptsNew(user)) return InteractionResultHolder.pass(playerStack);

        user.startUsingItem(hand);
        return InteractionResultHolder.success(playerStack);
    }

    @Override
    void onEaten(ItemStack stack, Level world, Player eater) {
        VictusPlayerComponent component = VictusPlayerComponent.getCapability(eater);
        boolean bl = component.victusHandler.addAspect(aspectType.factory().apply(eater), eater);


        //TODO VictusParticleEvents.HEART_PARTICLES.spawn(world, eater.position(), false);
    }

    @Override
    public MutableComponent getName(ItemStack stack) {
        return VictusItemRegistry.coloredTranslationWithPrefix(getDescriptionId(stack), aspectType.color());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        String desc = Language.getInstance().getOrDefault(this.getDescriptionId(stack) + ".description");
        if (desc.length() > 30 && desc.contains(" ")) {
            int spaceIndex = StringUtils.ordinalIndexOf(desc, " ", StringUtils.countMatches(desc, " ") / 2 + 1);
            tooltip.add(Component.literal(desc.substring(0, spaceIndex)).withStyle(ChatFormatting.DARK_GRAY));
            tooltip.add(Component.literal(desc.substring(spaceIndex + 1)).withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltip.add(Component.translatable(this.getDescriptionId(stack) + ".description").withStyle(ChatFormatting.DARK_GRAY));
        }

        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("text.victusforge.recharge_duration", aspectType.standardRechargeDuration() / 20f).withStyle(ChatFormatting.BLUE));
    }

    public static HeartAspectItem getItem(HeartAspect.Type type) {
        return HeartAspectItem.HEART_ASPECT_ITEMS.get(type);
    }
}
