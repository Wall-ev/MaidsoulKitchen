package com.catbert.tlma.task.cook.v1.bakery;

import com.catbert.tlma.TLMAddon;
import com.catbert.tlma.foundation.utility.Mods;
import com.catbert.tlma.task.cook.v1.common.TaskLdContainerCook;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import satisfy.bakery.block.entity.CookingPotBlockEntity;
import satisfy.bakery.recipe.CookingPotRecipe;
import satisfy.bakery.registry.RecipeTypeRegistry;
import satisfy.bakery.registry.ObjectRegistry;

import java.util.List;
import java.util.Map;

@LittleMaidExtension
public class TaskDbkCookingPot extends TaskLdContainerCook<CookingPotBlockEntity, CookingPotRecipe> {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "dkb_cooking_pot");

    @Override
    public boolean canLoaded() {
        return Mods.DBK.isLoaded;
    }

    @Override
    public boolean isHeated(CookingPotBlockEntity be) {
        return be.isBeingBurned();
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getInputStartSlot() {
        return 1;
    }

    @Override
    public int getInputSize() {
        return 7;
    }

    @Override
    public Container getContainer(CookingPotBlockEntity be) {
        return be;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof CookingPotBlockEntity;
    }

    @Override
    public RecipeType<CookingPotRecipe> getRecipeType() {
        return RecipeTypeRegistry.COOKING_POT_RECIPE_TYPE.get();
    }

    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.SMALL_COOKING_POT.get().asItem().getDefaultInstance();
    }

    @Override
    protected void tExtraEndRecipe(CookingPotRecipe recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
        extraRecipe(recipe.getContainer().getItem(), recipe, available, single, canMake, itemTimes, invIngredient);
    }
}
