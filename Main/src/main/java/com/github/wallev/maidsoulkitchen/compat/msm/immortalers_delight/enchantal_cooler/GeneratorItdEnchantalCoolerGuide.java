package com.github.wallev.maidsoulkitchen.compat.msm.immortalers_delight.enchantal_cooler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.google.common.collect.Lists;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightItems;
import com.renyigesai.immortalers_delight.recipe.EnchantalCoolerRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import studio.fantasyit.maid_storage_manager.craft.CollectCraftEvent;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOption;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.action.CraftAction;
import studio.fantasyit.maid_storage_manager.craft.action.PathTargetLocator;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.storage.ItemHandler.ItemHandlerStorage;

import java.util.ArrayList;
import java.util.List;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_IMD_ENCHANTAL_COOLER)
public class GeneratorItdEnchantalCoolerGuide implements ICookingRecipeGuideGenerator<EnchantalCoolerRecipe> {
//public class GeneratorItdEnchantalCoolerGuide implements ICookingRecipeGuideGenerator<EnchantalCoolerRecipe, SimpleContainer> {

    public GeneratorItdEnchantalCoolerGuide(CollectCraftEvent event) {
        event.addAutoCraftGuideGenerator(this);
        event.addAction(
                EnchantalCoolerSideSpitItemAction.TYPE,
                EnchantalCoolerSideSpitItemAction::new,
                PathTargetLocator::touchPos,
                CraftAction.PathEnoughLevel.CLOSER.value,
                false,
                CraftAction.MARK_HAND_RELATED,
                4,
                4,
                List.of(ActionOption.OPTIONAL, EnchantalCoolerSideSpitItemAction.OPTION_SPLIT_TYPE)
        );
    }

    @Override
    public void inputsStep(BlockPos pos, CraftGuideOperator2 craftGuide, List<ItemStack> realItems) {
        ItemStack fuel = realItems.remove(0);
        craftGuide.addStep(new CraftGuideStepData(
                TargetUtil.makeTargetNoSide(ItemHandlerStorage.TYPE, pos),
                List.of(fuel),
                List.of(),
                EnchantalCoolerSideSpitItemAction.TYPE,
                ActionOptionSet.with(EnchantalCoolerSideSpitItemAction.OPTION_SPLIT_TYPE, EnchantalCoolerSideSpitItemAction.SpiltType.FUEL)
        ));

        ICookingRecipeGuideGenerator.super.inputsStep(pos, craftGuide, realItems);
    }

    @Override
    public void outputContainerStep(BlockPos pos, boolean needContainer, CraftGuideOperator2 craftGuide, List<ItemStack> containers) {
        craftGuide.addStep(new CraftGuideStepData(
                TargetUtil.makeTargetNoSide(ItemHandlerStorage.TYPE, pos),
                containers,
                List.of(),
                EnchantalCoolerSideSpitItemAction.TYPE,
                ActionOptionSet.with(EnchantalCoolerSideSpitItemAction.OPTION_SPLIT_TYPE, EnchantalCoolerSideSpitItemAction.SpiltType.CONTAINER)
        ));
    }

    @Override
    public List<Ingredient> getInputs(EnchantalCoolerRecipe recipe) {
        ArrayList<Ingredient> allInputs = Lists.newArrayList(Ingredient.of(Items.LAPIS_LAZULI));
        allInputs.addAll(recipe.getIngredients());
        return allInputs;
    }

    @Override
    public List<Ingredient> getContainers(EnchantalCoolerRecipe recipe) {
        return List.of(Ingredient.of(recipe.getContainer()));
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ImmortalersDelightItems.ENCHANTAL_COOLER.get();
    }

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    public RecipeType<EnchantalCoolerRecipe> getRecipeType() {
        return EnchantalCoolerRecipe.Type.INSTANCE;
    }

    @Override
    @NotNull
    public ResourceLocation getType() {
        return VResourceLocation.createTypeMod(EnchantalCoolerRecipe.Serializer.ID);
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) simpleContainer(allInputs);
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(ImmortalersDelightBlocks.ENCHANTAL_COOLER.get());
    }
}
