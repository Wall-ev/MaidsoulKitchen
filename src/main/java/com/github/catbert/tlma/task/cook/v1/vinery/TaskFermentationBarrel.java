package com.github.catbert.tlma.task.cook.v1.vinery;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.catbert.tlma.task.cook.v1.common.TaskLdContainerCook;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import satisfyu.vinery.block.entity.FermentationBarrelBlockEntity;
import satisfyu.vinery.recipe.FermentationBarrelRecipe;
import satisfyu.vinery.registry.ObjectRegistry;
import satisfyu.vinery.registry.RecipeTypesRegistry;

import java.util.List;
import java.util.Map;

@LittleMaidExtension
public class TaskFermentationBarrel extends TaskLdContainerCook<FermentationBarrelBlockEntity, FermentationBarrelRecipe> {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "dv_fermentation_barrel");

    @Override
    public boolean canLoaded() {
        return Mods.DV.isLoaded;
    }

    @Override
    public boolean isHeated(FermentationBarrelBlockEntity be) {
        return true;
    }

    @Override
    public int getOutputSlot() {
        return 5;
    }

    @Override
    public int getInputSize() {
        return 5;
    }

    @Override
    public Container getContainer(FermentationBarrelBlockEntity be) {
        return be;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof FermentationBarrelBlockEntity;
    }

    @Override
    public RecipeType<FermentationBarrelRecipe> getRecipeType() {
        return RecipeTypesRegistry.FERMENTATION_BARREL_RECIPE_TYPE.get();
    }

    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.FERMENTATION_BARREL.get().asItem().getDefaultInstance();
    }

    @Override
    protected boolean tExtraStartRecipe(FermentationBarrelRecipe recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
        // 别问我为什么这里是硬编码,葡园酒香这里就是硬编码...
       return extraRecipe(ObjectRegistry.WINE_BOTTLE.get(), recipe, available, single, canMake, itemTimes, invIngredient);
    }
}
