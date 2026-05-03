package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot;

import com.github.wallev.maidsoulkitchen.compat.msm.common.storage.ContainerStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

//public interface ILdCookingPotGuideGenerator<R extends Recipe<C>, C extends Container, B extends BlockEntity> extends IFdCookingPotGuideGenerator<R, C, B> {
public interface ILdCookingPotGuideGenerator<R extends Recipe<? extends Container>, B extends BlockEntity> extends IFdCookingPotGuideGenerator<R, B> {

    @Override
    default ResourceLocation getInputStorageType() {
        return ContainerStorage.TYPE;
    }

    @Override
    default ResourceLocation getOutputContainerStorageType() {
        return ContainerStorage.TYPE;
    }

    @Override
    default ResourceLocation getOutputStorageType() {
        return ContainerStorage.TYPE;
    }

    @Override
    default ResourceLocation getRemainStorageType() {
        return ContainerStorage.TYPE;
    }

    @Override
    default  <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    default boolean matchResultCount() {
        return true;
    }
}
