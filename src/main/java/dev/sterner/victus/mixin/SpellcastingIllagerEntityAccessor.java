package dev.sterner.victus.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpellcasterIllager.class)
public interface SpellcastingIllagerEntityAccessor {

    @Accessor("spellCastingTickCount")
    void victus_setSpellTicks(int spellTicks);

    @Invoker("getCastingSoundEvent")
    SoundEvent victus_invokeGetCastSpellSound();
}
