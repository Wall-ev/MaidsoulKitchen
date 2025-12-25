package com.github.wallev.maidsoulkitchen.compat.msm.youkaishomecoming.cusine;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.click.IOnlyUseGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.GuideTest;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.google.common.collect.Lists;
import dev.xkmc.youkaishomecoming.content.pot.table.board.CuisineBoardBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.table.item.VariantTableItemBase;
import dev.xkmc.youkaishomecoming.content.pot.table.recipe.CuisineRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_YHC_CUISINE)
public class GeneratorYhcCuisineGuide implements IOnlyUseGuideGenerator<CuisineRecipe<?>> {

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    public RecipeType<CuisineRecipe<?>> getRecipeType() {
        return YHBlocks.CUISINE_RT.get();
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.ofTypeMod(YHBlocks.CUISINE_RT.getId());
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof CuisineBoardBlockEntity;
    }

    @Override
    public void generateSteps(BlockPos pos, Level level, CuisineRecipe<?> recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        List<Ingredient> ingredients = collectIngredients(recipe);
        List<Integer> handIndexes = new ArrayList<>();
        for (int i = 0, emptyHand = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).isEmpty()) {
                handIndexes.add(i - 1 - emptyHand);
                emptyHand++;
            }
        }

        for (int i = 0; i < realItems.size(); i++) {
            ItemStack itemStack = realItems.get(i);
            // 放置食物
            craftGuide.addItemUse(itemStack);
            // 空手捏捏
            if (handIndexes.contains(i)) {
                craftGuide.addEmptyUse(pos);
            }
        }

        // 空手捏捏取出成品
        craftGuide.addEmptyUse(outputs);
    }

    @Override
    public int getRecipeTime(CuisineRecipe<?> recipe) {
        return 0;
    }

    @Override
    public List<Ingredient> getInputs(CuisineRecipe<?> recipe) {
        return collectIngredients(recipe)
                .stream()
                .filter(ingredient -> !ingredient.isEmpty())
                .toList();
    }

    @Override
    public Item getBlockItemForTranslate() {
        return YHBlocks.CUISINE_BOARD.asItem();
    }

    @Override
    public String getRecipeTranslateKeyFromJei() {
        return IOnlyUseGuideGenerator.super.getRecipeTranslateKeyFromJei();
    }

    protected List<Ingredient> collectIngredients(CuisineRecipe<?> recipe) {
        List<Ingredient> listBase = new ArrayList<>();
        List<Ingredient> listRecipe = new ArrayList<>(recipe.getCustomIngredients());
        VariantTableItemBase base = VariantTableItemBase.MAP.get(recipe.base());
        if (base != null) {
            base.collectIngredients(listBase, listRecipe);
        } else {
            Item item = ForgeRegistries.ITEMS.getValue(recipe.base());
            listBase.add(Ingredient.of(item));
        }

        List<Ingredient> all = Lists.newArrayList(listBase);
        all.addAll(listRecipe);
        return all;
    }
}
