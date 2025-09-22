package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.plate;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.IRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import studio.fantasyit.maid_storage_manager.craft.generator.config.ConfigTypes;

import java.util.List;
import java.util.function.Consumer;

public abstract class IPlateGuideGenerator<R extends Recipe<?>> implements ICookingGuideGenerator<R>, IRecipeGuideGenerator<R> {

    protected ConfigTypes.ConfigType<Integer> COUNT = new ConfigTypes.ConfigType<>(
            "count",
            this.getMaxSingleCount(),
            Component.translatable("config.maid_storage_manager.crafting.generating.maid_storage_manager." + this.toTypeStr() + ".count"),
            ConfigTypes.ConfigTypeEnum.Integer
    );

    @Override
    @NotNull
    public ResourceLocation getType() {
        return VResourceLocation.createTypeMod(this.getRecipeType().toString());
    }

    /**
     * 等待时间
     * @return 等待时间
     */
    public abstract int waitTime(R recipe);

    @Override
    public void generateSteps(BlockPos pos, Level level, R recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        // 物品使用
        List<ItemStack> withCountRealItems = realItems.stream()
                .map(itemStack -> itemStack.copyWithCount(COUNT.getValue()))
                .toList();
        CraftGuideOperator2.forEachSingleItem(withCountRealItems, craftGuide::addItemUse);

        // 物品合成
        craftGuide.addIdle(waitTime(recipe) + Math.min(0, COUNT.getValue() - 1));

        // 物品拾取
        List<ItemStack> withCountOutputs = outputs.stream()
                .map(itemStack -> itemStack.copyWithCount(COUNT.getValue()))
                .toList();
        craftGuide.addItemPickup(withCountOutputs);
    }

    @Override
    public void consumeRecipes(RecipeManager manager, Consumer<R> recipeConsumer) {
        IRecipeGuideGenerator.super.consumeRecipes(manager, recipeConsumer);
    }

    @Override
    public ResourceLocation getRecipeId(R recipe) {
        return IRecipeGuideGenerator.super.getRecipeId(recipe);
    }

    @Override
    public List<Ingredient> getInputs(R recipe) {
        return IRecipeGuideGenerator.super.getInputs(recipe);
    }

    @Override
    public List<ItemStack> getOutputs(R recipe, RegistryAccess registryAccess) {
        return IRecipeGuideGenerator.super.getOutputs(recipe, registryAccess);
    }

    public abstract int getMaxSingleCount();

    @Override
    public List<ConfigTypes.ConfigType<?>> getConfigurations() {
        return List.of(COUNT);
    }
}
