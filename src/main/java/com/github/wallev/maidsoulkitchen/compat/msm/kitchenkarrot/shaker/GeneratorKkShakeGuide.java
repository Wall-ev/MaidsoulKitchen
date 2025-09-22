package com.github.wallev.maidsoulkitchen.compat.msm.kitchenkarrot.shaker;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.nbtcustom.NbtItemTagGen;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonUseAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.menu.MenuPlaceItemAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.menu.MenuTakeItemAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.TypeLang;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.google.common.collect.Lists;
import io.github.tt432.kitchenkarrot.recipes.recipe.CocktailRecipe;
import io.github.tt432.kitchenkarrot.registries.ModItems;
import io.github.tt432.kitchenkarrot.registries.RecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import studio.fantasyit.maid_storage_manager.craft.CollectCraftEvent;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.action.CraftAction;
import studio.fantasyit.maid_storage_manager.craft.action.PathTargetLocator;
import studio.fantasyit.maid_storage_manager.craft.context.common.CommonUseAction;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;

import java.util.List;

@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_KK_SHAKER)
public class GeneratorKkShakeGuide implements ICookingRecipeGuideGenerator<CocktailRecipe> {
    @NbtItemTagGen(TaskInfo.MSM_KK_SHAKER)
    public static final Item NBT_ITEM = ModItems.COCKTAIL.get();

    public GeneratorKkShakeGuide(CollectCraftEvent event) {
        new ShakeMenuWrap();

        event.addAutoCraftGuideGenerator(this);
        event.addAction(
                PlayShakeSoundAction.TYPE,
                PlayShakeSoundAction::new,
                PathTargetLocator::nearByNoLimitation,
                CraftAction.PathEnoughLevel.CLOSER.value,
                false,
                4,
                4,
                List.of()
        );
        event.addItemStackPredicate(ModItems.SHAKER.get(), ((stack, target) -> {
            IItemHandler stackHandler = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
            IItemHandler targetHandler = target.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
            if (stackHandler == null && targetHandler == null) {
                return true;
            }
            if (stackHandler == null || targetHandler == null) {
                return false;
            }

            for (int i = 0; i < stackHandler.getSlots(); i++) {
                if (!stackHandler.getStackInSlot(i).isEmpty()) {
                    return false;
                }
            }
            for (int i = 0; i < targetHandler.getSlots(); i++) {
                if (!targetHandler.getStackInSlot(i).isEmpty()) {
                    return false;
                }
            }
            return true;
        }));
    }

    @Override
    public void generateSteps(BlockPos pos, Level level, CocktailRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        ItemStack shaker = realItems.remove(0);
        // 模拟打开Gui,并播放声音
        craftGuide.addStep(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(shaker),
                List.of(shaker),
                PlayShakeSoundAction.TYPE
        ));
        // 放入原料
        CraftGuideOperator2.forEachSingleItem(realItems, itemStacks -> {
            craftGuide.addStep(new CraftGuideStepData(
                    TargetUtil.makeTargetVirtualNoSide(pos),
                    List.of(shaker, itemStacks),
                    List.of(shaker),
                    MenuPlaceItemAction.TYPE,
                    MenuPlaceItemAction.Context.to(ShakeMenuWrap.ID, Direction.UP)
            ));
        });
        // 使用
        craftGuide.addStep(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(shaker),
                List.of(shaker),
                EnchantCommonUseAction.TYPE,
                ActionOptionSet.with(EnchantCommonUseAction.OPTION_USE_METHOD, CommonUseAction.USE_TYPE.LONG)
        ));
        // 取出
        List<ItemStack> allOutputs = Lists.newArrayList(shaker);
        allOutputs.addAll(outputs);
        craftGuide.addStep(new CraftGuideStepData(
                TargetUtil.makeTargetVirtualNoSide(pos),
                List.of(shaker),
                allOutputs,
                MenuTakeItemAction.TYPE,
                MenuTakeItemAction.Context.to(ShakeMenuWrap.ID, Direction.DOWN)
        ));
    }

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return true;
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public List<Ingredient> getInputs(CocktailRecipe recipe) {
        List<Ingredient> allInputs = Lists.newArrayList(Ingredient.of(ModItems.SHAKER.get()));
        allInputs.addAll(recipe.getContent().getRecipe());

        return allInputs;
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModItems.SHAKER.get();
    }

    @TypeLang(
            en_us = "Shaker",
            zh_cn = "摇酒壶"
    )
    @Override
    public RecipeType<CocktailRecipe> getRecipeType() {
        return RecipeTypes.COCKTAIL.get();
    }

    @Override
    public boolean positionalAvailable(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return maid.blockPosition.equals(pos);
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return false;
    }
}
