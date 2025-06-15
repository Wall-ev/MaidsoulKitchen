package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager2;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.FluidRecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.util.ItemStackUtil;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class FluidPotCookRule2<B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookRule<B, R> {
    @SuppressWarnings("rawtypes")
    private static final FluidPotCookRule2 INSTANCE = new FluidPotCookRule2<>();

    @SuppressWarnings("unchecked")
    public static <B extends BlockEntity, R extends Recipe<? extends Container>> FluidPotCookRule2<B, R> getInstance() {
        return (FluidPotCookRule2<B, R>) INSTANCE;
    }

    protected boolean hasFluidContainers(Fluid fluid, MaidRecipesManager2<R> rm) {
        FluidRecSerializerManager<R> frm = rm.getRecSerializerManager().toFluid();
        List<ItemStack> containers = frm.fluidContainer(fluid);

        return rm.hasItemFromOutputAddition(itemStack -> ItemStackUtil.isItem(containers, itemStack));
    }

    protected ItemStack getFluidContainers(Fluid fluid, MaidRecipesManager2<R> rm) {
        FluidRecSerializerManager<R> frm = rm.getRecSerializerManager().toFluid();
        List<ItemStack> containers = frm.fluidContainer(fluid);

        return rm.getItemFromOutputAddition(itemStack -> ItemStackUtil.isItem(containers, itemStack));
    }

    public boolean canMoveTo(CookBeBase<B> cookBeBase, MaidRecipesManager2<R> rm) {
        boolean matchCookState = cookBeBase.cookStateMatch();
        boolean recMatch = cookBeBase.recMatch();

        boolean hasFluid = cookBeBase.hasFluid();
        // 有待取出成品(有条件取出)和对应的餐具
        if (!recMatch && hasFluid) {
            if (hasFluidContainers(cookBeBase.getFluid(), rm)) {
                return true;
            }
        }

        // 厨具满足烹饪的外部条件和有符合配方的原材料
        if (matchCookState && !recMatch) {
            boolean hasMaidRecs = rm.hasMaidRecs(cookBeBase);
            if (hasMaidRecs) {
                return true;
            }
        }

        boolean hasInputs = cookBeBase.hasInputs();
        // 配方不存在以及有残留的物品
        if (!recMatch && hasInputs) {
            return true;
        }

        return false;
    }

    public void cookMake(CookBeBase<B> cookBeBase, MaidRecipesManager2<R> rm) {
        IItemHandlerModifiable inputInv = rm.getInputInv();
        IItemHandlerModifiable outputInv = rm.getOutputInv();
        IItemHandlerModifiable outputAdditionInv = rm.getOutputAdditionInv();

        boolean matchCookState = cookBeBase.cookStateMatch();
        boolean recMatch = cookBeBase.recMatch();
        boolean hasInputs = cookBeBase.hasInputs();
        // 取出残存的原材料
        if (!recMatch && hasInputs) {
            cookBeBase.takeInputs(inputInv);
            cookBeBase.markChanged();
        }

        // 放入烹饪的原材料
        if (matchCookState && !recMatch && rm.hasMaidRecs(cookBeBase)) {
            ItemInventory itemInventory = rm.getItemInventory();
            MaidRec maidRec = rm.pollMaidRec(cookBeBase);
            cookBeBase.insertFluidItems(maidRec.fluidItem(), itemInventory);
            cookBeBase.insertInputs(maidRec, itemInventory);
            cookBeBase.markChanged();
            rm.getItemInventory().markDirty();;
            recMatch = true;
        }

        FluidStack fluidStack = cookBeBase.getFluidStack();
        if (!recMatch && !fluidStack.isEmpty()) {
            Fluid fluid = fluidStack.getFluid();
            ItemStack fluidContainer = getFluidContainers(fluid, rm);
            cookBeBase.useItem(fluidContainer, () -> {
                return !fluidStack.isEmpty();
            });
        }
    }

    @Override
    public FluidPotCookRule2<B, R> getOrCreate() {
        return this;
    }
}
