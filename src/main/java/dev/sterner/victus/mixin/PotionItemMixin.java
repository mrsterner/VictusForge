package dev.sterner.victus.mixin;

import dev.sterner.victus.capability.VictusPlayerComponent;
import dev.sterner.victus.handler.VictusHandler;
import dev.sterner.victus.hearts.content.PotionAspect;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public class PotionItemMixin {

    @Inject(method = "finishUsingItem", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;isClientSide:Z"), cancellable = true)
    private void onConsume(ItemStack stack, Level world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        Player player = user instanceof Player ? (Player) user : null;
        if (player == null) return;

        var potion = PotionUtils.getPotion(stack);
        if (potion == Potions.EMPTY || potion.getEffects().isEmpty()) return;

        final var potionAspect = findFirstEmptyPotionAspect(VictusPlayerComponent.getCapability(player).victusHandler);
        if (potionAspect == null) return;

        potionAspect.setPotion(potion);

        player.awardStat(Stats.ITEM_USED.get((PotionItem) (Object) this));
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        if (!player.getAbilities().instabuild) {
            if (stack.isEmpty()) {
                cir.setReturnValue(new ItemStack(Items.GLASS_BOTTLE));
            }

            player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
        }

        user.gameEvent(GameEvent.DRINK);
        cir.setReturnValue(stack);
    }

    @Nullable
    private static PotionAspect findFirstEmptyPotionAspect(VictusHandler component) {
        for (int i = 0; i < component.effectiveSize(); i++) {
            if (component.getAspect(i) instanceof PotionAspect aspect && aspect.getPotion() == Potions.EMPTY)
                return aspect;
        }
        return null;
    }
}
