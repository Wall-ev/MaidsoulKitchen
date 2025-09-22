package com.github.wallev.maidsoulkitchen.compat.msm.vinery;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.grape.IGrapeJumpCustomGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.TypeLang;
import com.github.wallev.maidsoulkitchen.mixin.compat.vinery.GrapevinePotBlockAccessor;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.vinery.core.Vinery;
import net.satisfy.vinery.core.block.GrapevinePotBlock;
import net.satisfy.vinery.core.registry.GrapeTypeRegistry;
import net.satisfy.vinery.core.registry.ObjectRegistry;
import net.satisfy.vinery.core.util.GrapeType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_VINERY_GRAPE_POT)
public class GeneratorVineryGrapePotGuide implements IGrapeJumpCustomGuideGenerator<GrapeType> {

    @Override
    public int jumpTime() {
        return 4;
    }

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        BlockState blockState = level.getBlockState(pos);
        return blockState.getBlock() instanceof GrapevinePotBlock && isEmpty(blockState);
    }

    public boolean isEmpty(BlockState blockState) {
        return blockState.getValue(GrapevinePotBlockAccessor.msk$getState()) == 0 && blockState.getValue(GrapevinePotBlockAccessor.msk$getStorage()) == 0;
    }

    /**
     * 获取合成指南生成器类型。建议取RecipeType或者CraftType
     *
     * @return 合成指南生成器类型
     */
    @TypeLang(
            en_us = "Make grape vine",
            zh_cn = "制作葡萄酒(踩葡萄)"
    )
    @Override
    @NotNull
    public ResourceLocation getType() {
        return VResourceLocation.createTypeMod(Vinery.MOD_ID + ":grape");
    }

    @Override
    public List<Ingredient> getInputs(GrapeType recipe) {
        Item fruit = recipe.getFruit();
        ItemStack itemStack = new ItemStack(fruit, 6);
        return List.of(Ingredient.of(itemStack));
    }

    @Override
    public List<Ingredient> getContainers(GrapeType recipe) {
        ItemStack bottle = ObjectRegistry.WINE_BOTTLE.get().getDefaultInstance();
        bottle.setCount(2);
        return List.of(Ingredient.of(bottle));
    }

    @Override
    public List<ItemStack> getOutputs(GrapeType recipe, RegistryAccess registryAccess) {
        ItemStack defaultInstance = recipe.getBottle().getDefaultInstance();
        defaultInstance.setCount(2);
        return List.of(defaultInstance);
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ObjectRegistry.GRAPEVINE_POT.get().asItem();
    }

    /**
     * 消耗配方。用于缓存配方。<b>缓存的配方信息必须和下次添加同ID配方时完全一致，否则可能出现不可预料的错误</b>
     *
     * @param manager        配方管理器
     * @param recipeConsumer 配方消费者
     */
    @Override
    public void consumeRecipes(RecipeManager manager, Consumer<GrapeType> recipeConsumer) {
        GrapeTypeRegistry.GRAPE_TYPE_TYPES.forEach(recipeConsumer);
    }

    /**
     * 获取配方ID。用于缓存配方。<b>缓存的配方ID必须和下次添加同ID配方时完全一致，否则可能出现不可预料的错误</b>
     *
     * @param recipe 配方
     * @return 配方ID
     */
    @Override
    public ResourceLocation getRecipeId(GrapeType recipe) {
        return VResourceLocation.create(Vinery.MOD_ID + ":grape/" + recipe.getSerializedName());
    }

    /**
     * 方块是否合法。用于判断和世界无关的成立条件
     *
     * @param level 世界
     * @param pos   方块位置
     * @return 是否合法
     */
    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() instanceof GrapevinePotBlock;
    }


}
