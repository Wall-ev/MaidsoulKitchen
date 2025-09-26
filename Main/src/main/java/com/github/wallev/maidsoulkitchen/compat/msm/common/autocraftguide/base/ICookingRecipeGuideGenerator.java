package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base;

import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ICookingRecipeGuideGenerator<R extends Recipe<? extends Container>> extends ICookingGuideGenerator<R>, IRecipeGuideGenerator<R> {
//public interface ICookingRecipeGuideGenerator<R extends Recipe<C>, C extends Container> extends ICookingGuideGenerator<R>, IRecipeGuideGenerator<R> {
    @Override
    default ResourceLocation getRecipeId(R recipe) {
        return recipe.getId();
    }

    @Override
    @NotNull
    default ResourceLocation getType() {
        return VResourceLocation.createTypeMod(VResourceLocation.create(getRecipeType().toString()));
    }

    @Override
    default void consumeRecipes(RecipeManager manager, Consumer<R> recipeConsumer) {
        IRecipeGuideGenerator.super.consumeRecipes(manager, recipeConsumer);
    }

    @Override
    default List<ItemStack> getRemains(R recipe, List<ItemStack> inputs) {
        List<ItemStack> remainingItems = recipe.getRemainingItems(this.convert2InputsInv(inputs));
        return remainingItems;
    }

    /**
     * 转换为输入容器。用于获取input后的remainItem。
     *
     * @param allInputs 所有输入
     * @return 输入容器
     * @param <T>       输入容器类型
     */
    @SuppressWarnings("unchecked")
    <T extends Container> T convert2InputsInv(List<ItemStack> allInputs);

    default <T1, T2 extends Container> T2 defaultRecipeWrapper(T1 container, BiConsumer<Integer, ItemStack> consumer, Function<T1, T2> function, List<ItemStack> allInputs) {
        for (int i = 0; i < allInputs.size(); i++) {
            ItemStack itemStack = allInputs.get(i);
            consumer.accept(i, itemStack);
        }
        return function.apply(container);
    }

    default RecipeWrapper recipeWrapperContainer(List<ItemStack> allInputs) {
        ItemStackHandler itemStackHandler = new ItemStackHandler(allInputs.size());
        return defaultRecipeWrapper(
                itemStackHandler,
                itemStackHandler::setStackInSlot,
                RecipeWrapper::new,
                allInputs
        );
    }

    default SimpleContainer simpleContainer(List<ItemStack> allInputs) {
        SimpleContainer simpleContainer = new SimpleContainer(allInputs.size());
        for (int i = 0; i < simpleContainer.getContainerSize(); i++) {
            simpleContainer.setItem(i, allInputs.get(i));
        }
        return simpleContainer;
    }

    default Container RecipeWrapper(List<ItemStack> allInputs) {
        ItemStackHandler itemStackHandler = new ItemStackHandler(allInputs.size());
        return defaultRecipeWrapper(
                itemStackHandler,
                itemStackHandler::setStackInSlot,
                RecipeWrapper::new,
                allInputs
        );
    }

//    default Container defaultContainerRecipeWrapper(List<ItemStack> allInputs) {
//        ItemStackHandler container = new ItemStackHandler(allInputs.size());
//        for (int i = 0; i < allInputs.size(); i++) {
//            ItemStack itemStack = allInputs.get(i);
//            container.setStackInSlot(i, itemStack);
//        }
//        RecipeWrapper inputsWrapper = new RecipeWrapper(container);
//        return inputsWrapper;
//    }

    default List<Ingredient> getInputs(R recipe) {
        return IRecipeGuideGenerator.super.getInputs(recipe);
    }

    default List<ItemStack> getOutputs(R recipe, RegistryAccess registryAccess) {
        return IRecipeGuideGenerator.super.getOutputs(recipe, registryAccess);
    }
}
