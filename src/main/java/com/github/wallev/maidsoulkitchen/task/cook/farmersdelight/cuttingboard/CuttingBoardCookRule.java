package com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.cuttingboard;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager2;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.TickCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

public class CuttingBoardCookRule extends TickCookRule<CuttingBoardBlockEntity, CuttingBoardRecipe> {
    private static final CuttingBoardCookRule INSTANCE = new CuttingBoardCookRule();

    private boolean maidHand = false;
    private Item processItem = null;

    @Override
    public boolean canMoveTo(CookBeBase<CuttingBoardBlockEntity> cookBeBase, MaidRecipesManager2<CuttingBoardRecipe> rm) {
        CuttingBoardBlockEntity cuttingBoard = cookBeBase.getBe();

        if (!cuttingBoard.isEmpty() && hasBoardStackTool(cookBeBase.getMaid(), cuttingBoard)) {
            return true;
        }

        if (cuttingBoard.getStoredItem().isEmpty() && rm.hasMaidRecs(cookBeBase)) {
            return true;
        }
        return false;
    }

    @Override
    public void cookMake(CookBeBase<CuttingBoardBlockEntity> cookBeBase, MaidRecipesManager2<CuttingBoardRecipe> rm) {
        this.init(cookBeBase, rm);
        ItemStack storedItem = be.getStoredItem();
        if (!storedItem.isEmpty()) {
            ItemStack tool = getBoardStackTool(maid, be);
            if (tool.isEmpty()) {
                this.tickStop(cookBeBase, rm);
                return;
            }

            this.swapItem(InteractionHand.MAIN_HAND, tool, maid, maid.getAvailableInv(true));
            be.processStoredItemUsingTool(tool, null);

            if (!storedItem.isEmpty()) {
                this.processItem = storedItem.getItem();
                be.setChanged();
                return;
            }
        }

        if (rm.hasMaidRecs(cookBeBase)) {
            MaidRec maidRec = rm.pollMaidRec(cookBeBase);
            ItemStack tool = maidRec.tool();
            ItemInventory itemInventory = rm.getItemInventory();
            ItemStack pollTool = itemInventory.getItemStacks(tool.getItem()).poll();
            if (pollTool == null) {
                return;
            }
            this.swapItem(InteractionHand.MAIN_HAND, pollTool, maid, maid.getAvailableInv(true));

            ItemDefinition processItem = maidRec.maidItems().get(0).item();
            ItemStack processItemPoll = itemInventory.getItemStacks(processItem).poll();
            if (processItemPoll == null) {
                return;
            }
            this.swapItem(InteractionHand.OFF_HAND, processItemPoll, maid, maid.getAvailableInv(true));
            this.processItem = processItem.item();

            rm.getItemInventory().markDirty();;
        }
        be.setChanged();
    }

    @Override
    public boolean tickCan(CookBeBase<CuttingBoardBlockEntity> cookBeBase, MaidRecipesManager2<CuttingBoardRecipe> rm) {
        return this.be != null && !maid.getMainHandItem().isEmpty() && this.processItem != null &&
                (maid.getOffhandItem().is(this.processItem) || isProcessItem());
    }

    @Override
    public void tickCookMake(CookBeBase<CuttingBoardBlockEntity> cookBeBase, MaidRecipesManager2<CuttingBoardRecipe> rm) {
        if (tick++ % 5 != 0) {
            return;
        }

        if (maidHand) {
            ItemStack tool = maid.getMainHandItem();
            be.processStoredItemUsingTool(tool, null);
            maid.swing(InteractionHand.MAIN_HAND);
        } else {
            ItemStack offhandItem = maid.getOffhandItem();
            if (offhandItem.is(this.processItem)) {
                ItemStack split = offhandItem.split(1);
                be.getInventory().insertItem(0, split, false);
                maid.swing(InteractionHand.OFF_HAND);
            }
        }
        maidHand = !maidHand;
    }

    private boolean hasBoardStackTool(EntityMaid maid, CuttingBoardBlockEntity blockEntity) {
        return !this.getBoardStackTool(maid, blockEntity).isEmpty();
    }

    private ItemStack getBoardStackTool(EntityMaid maid, CuttingBoardBlockEntity blockEntity) {
        Level level = maid.level;
        CombinedInvWrapper maidInv = maid.getAvailableInv(true);

        IItemHandler inventory = blockEntity.getInventory();
        RecipeWrapper recipeWrapper = new RecipeWrapper((IItemHandlerModifiable) inventory);
        Optional<CuttingBoardRecipe> recipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.CUTTING.get(), recipeWrapper, level);

        if (recipe.isPresent()) {
            Ingredient tool = recipe.get().getTool();
            return ItemsUtil.getStack(maidInv, (itemStack) -> {
                return tool.test(itemStack);
            });
        }

        return ItemStack.EMPTY;
    }

    private boolean isProcessItem() {
        return be.getStoredItem().is(this.processItem);
    }

    @Override
    public void tickStop(CookBeBase<CuttingBoardBlockEntity> cookBeBase, MaidRecipesManager2<CuttingBoardRecipe> rm) {
        super.tickStop(cookBeBase, rm);
        this.processItem = null;
        this.maidHand = false;
    }

    @Override
    public AbstractCookRule<CuttingBoardBlockEntity, CuttingBoardRecipe> getOrCreate() {
        return new CuttingBoardCookRule();
    }

    public static CuttingBoardCookRule getInstance() {
        return INSTANCE;
    }
}
