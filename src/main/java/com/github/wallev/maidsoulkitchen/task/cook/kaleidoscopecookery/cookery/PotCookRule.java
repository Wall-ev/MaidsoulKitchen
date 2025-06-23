package com.github.wallev.maidsoulkitchen.task.cook.kaleidoscopecookery.cookery;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.maid.IMaidCookInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.TickCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.util.BubbleUtil;
import com.github.wallev.maidsoulkitchen.util.InvUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.block.entity.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.recipe.PotRecipe;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandlerModifiable;

public class PotCookRule extends TickCookRule<PotBlockEntity, PotRecipe> {
    public static final Item CONTAINER = Items.BOWL;
    public static final Item FLINT = Items.FLINT_AND_STEEL;
    private static final PotCookRule INSTANCE = new PotCookRule();

    private ItemStack kitchenShovel = ItemStack.EMPTY;
    private ItemStack bowl = ItemStack.EMPTY;
    private boolean needBowl = false;
    private int stirFrySpace = 0;
    private int stirFryMinCount = 0;
    private int time = 0;

    public static PotCookRule getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean canMoveTo(CookBeBase<PotBlockEntity> cookBeBase, MaidCookManager<PotRecipe> cm) {
        PotBe potBe = (PotBe) cookBeBase;
        PotBlockEntity potBlockEntity = potBe.getBe();

        IMaidCookInventory cookInv = cm.getCookInv();
        boolean hasInputAvailableSlot = cookInv.hasInputAvailableSlot();
        boolean hasOutputAvailableSlot = cookInv.hasOutputAvailableSlot();

        if (potBlockEntity.getStatus() == 2 && hasOutputAvailableSlot) {
            if (potBlockEntity.isNeedBowl()) {
                ItemStack container = cm.getItem(CONTAINER);
                return !container.isEmpty();
            } else {
                IItemHandlerModifiable inputInv = cm.getInputInv();
                ItemStack shovel = InvUtil.getStack(inputInv, ModItems.KITCHEN_SHOVEL.get());
                return !shovel.isEmpty();
            }
        }

        boolean hasMaidRecs = cm.hasMaidRecs(cookBeBase);
        if (hasMaidRecs) {
            boolean stateMatch = cookBeBase.cookStateMatch();
            if (stateMatch) {
                return true;
            }

            if (potBe.canFlitByItem()) {
                return cm.hasItem(FLINT);
            }
        }

        return false;
    }

    @Override
    public void cookMake(CookBeBase<PotBlockEntity> cookBeBase, MaidCookManager<PotRecipe> cm) {
        this.init(cookBeBase, cm);

        IItemHandlerModifiable inputInv = cm.getInputInv();
        PotBe potBe = (PotBe) cookBeBase;
        PotBlockEntity potBlockEntity = potBe.getBe();
        if (potBlockEntity.getStatus() == 2) {
            if (potBlockEntity.isNeedBowl()) {
                ItemStack container = cm.getItem(CONTAINER);
                cookBeBase.useItem(container, inputInv);
            } else {
                ItemStack shovel = InvUtil.getStack(inputInv, ModItems.KITCHEN_SHOVEL.get());
                if (shovel.isEmpty()) {
                    this.stop();
                    return;
                }
                this.swapItem(InteractionHand.MAIN_HAND, shovel, maid, inputInv);
                cookBeBase.useItemWithSneak(shovel, inputInv);
            }
        }

        boolean hasMaidRecs = cm.hasMaidRecs(cookBeBase);
        if (hasMaidRecs) {
            boolean canStart = cookBeBase.cookStateMatch();

            if (!canStart) {
                if (potBe.canFlitByItem()) {
                    ItemStack flint = cm.getItem(FLINT);
                    if (flint.isEmpty()) {
                        this.stop();
                        return;
                    }
                    InteractionResult result = player.useOnByItem(pos.below(), flint);
                    if (!result.consumesAction()) {
                        this.stop();
                        return;
                    }
                    canStart = true;
                }
            }

            if (!canStart) {
                this.stop();
                return;
            }


            EntityMaid maid = cm.getMaid();
            MaidRec maidRec = cm.pollMaidRec(cookBeBase);
            ItemInventory itemInventory = cm.getItemInventory();

            ItemStack oil = maidRec.oil();
            ItemStack oilItem = this.getItem(oil.getItem(), itemInventory);
            if (oilItem.isEmpty()) {
                this.stop();
                return;
            }
            potBe.useItem(oilItem, inputInv);

            potBe.insertInputs(maidRec, itemInventory);

            ItemStack toolItemStack = maidRec.tool();
            ItemStack swappedTool = this.swapTool(toolItemStack, itemInventory, maid, InteractionHand.MAIN_HAND, inputInv);
            if (swappedTool.isEmpty()) {
                this.stop();
                return;
            }
            this.kitchenShovel = swappedTool;

            PotRecipe potRecipe = maidRec.recCast();
            if (potRecipe.isNeedBowl()) {
                this.needBowl = true;
                ItemStack container = maidRec.container();
                this.bowl = this.getItem(container.getItem(), itemInventory);
            }

            this.stirFryMinCount = potRecipe.getStirFryCount();
            this.time = potRecipe.getTime();

            this.stirFrySpace = (time - 20) / stirFryMinCount;

            BubbleUtil.makeResultsBubble(maid, maidRec.result(), maidRec.time() + 20);
        } else {
            this.stop();
        }

    }

    @Override
    public void tickCookMake(CookBeBase<PotBlockEntity> cookBeBase, MaidCookManager<PotRecipe> cm) {
        IItemHandlerModifiable inputInv = cm.getInputInv();
        ItemInventory itemInventory = cm.getItemInventory();
        if (tick++ % stirFrySpace == 0) {
            if (maid.getMainHandItem() != kitchenShovel) {
                ItemStack swappedTool = this.swapTool(kitchenShovel, itemInventory, maid, InteractionHand.MAIN_HAND, inputInv);
                if (swappedTool.isEmpty()) {
                    this.stop();
                    return;
                }
                this.kitchenShovel = swappedTool;
//
//                this.swapItem(InteractionHand.MAIN_HAND, kitchenShovel, maid, inputInv);
            }

            cookBeBase.useItem(kitchenShovel, inputInv);
            return;
        }

        if (be.getStatus() == 2 || tick - 30 > time) {
            IItemHandlerModifiable outputInv = cm.getOutputInv();
            if (needBowl) {
                InteractionResult result = cookBeBase.useItem(bowl, outputInv);
                if (!result.consumesAction()) {
                    int a = 1;
                }
            } else {
                InteractionResult result = cookBeBase.useItemWithSneak(kitchenShovel, outputInv);
                if (!result.consumesAction()) {
                    int a = 1;
                }
            }
            this.stop();
        }

        if (tick % 5 == 0) {
            int nextInt = maid.getRandom().nextInt(1, 10);
            if (tick % nextInt == 0) {
                if (maid.getMainHandItem() != kitchenShovel) {
//                    this.swapItem(InteractionHand.MAIN_HAND, kitchenShovel, maid, inputInv);
                    ItemStack swappedTool = this.swapTool(kitchenShovel, itemInventory, maid, InteractionHand.MAIN_HAND, inputInv);
                    if (swappedTool.isEmpty()) {
                        this.stop();
                        return;
                    }
                    this.kitchenShovel = swappedTool;
                }

                cookBeBase.useItem(kitchenShovel, inputInv);
            }
        }
    }

    private void backpackTool(CookBeBase<PotBlockEntity> cookBeBase, MaidCookManager<PotRecipe> cm) {
        if (!kitchenShovel.is(ModItems.KITCHEN_SHOVEL.get())) {
            return;
        }

        this.swapTool(kitchenShovel, cm.getItemInventory(), cm.getMaid(), cm.getInputInv());
    }

    @Override
    public boolean tickCan(CookBeBase<PotBlockEntity> cookBeBase, MaidCookManager<PotRecipe> cm) {
        return super.tickCan(cookBeBase, cm);
    }

    @Override
    public void tickStop(CookBeBase<PotBlockEntity> cookBeBase, MaidCookManager<PotRecipe> cm) {
        this.backpackTool(cookBeBase, cm);
        super.tickStop(cookBeBase, cm);
        kitchenShovel = ItemStack.EMPTY;
        bowl = ItemStack.EMPTY;
        needBowl = false;
        stirFrySpace = 0;
        stirFryMinCount = 0;
        time = 0;
    }

    @Override
    protected TickCookRule<PotBlockEntity, PotRecipe> create() {
        return new PotCookRule();
    }
}
