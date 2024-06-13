package com.catbert.tlma.task.cook.bnc;

import com.catbert.tlma.TLMAddon;
import com.catbert.tlma.foundation.utility.Mods;
import com.catbert.tlma.mixin.bnc.KegBlockEntityAccessor;
import com.catbert.tlma.task.cook.common.TaskFdPot;
import com.catbert.tlma.task.cook.handler.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegRecipe;
import umpaz.brewinandchewin.common.registry.BCBlocks;
import umpaz.brewinandchewin.common.registry.BCRecipeTypes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@LittleMaidExtension
public class TaskBncKey extends TaskFdPot<KegRecipe, KegBlockEntity> {

    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "bnc_key");

    @Override
    public boolean modLoaded() {
        return Mods.BNC.isLoaded;
    }

    @Override
    public MaidRecipesManager<KegRecipe> getRecipesManager(EntityMaid maid) {
        return new MaidRecipesManager<>(maid, getRecipeType(), false){
            @Override
            protected void extraRecipe(KegRecipe recipe, Map<Item, Integer> available, boolean single, boolean canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
                ItemStack fluidItem = recipe.getFluidItem();
                boolean hasIngredient = false;
                for (Item item : available.keySet()) {
                    ItemStack stack = item.getDefaultInstance();
                    if (fluidItem.is(item)) {
                        invIngredient.add(item);
                        hasIngredient = true;

                        if (stack.getMaxStackSize() == 1) {
                            single = true;
                            itemTimes.put(item, 1);
                        } else {
                            itemTimes.merge(item, 1, Integer::sum);
                        }

                        break;
                    }
                }

                if (!hasIngredient) {
                    canMake = false;
                    itemTimes.clear();
                    invIngredient.clear();
                }
            }
        };
    }

    @Override
    public ItemStackHandler getItemStackHandler(KegBlockEntity be) {
        return be.getInventory();
    }

    @Override
    public Optional<KegRecipe> getMatchingRecipe(KegBlockEntity be, RecipeWrapper recipeWrapper) {
        return ((KegBlockEntityAccessor)be).getMatchingRecipe$tlma(recipeWrapper);
    }

    @Override
    public boolean canCook(KegBlockEntity be, KegRecipe recipe) {
        return ((KegBlockEntityAccessor)be).canCook$tlma(recipe, be.getLevel());
    }

    @Override
    public int getOutputSlot() {
        return KegBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getInputEndSlot() {
        return 4;
    }

    @Override
    public int getMealStackSlot() {
        return KegBlockEntity.DRINK_DISPLAY_SLOT;
    }

    @Override
    public int getContainerStackSlot() {
        return KegBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public ItemStack getFoodContainer(KegBlockEntity blockEntity) {
        return blockEntity.getContainer();
    }

    @Override
    public boolean isHeated(KegBlockEntity be) {
        return true;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof KegBlockEntity;
    }

    @Override
    public RecipeType<KegRecipe> getRecipeType() {
        return BCRecipeTypes.FERMENTING.get();
    }

    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return BCBlocks.KEG.get().asItem().getDefaultInstance();
    }
}
