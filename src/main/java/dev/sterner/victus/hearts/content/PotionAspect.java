package dev.sterner.victus.hearts.content;

import dev.sterner.victus.Victus;
import dev.sterner.victus.hearts.HeartAspect;
import dev.sterner.victus.hearts.OverlaySpriteProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class PotionAspect extends HeartAspect implements OverlaySpriteProvider {

    public static final Type TYPE = new Type(Victus.id("potion"), 7, 20, 0xFFFFFF, PotionAspect::new);

    private Potion potion = Potions.EMPTY;

    public PotionAspect(Player player) {
        super(player, TYPE);
    }

    public Potion getPotion() {
        return potion;
    }

    public void setPotion(Potion potion) {
        this.potion = potion;
    }

    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        List<MobEffectInstance> list = potion.getEffects();

        for (MobEffectInstance statusEffectInstance : list) {
            if (statusEffectInstance.getEffect().isInstantenous()) {
                statusEffectInstance.getEffect().applyInstantenousEffect(player, player, player, statusEffectInstance.getAmplifier(), 1.0D);
            } else {
                player.addEffect(new MobEffectInstance(statusEffectInstance));
            }
        }

        this.setPotion(Potions.EMPTY);
        return false;
    }

    @Override
    protected void readCustomData(CompoundTag nbt) {
        this.potion = ForgeRegistries.POTIONS.getDelegateOrThrow(new ResourceLocation(nbt.getString("Potion"))).get();
    }

    @Override
    protected void writeCustomData(CompoundTag nbt) {
        nbt.putString("Potion", ForgeRegistries.POTIONS.getKey(this.potion).toString());
    }

    @Override
    public int getOverlayIndex() {
        return 8;
    }

    @Override
    public int getOverlayTint() {
        return potion == Potions.EMPTY ? 0xFFFFFF : PotionUtils.getColor(potion);
    }
}
