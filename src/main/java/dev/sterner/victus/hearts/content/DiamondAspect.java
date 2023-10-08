package dev.sterner.victus.hearts.content;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.sterner.victus.Victus;
import dev.sterner.victus.hearts.HeartAspect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class DiamondAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("diamond"), 1, 50, 0x00D4C9, ALWAYS_UPDATE, DiamondAspect::new);

    private final Multimap<Attribute, AttributeModifier> modifiers;

    public DiamondAspect(Player player) {
        super(player, TYPE);

        modifiers = HashMultimap.create(2, 1);
        modifiers.put(Attributes.ARMOR, new AttributeModifier(UUID.randomUUID(), "Diamond Heart Aspect Armor", 4, AttributeModifier.Operation.ADDITION));
        modifiers.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "Diamond Heart Aspect Armor Toughness", 1, AttributeModifier.Operation.ADDITION));
    }

    @Override
    protected void readCustomData(CompoundTag nbt) {
        readModifier(nbt, Attributes.ARMOR, "Armor");
        readModifier(nbt, Attributes.ARMOR_TOUGHNESS, "ArmorToughness");
    }

    private void readModifier(CompoundTag nbt, Attribute attribute, String key) {
        if (nbt.contains(key, Tag.TAG_COMPOUND)) {
            var modifierNbt = nbt.getCompound(key);
            this.modifiers.removeAll(attribute);
            this.modifiers.put(attribute, AttributeModifier.load(modifierNbt));
        }
    }

    @Override
    protected void writeCustomData(CompoundTag nbt) {
        writeFirstModifier(nbt, Attributes.ARMOR, "Armor");
        writeFirstModifier(nbt, Attributes.ARMOR_TOUGHNESS, "ArmorToughness");
    }

    private void writeFirstModifier(CompoundTag nbt, Attribute attribute, String key) {
        modifiers.get(attribute).stream().findAny().ifPresent(modifier -> {
            nbt.put(key, modifier.save());
        });
    }

    @Override
    public void update() {
        player.getAttributes().addTransientAttributeModifiers(modifiers);
    }

    @Override
    protected boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        player.getAttributes().removeAttributeModifiers(modifiers);
        return false;
    }
}
