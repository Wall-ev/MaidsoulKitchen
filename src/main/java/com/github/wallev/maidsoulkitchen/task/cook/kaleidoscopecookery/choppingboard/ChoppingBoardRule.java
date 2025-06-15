package com.github.wallev.maidsoulkitchen.task.cook.kaleidoscopecookery.choppingboard;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager2;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.TickCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.ysbbbbbb.kaleidoscopecookery.block.entity.ChoppingBoardBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.tag.TagItem;
import com.github.ysbbbbbb.kaleidoscopecookery.recipe.ChoppingBoardRecipe;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class ChoppingBoardRule extends TickCookRule<ChoppingBoardBlockEntity, ChoppingBoardRecipe> {
    private static final ChoppingBoardRule INSTANCE = new ChoppingBoardRule();

    private boolean maidHand = false;
    private Item processItem = null;

    public static ChoppingBoardRule getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean canMoveTo(CookBeBase<ChoppingBoardBlockEntity> cookBeBase, MaidRecipesManager2<ChoppingBoardRecipe> rm) {
        ChoppingBoardBlockEntity cuttingBoard = cookBeBase.getBe();

        if (!cuttingBoard.getCurrentCutStack().isEmpty() && this.hasBoardStackTool(rm.getMaid(), cuttingBoard)) {
            return true;
        }

        if (cuttingBoard.getCurrentCutStack().isEmpty() && rm.hasMaidRecs(cookBeBase)) {
            return true;
        }
        return false;
    }

    @Override
    public void cookMake(CookBeBase<ChoppingBoardBlockEntity> cookBeBase, MaidRecipesManager2<ChoppingBoardRecipe> rm) {
        this.init(cookBeBase, rm);
        ItemStack storedItem = be.getCurrentCutStack();
        if (!storedItem.isEmpty()) {
            ItemStack tool = getBoardStackTool(maid, be);
            if (tool.isEmpty()) {
                this.tickStop(cookBeBase, rm);
                return;
            }

            this.swapItem(InteractionHand.MAIN_HAND, tool, maid, maid.getAvailableInv(true));
            player.useByHand(InteractionHand.MAIN_HAND, pos);

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
    public boolean tickCan(CookBeBase<ChoppingBoardBlockEntity> cookBeBase, MaidRecipesManager2<ChoppingBoardRecipe> rm) {
        return super.tickCan(cookBeBase, rm) && !maid.getMainHandItem().isEmpty() && this.processItem != null &&
                (maid.getOffhandItem().is(this.processItem) || isProcessItem());
    }

    @Override
    public void tickCookMake(CookBeBase<ChoppingBoardBlockEntity> cookBeBase, MaidRecipesManager2<ChoppingBoardRecipe> rm) {
        if (tick++ % 5 != 0) {
            return;
        }

        if (!be.getCurrentCutStack().isEmpty() || be.getCurrentCutCount() < be.getMaxCutCount()) {
            player.useByHand(InteractionHand.MAIN_HAND, pos);
        } else {
            ItemStack offhandItem = maid.getOffhandItem();
            if (offhandItem.is(this.processItem)) {
                player.useByHand(InteractionHand.OFF_HAND, pos);
            }
        }

//        if (maidHand) {
//            player.useByHand(InteractionHand.MAIN_HAND, pos);
//        } else {
//            ItemStack offhandItem = maid.getOffhandItem();
//            if (offhandItem.is(this.processItem)) {
//                player.useByHand(InteractionHand.OFF_HAND, pos);
//            }
//        }
//        maidHand = !maidHand;
    }

    private boolean hasBoardStackTool(EntityMaid maid, ChoppingBoardBlockEntity blockEntity) {
        return !this.getBoardStackTool(maid, blockEntity).isEmpty();
    }

    private ItemStack getBoardStackTool(EntityMaid maid, ChoppingBoardBlockEntity blockEntity) {
        Level level = maid.level;
        CombinedInvWrapper maidInv = maid.getAvailableInv(true);

        Ingredient tool = Ingredient.of(TagItem.KITCHEN_KNIFE);
        return ItemsUtil.getStack(maidInv, (itemStack) -> {
            return tool.test(itemStack);
        });
    }

    private boolean isProcessItem() {
        return be.getCurrentCutStack().is(this.processItem);
    }

    @Override
    public void tickStop(CookBeBase<ChoppingBoardBlockEntity> cookBeBase, MaidRecipesManager2<ChoppingBoardRecipe> rm) {
        super.tickStop(cookBeBase, rm);
        this.processItem = null;
        this.maidHand = false;
    }

    @Override
    public AbstractCookRule<ChoppingBoardBlockEntity, ChoppingBoardRecipe> getOrCreate() {
        return new ChoppingBoardRule();
    }
}
