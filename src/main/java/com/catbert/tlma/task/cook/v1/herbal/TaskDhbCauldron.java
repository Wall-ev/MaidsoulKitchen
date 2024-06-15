package com.catbert.tlma.task.cook.v1.herbal;

import com.catbert.tlma.TLMAddon;
import com.catbert.tlma.foundation.utility.Mods;
import com.catbert.tlma.task.cook.v1.common.TaskLdContainerCook;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import satisfy.herbalbrews.blocks.entity.CauldronBlockEntity;
import satisfy.herbalbrews.recipe.CauldronRecipe;
import satisfy.herbalbrews.registry.ObjectRegistry;
import satisfy.herbalbrews.registry.RecipeTypeRegistry;

import java.util.List;
import java.util.Map;

@LittleMaidExtension
public class TaskDhbCauldron extends TaskLdContainerCook<CauldronBlockEntity, CauldronRecipe> {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "dhb_cauldron");

    @Override
    public boolean canLoaded() {
        return Mods.DHB.isLoaded;
    }

    @Override
    public boolean isHeated(CauldronBlockEntity be) {
        return true;
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getInputSize() {
        return 5;
    }

    @Override
    public Container getContainer(CauldronBlockEntity be) {
        return be;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof CauldronBlockEntity;
    }

    @Override
    public RecipeType<CauldronRecipe> getRecipeType() {
        return RecipeTypeRegistry.CAULDRON_RECIPE_TYPE.get();
    }

    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.CAULDRON.get().asItem().getDefaultInstance();
    }

    @Override
    protected void tExtraStartRecipe(CauldronRecipe recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
        extraRecipe(Items.GLASS_BOTTLE.asItem(), recipe, available, single, canMake, itemTimes, invIngredient);
    }
}
