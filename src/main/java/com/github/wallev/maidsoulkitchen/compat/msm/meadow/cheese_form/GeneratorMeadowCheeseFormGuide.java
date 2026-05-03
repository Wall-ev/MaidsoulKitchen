package com.github.wallev.maidsoulkitchen.compat.msm.meadow.cheese_form;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.ILdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.storage.ContainerStorage;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.GuideTest;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.meadow.core.block.entity.CheeseFormBlockEntity;
import net.satisfy.meadow.core.recipes.CheeseFormRecipe;
import net.satisfy.meadow.core.registry.ObjectRegistry;
import net.satisfy.meadow.core.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_MEADOW_CHEESE_FORM)
public class GeneratorMeadowCheeseFormGuide implements ILdCookingPotGuideGenerator<CheeseFormRecipe, CheeseFormBlockEntity> {
    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof CheeseFormBlockEntity;
    }

    @Override
    public boolean isHeated(CheeseFormBlockEntity be) {
        return true;
    }

    @Override
    public RecipeType<CheeseFormRecipe> getRecipeType() {
        return RecipeRegistry.CHEESE.get();
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod(Mods.DM, RecipeRegistry.CHEESE.get().toString());
    }

    @Override
    public void inputsStep(BlockPos pos, CraftGuideOperator2 craftGuide, List<ItemStack> realItems) {
        List<ItemStack> firstItem = Collections.singletonList(realItems.remove(0));
        CraftGuideOperator2.forEachSingleItem(firstItem, is -> {
            craftGuide.addItemInsert(ContainerStorage.TYPE, Direction.UP, is);
        });

        CraftGuideOperator2.forEachSingleItem(realItems, is -> {
            craftGuide.addItemInsert(ContainerStorage.TYPE, Direction.EAST, is);
        });
    }

    @Override
    public int getRecipeTime(CheeseFormRecipe recipe) {
        return 1800;
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ObjectRegistry.CHEESE_FORM.get().asItem();
    }
}
