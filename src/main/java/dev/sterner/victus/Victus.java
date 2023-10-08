package dev.sterner.victus;

import com.mojang.logging.LogUtils;
import dev.sterner.victus.hearts.HeartAspectRegistry;
import dev.sterner.victus.registry.VictusItemRegistry;
import dev.sterner.victus.registry.VictusStatusEffectRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(Victus.MODID)
public class Victus {
    public static final String MODID = "victusforge";
    public static final Logger LOGGER = LogUtils.getLogger();


    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);


    public static final RegistryObject<CreativeModeTab> VICTUS_TAB = CREATIVE_MODE_TABS.register("victus_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> VictusItemRegistry.VOID_HEART_ASPECT.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(VictusItemRegistry.VICTUS_JOURNAL.get());
                output.accept(VictusItemRegistry.GRILLED_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.BUNDLE_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.CREEPER_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.DIAMOND_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.LIGHT_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.OCEAN_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.TOTEM_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.POTION_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.ARCHERY_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.BLAZING_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.DRACONIC_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.EMERALD_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.EVOKING_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.GOLDEN_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.ICY_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.IRON_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.LAPIS_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.SWEET_HEART_ASPECT.get());
                output.accept(VictusItemRegistry.CHEESE_HEART_ASPECT.get());
            }).build());

    public Victus() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        HeartAspectRegistry.registerDefaults();
        VictusItemRegistry.ITEMS.register(modEventBus);
        VictusStatusEffectRegistry.EFFECTS.register(modEventBus);
        VictusItemRegistry.POTIONS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);


        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @SubscribeEvent
    void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.HARMING)),
                    Ingredient.of(Items.GLOW_LICHEN),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), VictusItemRegistry.HEARTBLEED.get()))
            );
            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.REGENERATION)),
                    Ingredient.of(Items.MAGMA_CREAM),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), VictusItemRegistry.RESURGENCE.get()))
            );

            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), VictusItemRegistry.RESURGENCE.get())),
                    Ingredient.of(Items.REDSTONE),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), VictusItemRegistry.LONG_RESURGENCE.get()))
            );

            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                    Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), VictusItemRegistry.RESURGENCE.get())),
                    Ingredient.of(Items.GLOWSTONE),
                    PotionUtils.setPotion(new ItemStack(Items.POTION), VictusItemRegistry.STRONG_RESURGENCE.get()))
            );
        });
    }

    public static ResourceLocation id(String s) {
        return new ResourceLocation(MODID, s);
    }
}
