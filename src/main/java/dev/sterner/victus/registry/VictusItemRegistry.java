package dev.sterner.victus.registry;

import dev.sterner.victus.Victus;
import dev.sterner.victus.hearts.content.*;
import dev.sterner.victus.item.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VictusItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Victus.MODID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Victus.MODID);

    public static final RegistryObject<Item> GRILLED_HEART_ASPECT = ITEMS.register("grilled_heart_aspect", () -> new HeartAspectItem(GrilledAspect.TYPE));
    public static final RegistryObject<Item> BUNDLE_HEART_ASPECT = ITEMS.register("bundle_heart_aspect", () -> new HeartAspectItem(BundleAspect.TYPE));
    public static final RegistryObject<Item> CREEPER_HEART_ASPECT = ITEMS.register("creeper_heart_aspect", () -> new HeartAspectItem(CreeperAspect.TYPE));
    public static final RegistryObject<Item> DIAMOND_HEART_ASPECT = ITEMS.register("diamond_heart_aspect", () -> new HeartAspectItem(DiamondAspect.TYPE));
    public static final RegistryObject<Item> LIGHT_HEART_ASPECT = ITEMS.register("light_heart_aspect", () -> new HeartAspectItem(LightAspect.TYPE));
    public static final RegistryObject<Item> OCEAN_HEART_ASPECT = ITEMS.register("ocean_heart_aspect", () -> new HeartAspectItem(OceanAspect.TYPE));
    public static final RegistryObject<Item> TOTEM_HEART_ASPECT = ITEMS.register("totem_heart_aspect", () -> new HeartAspectItem(TotemAspect.TYPE));
    public static final RegistryObject<Item> POTION_HEART_ASPECT = ITEMS.register("potion_heart_aspect", () -> new HeartAspectItem(PotionAspect.TYPE));
    public static final RegistryObject<Item> ARCHERY_HEART_ASPECT = ITEMS.register("archery_heart_aspect", () -> new HeartAspectItem(ArcheryAspect.TYPE));
    public static final RegistryObject<Item> BLAZING_HEART_ASPECT = ITEMS.register("blazing_heart_aspect", () -> new HeartAspectItem(BlazingAspect.TYPE));
    public static final RegistryObject<Item> DRACONIC_HEART_ASPECT = ITEMS.register("draconic_heart_aspect", () -> new HeartAspectItem(DraconicAspect.TYPE));
    public static final RegistryObject<Item> EMERALD_HEART_ASPECT = ITEMS.register("emerald_heart_aspect", () -> new HeartAspectItem(EmeraldAspect.TYPE));
    public static final RegistryObject<Item> EVOKING_HEART_ASPECT = ITEMS.register("evoking_heart_aspect", () -> new HeartAspectItem(EvokingAspect.TYPE));
    public static final RegistryObject<Item> GOLDEN_HEART_ASPECT = ITEMS.register("golden_heart_aspect", () -> new HeartAspectItem(GoldenAspect.TYPE));
    public static final RegistryObject<Item> ICY_HEART_ASPECT = ITEMS.register("icy_heart_aspect", () -> new HeartAspectItem(IcyAspect.TYPE));
    public static final RegistryObject<Item> IRON_HEART_ASPECT = ITEMS.register("iron_heart_aspect", () -> new HeartAspectItem(IronAspect.TYPE));
    public static final RegistryObject<Item> LAPIS_HEART_ASPECT = ITEMS.register("lapis_heart_aspect", () -> new HeartAspectItem(LapisAspect.TYPE));
    public static final RegistryObject<Item> SWEET_HEART_ASPECT = ITEMS.register("sweet_heart_aspect", () -> new HeartAspectItem(SweetAspect.TYPE));
    public static final RegistryObject<Item> CHEESE_HEART_ASPECT = ITEMS.register("cheese_heart_aspect", () -> new HeartAspectItem(CheeseAspect.TYPE));
    public static final RegistryObject<Item> VOID_HEART_ASPECT = ITEMS.register("void_heart_aspect", () -> new VoidAspectItem());
    public static final RegistryObject<Item> BROKEN_HEART = ITEMS.register("broken_heart", () -> new BrokenHeartItem());
    public static final RegistryObject<Item> BLANK_HEART_ASPECT = ITEMS.register("blank_heart_aspect", () -> new BlankAspectItem());

//    public static final Item VICTUS_JOURNAL = BookItem.registerForBook(Victus.id("journal"), Victus.id("victus_journal"), new OwoItemSettings().group(Victus.VICTUS_GROUP).maxCount(1));
    public static final RegistryObject<JournalItem> VICTUS_JOURNAL = ITEMS.register("victus_journal",
        () -> new JournalItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Potion> HEARTBLEED = POTIONS.register(
            "heartbleed",
            () -> new Potion("heartbleed", new MobEffectInstance(VictusStatusEffectRegistry.HEARTBLEED.get(), 400))
    );

    public static final RegistryObject<Potion> RESURGENCE = POTIONS.register(
            "resurgence",
            () -> new Potion("resurgence", new MobEffectInstance(VictusStatusEffectRegistry.RESURGENCE.get(), 400))
    );

    public static final RegistryObject<Potion> LONG_RESURGENCE = POTIONS.register(
            "long_resurgence",
            () -> new Potion("resurgence", new MobEffectInstance(VictusStatusEffectRegistry.RESURGENCE.get(), 800))
    );

    public static final RegistryObject<Potion> STRONG_RESURGENCE = POTIONS.register(
            "strong_resurgence",
            () -> new Potion("resurgence", new MobEffectInstance(VictusStatusEffectRegistry.RESURGENCE.get(), 200, 1))
    );



    public static MutableComponent coloredTranslationWithPrefix(String key, int color) {
        return Component.translatable("victusforge.aspect_item_prefix").append(Component.translatable(key).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color))));
    }
}
