package com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.grill;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid.IMaidCookInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.TickCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidItem;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.mao.barbequesdelight.content.block.GrillBlockEntity;
import com.mao.barbequesdelight.content.recipe.GrillingRecipe;
import com.mao.barbequesdelight.init.registrate.BBQDItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

public class GrillCookRule extends TickCookRule<GrillBlockEntity, GrillingRecipe<?>> {
    private static final GrillCookRule INSTANCE = new GrillCookRule();
    private ItemStack grillStack = ItemStack.EMPTY;

    public static GrillCookRule getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean canMoveTo(CookBeBase<GrillBlockEntity> cookBeBase, MaidCookManager<GrillingRecipe<?>> cm) {
        boolean innerCanCook = false;
        GrillBlockEntity blockEntity = cookBeBase.getBe();

        IMaidCookInventory cookInv = cm.getCookInv();
        boolean hasInputAvailableSlot = cookInv.hasInputAvailableSlot();
        boolean hasOutputAvailableSlot = cookInv.hasOutputAvailableSlot();

        if (hasOutputAvailableSlot) {
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
        }

        // 烧烤架没有在烤东西，并且女仆身上有待烧烤的食物
        if (blockEntity.isHeated() && !innerCanCook && cm.hasMaidRecs(cookBeBase)) {
            return true;
        }

        return false;
    }

    @Override
    public void cookMake(CookBeBase<GrillBlockEntity> cookBeBase, MaidCookManager<GrillingRecipe<?>> cm) {
        this.init(cookBeBase, cm);
        if (cm.hasMaidRecs(cookBeBase)) {
            ItemInventory itemInventory = cm.getItemInventory();
            MaidRec maidRec = cm.pollMaidRec(cookBeBase);
            MaidItem maidItem = maidRec.maidItems().get(0);
            this.grillStack = contItemStack(maidItem, itemInventory);
        }
    }

    @Override
    public void tickCookMake(CookBeBase<GrillBlockEntity> cookBeBase, MaidCookManager<GrillingRecipe<?>> cm) {
        IItemHandlerModifiable outputInv = cm.getOutputInv();

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
    public boolean tickCan(CookBeBase<GrillBlockEntity> cookBeBase, MaidCookManager<GrillingRecipe<?>> cm) {
        return super.tickCan(cookBeBase, cm);
    }

    @Override
    public void tickStop(CookBeBase<GrillBlockEntity> cookBeBase, MaidCookManager<GrillingRecipe<?>> cm) {
        super.tickStop(cookBeBase, cm);
        grillStack = ItemStack.EMPTY;
    }

    @Override
    protected TickCookRule<GrillBlockEntity, GrillingRecipe<?>> create() {
        return new GrillCookRule();
    }
}
