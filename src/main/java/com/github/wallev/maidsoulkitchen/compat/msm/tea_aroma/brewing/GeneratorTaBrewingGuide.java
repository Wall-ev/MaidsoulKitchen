package com.github.wallev.maidsoulkitchen.compat.msm.tea_aroma.brewing;

import cn.foggyhillside.tea_aroma.items.KettleItem;
import cn.foggyhillside.tea_aroma.recipe.BrewingRecipe;
import cn.foggyhillside.tea_aroma.registry.ModItems;
import cn.foggyhillside.tea_aroma.registry.ModRecipeTypes;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.nbtcustom.NbtItemTagGen;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.tea.ITeaGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.tea_aroma.util.TeaBrewingFoamHelper;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_TA_BREWING)
public class GeneratorTaBrewingGuide implements ITeaGuideGenerator<BrewingRecipe> {
//public class GeneratorTaBrewingGuide implements ITeaGuideGenerator<BrewingRecipe, SimpleContainer> {

    @NbtItemTagGen(TaskInfo.MSM_TA_BREWING)
    public static final Item NBT_ITEM = ModItems.KETTLE.get();

    @Override
    public ItemStack leftFluidTeaBase(ItemStack itemStack) {
        return TeaBrewingFoamHelper.leftTeaFluidBase(itemStack);
    }

    @Override
    public List<Ingredient> getCups(BrewingRecipe recipe) {
        return List.of(Ingredient.of(ModItems.CUP.get()));
    }

    @Override
    public List<Ingredient> getTeaLeaves(BrewingRecipe recipe) {
        return Lists.newArrayList(recipe.getIngredients());
    }

    @Override
    public List<Ingredient> getFluidTeaBase(BrewingRecipe recipe) {
//        String liquidType = recipe.getLiquidType();
//        Ingredient kettleIngredient = TeaBrewingFoamHelper.forLiquidIngredient(liquidType);

//        boilingWaterKettle.getTags().toList().stream().anyMatch(itemTagKey -> itemTagKey.equals(ItemStackUtil.matchNbt))



        ItemStack boilingWaterKettle = KettleItem.getBoilingWaterKettle();
        Ingredient kettleIngredient = Ingredient.of(boilingWaterKettle);
        return List.of(kettleIngredient);
    }

    @Override
    public void generateSteps(BlockPos pos, Level level, BrewingRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        ITeaGuideGenerator.super.generateSteps(pos, level, recipe, craftGuide, realItems, needContainer, containers, outputs, remains);
    }

    @Override
    public List<ItemStack> getTea(BrewingRecipe recipe, RegistryAccess registryAccess) {
        return List.of(recipe.getResultItem(registryAccess));
    }

    @Override
    @NotNull
    public ResourceLocation getType() {
        assert ModRecipeTypes.BREWING_RECIPE.getId() != null;
        return VResourceLocation.createTypeMod(ModRecipeTypes.BREWING_RECIPE.getId());
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) simpleContainer(allInputs);
    }

    @Override
    public Item getBlockItemForTranslate() {
        return NBT_ITEM;
    }

    @Override
    public RecipeType<BrewingRecipe> getRecipeType() {
        return BrewingRecipe.Type.INSTANCE;
    }
}
