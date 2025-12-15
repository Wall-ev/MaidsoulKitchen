package com.github.wallev.maidsoulkitchen.init;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.recipe.itemuse.ItemUseRecipe;
import com.github.wallev.maidsoulkitchen.recipe.water.ConsumeWaterRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MaidsoulKitchen.MOD_ID);

    public static RegistryObject<RecipeSerializer<ConsumeWaterRecipe>> CONSUME_WATER_SERIALIZER;
    public static RegistryObject<RecipeSerializer<ItemUseRecipe>> ITEM_USE_SERIALIZER;

    public static RecipeType<ConsumeWaterRecipe> CONSUME_WATER_RECIPE;
    public static RecipeType<ItemUseRecipe> ITEM_USE_RECIPE;

    static {
        if (Mods.MAID_STORAGE_MANAGER.versionLoad()) {
            CONSUME_WATER_SERIALIZER = RECIPE_SERIALIZERS.register("consume_water", ConsumeWaterRecipe.Serializer::new);
            ITEM_USE_SERIALIZER = RECIPE_SERIALIZERS.register("item_use", ItemUseRecipe.Serializer::new);
        }
    }

    @SubscribeEvent
    public static void register(RegisterEvent evt) {
        if (evt.getRegistryKey().equals(Registries.RECIPE_SERIALIZER) && Mods.MAID_STORAGE_MANAGER.versionLoad()) {
            CONSUME_WATER_RECIPE = RecipeType.simple(new ResourceLocation(MaidsoulKitchen.MOD_ID, "consume_water"));
            ITEM_USE_RECIPE = RecipeType.simple(new ResourceLocation(MaidsoulKitchen.MOD_ID, "item_use"));
        }
    }
}
