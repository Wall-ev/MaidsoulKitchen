package com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.grill;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager2;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.TickCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidItem;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.mao.barbequesdelight.content.block.GrillBlockEntity;
import com.mao.barbequesdelight.content.recipe.GrillingRecipe;
import com.mao.barbequesdelight.init.registrate.BBQDItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class GrillCookRule extends TickCookRule<GrillBlockEntity, GrillingRecipe<?>> {
    private static final GrillCookRule INSTANCE = new GrillCookRule();
    private ItemStack grillStack = ItemStack.EMPTY;

    public static GrillCookRule getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean canMoveTo(CookBeBase<GrillBlockEntity> cookBeBase, MaidRecipesManager2<GrillingRecipe<?>> rm) {
        boolean innerCanCook = false;
        GrillBlockEntity blockEntity = cookBeBase.getBe();

        // 取出烤焦的食物
        GrillBlockEntity.ItemEntry[] itemEntries = blockEntity.entries;
        for (GrillBlockEntity.ItemEntry itemEntry : itemEntries) {
            if (itemEntry.stack.is(BBQDItems.BURNT_FOOD.asItem())) {
                return true;
            } else if (!itemEntry.stack.isEmpty()) {

                // 要翻转了
                if (itemEntry.canFlip()) {
                    return true;
                }

                if (itemEntry.flipped && itemEntry.time >= itemEntry.duration) {
                    return true;
                }

                innerCanCook = true;
            }
        }

        // 烧烤架没有在烤东西，并且女仆身上有待烧烤的食物
        if (blockEntity.isHeated() && !innerCanCook && rm.hasMaidRecs(cookBeBase)) {
            return true;
        }

        return false;
    }

    @Override
    public void cookMake(CookBeBase<GrillBlockEntity> cookBeBase, MaidRecipesManager2<GrillingRecipe<?>> rm) {
        this.init(cookBeBase, rm);
        if (rm.hasMaidRecs(cookBeBase)) {
            Map<Item, LinkedList<ItemStack>> invIngredients = rm.getInvIngredients();
            MaidRec maidRec = rm.pollMaidRec(cookBeBase);
            MaidItem maidItem = maidRec.maidItems().get(0);
            this.grillStack = contItemStack(maidItem, invIngredients);
        }
    }

    @Override
    public void tickCookMake(CookBeBase<GrillBlockEntity> cookBeBase, MaidRecipesManager2<GrillingRecipe<?>> rm) {
        IItemHandlerModifiable outputInv = rm.getOutputInv();

        boolean nothing = true;
        GrillBlockEntity.ItemEntry[] itemEntries = be.entries;
        for (GrillBlockEntity.ItemEntry itemEntry : itemEntries) {
            ItemStack stack = itemEntry.stack;
            if (stack.is(BBQDItems.BURNT_FOOD.asItem())) {
                ItemStack leftStack = ItemHandlerHelper.insertItemStacked(outputInv, stack.copy(), false);
                stack.shrink(stack.getCount() - leftStack.getCount());
                be.inventoryChanged();

                nothing = false;
            } else if (!stack.isEmpty()) {
                // 要翻转了
                if (itemEntry.canFlip()) {
                    itemEntry.flip(be);
                    maid.swing(InteractionHand.MAIN_HAND);

                }

                // 熟了，可以取出来了
                if (itemEntry.flipped && itemEntry.time >= itemEntry.duration) {
                    ItemStack leftStack = ItemHandlerHelper.insertItemStacked(outputInv, stack.copy(), false);
                    stack.shrink(stack.getCount() - leftStack.getCount());
                    be.inventoryChanged();
                }

                nothing = false;
            } else {
                if (!grillStack.isEmpty()) {
                    if (!grillStack.isEmpty() && itemEntry.addItem(be, grillStack.copyWithCount(1))) {
                        maid.swing(InteractionHand.MAIN_HAND);
                        be.inventoryChanged();
                        grillStack.shrink(1);
                        nothing = false;
                    }

                }

            }
        }

        if (nothing) {
            this.stop();
            return;
        }

    }

    @Override
    public boolean tickCan(CookBeBase<GrillBlockEntity> cookBeBase, MaidRecipesManager2<GrillingRecipe<?>> rm) {
        return super.tickCan(cookBeBase, rm);
    }

    @Override
    public void tickStop(CookBeBase<GrillBlockEntity> cookBeBase, MaidRecipesManager2<GrillingRecipe<?>> rm) {
        super.tickStop(cookBeBase, rm);
        grillStack = ItemStack.EMPTY;
    }

    @Override
    public AbstractCookRule<GrillBlockEntity, GrillingRecipe<?>> getOrCreate() {
        return new GrillCookRule();
    }
}
