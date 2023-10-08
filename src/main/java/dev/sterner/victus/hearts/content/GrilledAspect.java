package dev.sterner.victus.hearts.content;


import dev.sterner.victus.Victus;
import dev.sterner.victus.hearts.HeartAspect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GrilledAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("grilled"), 3, 60, 0x71413B, GrilledAspect::new);

    public GrilledAspect(Player player) {
        super(player, TYPE);
    }

    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        player.eat(player.level(), new ItemStack(Items.COOKED_MUTTON));
        return false;
    }

}
