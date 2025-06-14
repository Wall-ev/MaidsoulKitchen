package com.github.wallev.maidsoulkitchen.task.cook.kaleidoscopecookery.cookery;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager2;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.TickCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidItem;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.util.InvUtil;
import com.github.wallev.maidsoulkitchen.util.MaidUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.block.entity.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.recipe.PotRecipe;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.LinkedList;
import java.util.Map;

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
    public boolean canMoveTo(CookBeBase<PotBlockEntity> cookBeBase, MaidRecipesManager2<PotRecipe> rm) {
        PotBe potBe = (PotBe) cookBeBase;
        PotBlockEntity potBlockEntity = potBe.getBe();

        if (potBlockEntity.getStatus() == 2) {
            if (potBlockEntity.isNeedBowl()) {
                ItemStack container = rm.getItem(BagType.OUTPUT_ADDITION, CONTAINER);
                cookBeBase.useItem(container);
            } else {
                IItemHandlerModifiable inputInv = rm.getInputInv();
                ItemStack shovel = InvUtil.getStack(inputInv, ModItems.KITCHEN_SHOVEL.get());
                return !shovel.isEmpty();
            }
        }

        boolean hasMaidRecs = rm.hasMaidRecs(cookBeBase);
        if (hasMaidRecs) {
            boolean stateMatch = cookBeBase.cookStateMatch();
            if (stateMatch) {
                return true;
            }

            if (potBe.canFlitByItem()) {
                return rm.hasItem(BagType.START_ADDITION, FLINT);
            }
        }

        return false;
    }

    @Override
    public void cookMake(CookBeBase<PotBlockEntity> cookBeBase, MaidRecipesManager2<PotRecipe> rm) {
        this.init(cookBeBase, rm);

        PotBe potBe = (PotBe) cookBeBase;
        PotBlockEntity potBlockEntity = potBe.getBe();
        if (potBlockEntity.getStatus() == 2) {
            if (potBlockEntity.isNeedBowl()) {
                ItemStack container = rm.getItem(BagType.OUTPUT_ADDITION, CONTAINER);
                cookBeBase.useItem(container);
            } else {
                IItemHandlerModifiable inputInv = rm.getInputInv();
                ItemStack shovel = InvUtil.getStack(inputInv, ModItems.KITCHEN_SHOVEL.get());
                if (shovel.isEmpty()) {
                    this.stop();
                    return;
                }
                this.swapItem(InteractionHand.MAIN_HAND, shovel, maid, inputInv);
                player.useOnByItem(pos, maid.getMainHandItem(), true);
            }
        }

        boolean hasMaidRecs = rm.hasMaidRecs(cookBeBase);
        if (hasMaidRecs) {
            boolean canStart = cookBeBase.cookStateMatch();

            if (!canStart) {
                if (potBe.canFlitByItem()) {
                    ItemStack flint = rm.getItem(BagType.START_ADDITION, FLINT);
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


            EntityMaid maid = rm.getMaid();
            IItemHandlerModifiable inputInv = rm.getInputInv();
            MaidRec maidRec = rm.pollMaidRec(cookBeBase);
            Map<Item, LinkedList<ItemStack>> invIngredients = rm.getInvIngredients();

            ItemStack oil = maidRec.oil();
            ItemStack oilItem = this.contItemStack(oil, invIngredients);
            if (oilItem.isEmpty()) {
                this.stop();
                return;
            }
            potBe.useItem(oilItem);

//            potBe.insertInputs(maidRec, invIngredients);

            int index = 0;
            for (MaidItem maidItem : maidRec.maidItems()) {
                if (!maidItem.isEmpty()) {
                    Item item = maidItem.item();
                    int amount = 1;
                    cookBeBase.insertAndShrink(cookBeBase.getInv(), amount, invIngredients.get(item), index++);
                    MaidUtil.pickupAction(maid);
                }
            }

            ItemStack tool = this.getItem(maidRec.tool(), invIngredients);
            ItemStack swapTool = this.swapItem(InteractionHand.MAIN_HAND, tool, maid, inputInv);
            this.kitchenShovel = swapTool;

            PotRecipe potRecipe = maidRec.recCast();
            if (potRecipe.isNeedBowl()) {
                this.needBowl = true;
                this.bowl = this.contItemStack(maidRec.container(), invIngredients);
            }

            this.stirFryMinCount = potRecipe.getStirFryCount();
            this.time = potRecipe.getTime();

            this.stirFrySpace = (time - 20) / stirFryMinCount;
        } else {
            this.stop();
        }

    }

    @Override
    public void tickCookMake(CookBeBase<PotBlockEntity> cookBeBase, MaidRecipesManager2<PotRecipe> rm) {
        IItemHandlerModifiable inputInv = rm.getInputInv();
        if (tick++ % stirFrySpace == 0) {
            if (maid.getMainHandItem() != kitchenShovel) {
                this.swapItem(InteractionHand.MAIN_HAND, kitchenShovel, maid, inputInv);
            }

            player.useOnByHand(pos);
            return;
        }

        if (be.getStatus() == 2) {
            ItemStack handItem = ItemStack.EMPTY;
            if (needBowl) {
                handItem = this.swapItem(InteractionHand.MAIN_HAND, bowl, maid, inputInv);
                player.useOnByItem(pos, handItem);
            } else {
                player.useOnByItem(pos, maid.getMainHandItem(), true);
            }
            this.stop();
        }

        if (tick % 5 == 0) {
            int nextInt = maid.getRandom().nextInt(1, 10);
            if (tick % nextInt == 0) {
                if (maid.getMainHandItem() != kitchenShovel) {
                    this.swapItem(InteractionHand.MAIN_HAND, kitchenShovel, maid, inputInv);
                }

                player.useOnByHand(pos);
            }
        }
    }

    @Override
    public boolean tickCan(CookBeBase<PotBlockEntity> cookBeBase, MaidRecipesManager2<PotRecipe> rm) {
        return super.tickCan(cookBeBase, rm);
    }

    @Override
    public void tickStop(CookBeBase<PotBlockEntity> cookBeBase, MaidRecipesManager2<PotRecipe> rm) {
        super.tickStop(cookBeBase, rm);
        kitchenShovel = ItemStack.EMPTY;
        bowl = ItemStack.EMPTY;
        stirFrySpace = 0;
        stirFryMinCount = 0;
        time = 0;
    }

    @Override
    public AbstractCookRule<PotBlockEntity, PotRecipe> getOrCreate() {
        return new PotCookRule();
    }
}
