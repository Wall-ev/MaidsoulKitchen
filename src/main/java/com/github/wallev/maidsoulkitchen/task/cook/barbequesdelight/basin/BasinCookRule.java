package com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.basin;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager2;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.TickCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidItem;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.mao.barbequesdelight.content.block.BasinBlockEntity;
import com.mao.barbequesdelight.content.recipe.SkeweringRecipe;
import com.mao.barbequesdelight.init.registrate.BBQDRecipes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BasinCookRule extends TickCookRule<BasinBlockEntity, SkeweringRecipe<?>> {
    private static final BasinCookRule INSTANCE = new BasinCookRule();

    public static BasinCookRule getInstance() {
        return INSTANCE;
    }

    private ItemStack container = ItemStack.EMPTY;
    private ItemStack tool = ItemStack.EMPTY;
    private ItemStack side = ItemStack.EMPTY;

    @Override
    public boolean canMoveTo(CookBeBase<BasinBlockEntity> cookBeBase, MaidRecipesManager2<SkeweringRecipe<?>> rm) {
//        BasinBlockEntity be = cookBeBase.getBe();
//        if(!be.items.isEmpty()) {
//            return true;
//        }

        if (rm.hasMaidRecs(cookBeBase)) {
            return true;
        }

        return false;
    }

    @Override
    public void cookMake(CookBeBase<BasinBlockEntity> cookBeBase, MaidRecipesManager2<SkeweringRecipe<?>> rm) {
        this.init(cookBeBase, rm);

        IItemHandlerModifiable inputInv = rm.getInputInv();
        BasinBlockEntity be = cookBeBase.getBe();
        MaidRec maidRec = rm.pollMaidRec(cookBeBase);
        Map<ItemDefinition, LinkedList<ItemStack>> invIngredients = rm.getInvIngredients();

        List<MaidItem> maidItems = maidRec.maidItems();

        MaidItem container = maidItems.get(1);
        ItemDefinition containerItem = container.item();
        int containerAmount = container.count();
        for (ItemStack itemStack : invIngredients.get(containerItem)) {
            if (itemStack.isEmpty()) continue;
            int count = itemStack.getCount();

            if (count >= containerAmount) {
                ItemStack copy = itemStack.copyWithCount(containerAmount);
                ItemStack leftInsertedStack = be.items.addItem(copy);
                itemStack.shrink(containerAmount - leftInsertedStack.getCount());
                break;
            } else {
                ItemStack copy = itemStack.copy();
                ItemStack leftInsertedStack = be.items.addItem(copy);
                itemStack.shrink(containerAmount - leftInsertedStack.getCount());
                containerAmount -= count;
                if (containerAmount <= 0) {
                    break;
                }
            }
        }
        this.container = be.items.getItem(0);

        ItemInventory itemInventory = rm.getItemInventory();
        MaidItem tool = maidItems.get(0);
        ItemStack toolItem = contItemStack(tool, itemInventory);
        this.swapItem(InteractionHand.MAIN_HAND, toolItem, maid, inputInv);
        this.tool = maid.getItemInHand(InteractionHand.MAIN_HAND);

        if (maidItems.size() > 2) {
            MaidItem side = maidItems.get(2);
            ItemStack sideItem = contItemStack(side, itemInventory);
            this.swapItem(InteractionHand.OFF_HAND, sideItem, maid, inputInv);
            this.side = maid.getItemInHand(InteractionHand.OFF_HAND);
        }

        rm.getItemInventory().markDirty();
    }

    @Override
    public void tickCookMake(CookBeBase<BasinBlockEntity> cookBeBase, MaidRecipesManager2<SkeweringRecipe<?>> rm) {
        if (tick++ % 5 != 0) {
            return;
        }

        Level worldIn = be.getLevel();
        IItemHandlerModifiable outputInv = rm.getOutputInv();

        var cont = new SimpleContainer(tool, container, side);
        var optional = worldIn.getRecipeManager().getRecipeFor(BBQDRecipes.RT_SKR.get(), cont, worldIn);
        if (optional.isEmpty()) {
            this.stop();
            return;
        }
        SkeweringRecipe<?> recipe = optional.get();
        ItemStack ret = recipe.assemble(cont, worldIn.registryAccess());
        ItemHandlerHelper.insertItemStacked(outputInv, ret, false);
        maid.swing(InteractionHand.MAIN_HAND);
    }

    @Override
    public void tickStop(CookBeBase<BasinBlockEntity> cookBeBase, MaidRecipesManager2<SkeweringRecipe<?>> rm) {
        super.tickStop(cookBeBase, rm);
        this.tool = ItemStack.EMPTY;
        this.container = ItemStack.EMPTY;
        this.side = ItemStack.EMPTY;
    }

    @Override
    public AbstractCookRule<BasinBlockEntity, SkeweringRecipe<?>> getOrCreate() {
        return new BasinCookRule();
    }
}
