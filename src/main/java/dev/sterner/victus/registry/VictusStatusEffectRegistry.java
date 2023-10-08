package dev.sterner.victus.registry;

import dev.sterner.victus.Victus;
import dev.sterner.victus.common.TrueDamageStatusEffect;
import dev.sterner.victus.common.VictusStatusEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VictusStatusEffectRegistry {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Victus.MODID);

    public static final RegistryObject<MobEffect> HEARTBLEED = EFFECTS.register("heartbleed", () -> new VictusStatusEffect(MobEffectCategory.NEUTRAL, 0xAE0000));
    public static final RegistryObject<MobEffect> RESURGENCE = EFFECTS.register("resurgance", () -> new VictusStatusEffect(MobEffectCategory.BENEFICIAL, 0xAE7733));
    public static final RegistryObject<MobEffect> TRUE_DAMAGE = EFFECTS.register("true_damage", () -> new TrueDamageStatusEffect());

}
