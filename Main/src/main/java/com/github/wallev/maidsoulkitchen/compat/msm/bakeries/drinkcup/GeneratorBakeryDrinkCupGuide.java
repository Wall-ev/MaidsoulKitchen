package com.github.wallev.maidsoulkitchen.compat.msm.bakeries.drinkcup;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonAttackAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonPickupItemAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.EmptyAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.google.common.collect.Lists;
import com.renyigesai.bakeries.init.BakeriesItems;
import com.renyigesai.bakeries.recipe.CoffeeRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;

import java.util.List;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_BAKERY_DRINK_CUP)
public class GeneratorBakeryDrinkCupGuide implements ICookingRecipeGuideGenerator<CoffeeRecipe> {
//public class GeneratorBakeryDrinkCupGuide implements ICookingRecipeGuideGenerator<CoffeeRecipe, SimpleContainer> {

    @Override
    public boolean allowMultiPosition() {
        return true;
    }

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return isValidGroundBlock(level, pos);
    }

    @Override
    @NotNull
    public ResourceLocation getType() {
        return VResourceLocation.createTypeMod(CoffeeRecipe.Serializer.ID);
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) simpleContainer(allInputs);
    }

    @Override
    public RecipeType<CoffeeRecipe> getRecipeType() {
        return CoffeeRecipe.Type.INSTANCE;
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return true;
    }

    @Override
    public List<Ingredient> getInputs(CoffeeRecipe recipe) {
        List<Ingredient> ingredients = Lists.newArrayList(Ingredient.of(BakeriesItems.DRINK_CUP.get()));
        ingredients.addAll(recipe.getIngredients());
        return ingredients;
    }

    @Override
    public Item getBlockItemForTranslate() {
        return BakeriesItems.DRINK_CUP.get();
    }

    @Override
    public void generateSteps(BlockPos pos, Level level, CoffeeRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        // 放置饮料瓶
        ItemStack glassBottle = realItems.get(0);
        craftGuide.addItemUse(glassBottle);

        BlockPos glassBottlePos = pos.above();
        // 放置物品
        CraftGuideOperator2.forEachSingleItem(realItems.subList(1, realItems.size()), realItem -> {
            ItemStack craftingRemainingItem = realItem.getCraftingRemainingItem();
            if (!craftingRemainingItem.isEmpty()) {
                craftGuide.addItemUse(glassBottlePos, realItem.copyWithCount(1), craftingRemainingItem);
            } else {
                craftGuide.addItemUse(glassBottlePos, realItem.copyWithCount(1));
            }

        });

        // 空手摇两次
        craftGuide.addEmptyUse(glassBottlePos);

        List<ItemStack> remainItems = realItems.stream()
                .map(ItemStack::getCraftingRemainingItem)
                .filter(itemStack -> !itemStack.isEmpty())
                .toList();

        // 打掉饮料瓶[失败步骤]
        CraftGuideStepData failStep0 = EnchantCommonAttackAction.createStep(glassBottlePos);
        List<ItemStack> allItems = Lists.newArrayList(realItems);
        allItems.addAll(remainItems);
        // 拾取原材料[失败步骤]
        CraftGuideStepData failStep1 = EnchantCommonPickupItemAction.createStep(glassBottlePos, allItems);

        // 获取成品，冰添加失败步骤
        craftGuide.addEmptyUseIfFail(glassBottlePos, outputs, failStep0, failStep1);
    }

    @Override
    public int getRecipeTime(CoffeeRecipe recipe) {
        return 0;
    }
}
