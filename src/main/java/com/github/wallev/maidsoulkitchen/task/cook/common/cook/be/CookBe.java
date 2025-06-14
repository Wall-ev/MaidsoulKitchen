package com.github.wallev.maidsoulkitchen.task.cook.common.cook.be;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.ICookBeAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager2;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidItem;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.util.fakeplayer.WrappedMaidFakePlayer;
import com.github.wallev.verhelper.server.item.VItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CookBe<B extends BlockEntity> extends CookBeBase<B>{
    private final Builder<B> builder;

    public CookBe(EntityMaid maid, Builder<B> builder) {
        super(maid);
        this.builder = builder;
    }



    public boolean hasResult() {
        return !this.getResult().isEmpty();
    }

    public void extractResult(IItemHandler result2Inv) {
        takeItem(getResult(), result2Inv);
    }

    public boolean hasInputs() {
        IInvHandler ingredientInv = this.getIngredientInv();
        int start = builder.ingredientStart.get();
        int size = start + builder.ingredientSize.get();
        for (int i = start; i < size; i++) {
            if (!ingredientInv.kl$getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void takeInputs(IItemHandler inputs2Inv) {
        IInvHandler ingredientInv = this.getIngredientInv();
        int start = builder.ingredientStart.get();
        int size = start + builder.ingredientSize.get();
        for (int i = start; i < size; i++) {
            takeItem(ingredientInv.kl$getStackInSlot(i), inputs2Inv);
        }
    }
//
//    public boolean insertInputs(MaidRecipesManager2<?> rm) {
//        MaidRec rec = rm.getMaidRecs().poll();
//        if (rec == null) {
//            return false;
//        }
//        Map<Item, Queue<ItemStack>> invIngredients = rm.getInvIngredients();
//        return insertInputs(rec, invIngredients);
//    }

    public boolean insertInputs(MaidRec rec, Map<Item, LinkedList<ItemStack>> invIngredients) {
        IInvHandler ingredientInv = this.getIngredientInv();

        int index = 0;
        for (MaidItem maidItem : rec.maidItems()) {
            if(!maidItem.isEmpty()) {
                Item item = maidItem.item();
                int count = maidItem.count();
                insertAndShrink(ingredientInv, count, invIngredients.get(item), index++);
            }
        }
        return true;
    }

    public boolean hasMeal() {
        return !this.getMeal().isEmpty();
    }

    public boolean hasContainer() {
        return !this.getNowContainer().isEmpty();
    }

    public boolean takeContainer(IItemHandler item2Inv) {
        return takeItem(this.getNowContainer(), item2Inv);
    }

    public boolean insertContainer(ItemStack container) {
        return insertItem(container, this.getContainerInv(), this.getContainerSlot());
    }

    public boolean hasActiveItem() {
        return !this.activeItemStack().isEmpty();
    }

    public boolean takeActiveItem(IItemHandler item2Inv) {
        return takeItem(this.activeItemStack(), item2Inv);
    }

    public boolean insertActiveItem(ItemStack activeItem) {
        return insertItem(activeItem, this.activeItemInv(), this.activeItemSlot());
    }

    public boolean isCookBe(BlockEntity be) {
        return builder.isCookBe.test(be);
    }

    @Override
    public IInvHandler getInv() {
        return null;
    }

    public IInvHandler getIngredientInv() {
        return builder.ingredientInv.apply(be);
    }

    public int getIngredientSize() {
        return builder.ingredientSize.get();
    }

    public ItemStack getResult() {
        return builder.result.apply(be);
    }

    public int getResultSlot() {
        return builder.resultSlot.get();
    }


    public ItemStack getMeal() {
        return builder.meal.apply(be);
    }

    public ItemStack getNeedContainer() {
        return builder.needContainer.apply(be);
    }

    public ItemStack getNowContainer() {
        return builder.nowContainer.apply(be);
    }

    public int getContainerSlot() {
        return builder.containerSlot.get();
    }

    public IInvHandler getContainerInv() {
        return builder.containerInv.apply(be);
    }


    public int activeItemSlot() {
        return builder.activeItemSlot.get();
    }

    public ItemStack activeItemStack() {
        return builder.activeItemStack.apply(be);
    }

    public IInvHandler activeItemInv() {
        return builder.activeItemInv.apply(be);
    }


    public boolean recMatch() {
        return builder.recMatch.test(be);
    }

    public boolean cookStateMatch() {
        return builder.cookStateMatch.test(be);
    }

    public boolean canTakeResult() {
        return builder.canTakeResult.test(be);
    }

    public boolean hasEnoughCategory() {
        return builder.hasEnoughCategory.test(be);
    }

    public List<ItemStack> getCategoryItems() {
        return builder.getCategoryItems.apply(be);
    }

    @Override
    public void markChanged() {

    }


    public static <B extends BlockEntity> Builder<B> builder() {
        return new Builder<>();
    }

    public static class Builder<B extends BlockEntity> {
        private static final Builder<?> EMPTY = new Builder<>();

        protected Predicate<BlockEntity> isCookBe;

        protected Function<B, IInvHandler> beInv;

        protected Supplier<Integer> ingredientStart = () -> 0;
        protected Supplier<Integer> ingredientSize;
        protected Function<B, IInvHandler> ingredientInv = defaultInv();
        protected Supplier<Integer> resultSlot;
        protected Function<B, IInvHandler> resultInv = defaultInv();
        protected Function<B, ItemStack> result = defaultStack(resultInv, resultSlot);

        protected Function<B, ItemStack> meal;
        protected Function<B, ItemStack> needContainer;
        protected Supplier<Integer> containerSlot;
        protected Function<B, IInvHandler> containerInv = defaultInv();
        protected Function<B, ItemStack> nowContainer = defaultStack(containerInv, containerSlot);

        protected Supplier<Integer> activeItemSlot;
        protected Function<B, IInvHandler> activeItemInv = defaultInv();
        protected Function<B, ItemStack> activeItemStack = defaultStack(activeItemInv, activeItemSlot);


        protected Predicate<B> recMatch = (be) -> ((ICookBeAccessor)be).kl$canCook();
        protected Predicate<B> canTakeResult = (be) -> true;
        protected Predicate<B> cookStateMatch = (be) -> true;

        protected Predicate<B> hasEnoughCategory = (be) -> true;
        protected Function<B, List<ItemStack>> getCategoryItems = (be) -> Collections.emptyList();


        private Function<B, IInvHandler> defaultInv() {
            return (be) -> beInv.apply(be);
        }

        private Function<B, ItemStack> defaultStack(Function<B, IInvHandler> beInv, Supplier<Integer> slot) {
            return (be) -> beInv.apply(be).kl$getStackInSlot(slot.get());
        }


        public Builder<B> isCookBe(Predicate<BlockEntity> isCookBe) {
            this.isCookBe = isCookBe;
            return this;
        }

        public Builder<B> beInv(Function<B, IInvHandler> beInv) {
            this.beInv = beInv;
            return this;
        }


        public Builder<B> ingredientInv(Function<B, IInvHandler> ingredientInv) {
            this.ingredientInv = ingredientInv;
            return this;
        }

        public Builder<B> ingredientStart(Supplier<Integer> ingredientStart) {
            this.ingredientStart = ingredientStart;
            return this;
        }

        public Builder<B> ingredientSize(Supplier<Integer> ingredientSize) {
            this.ingredientSize = ingredientSize;
            return this;
        }

        public Builder<B> result(Function<B, ItemStack> result) {
            this.result = result;
            return this;
        }

        public Builder<B> resultSlot(Supplier<Integer> resultSlot) {
            this.resultSlot = resultSlot;
            return this;
        }

        public Builder<B> resultInv(Function<B, IInvHandler> resultInv) {
            this.resultInv = resultInv;
            return this;
        }


        public Builder<B> meal(Function<B, ItemStack> meal) {
            this.meal = meal;
            return this;
        }

        public Builder<B> needContainer(Function<B, ItemStack> needContainer) {
            this.needContainer = needContainer;
            return this;
        }

        public Builder<B> nowContainer(Function<B, ItemStack> nowContainer) {
            this.nowContainer = nowContainer;
            return this;
        }

        public Builder<B> containerInv(Function<B, IInvHandler> containerInv) {
            this.containerInv = containerInv;
            return this;
        }

        public Builder<B> containerSlot(Supplier<Integer> containerSlot) {
            this.containerSlot = containerSlot;
            return this;
        }


        public Builder<B> activeItemSlot(Supplier<Integer> activeItemSlot) {
            this.activeItemSlot = activeItemSlot;
            return this;
        }

        public Builder<B> activeItemStack(Function<B, ItemStack> activeItemStack) {
            this.activeItemStack = activeItemStack;
            return this;
        }

        public Builder<B> activeItemInv(Function<B, IInvHandler> activeItemInv) {
            this.activeItemInv = activeItemInv;
            return this;
        }

        public Builder<B> recMatch(Predicate<B> recMatch) {
            this.recMatch = recMatch;
            return this;
        }

        public Builder<B> canTakeResult(Predicate<B> canTakeResult) {
            this.canTakeResult = canTakeResult;
            return this;
        }

        public Builder<B> cookStateMatch(Predicate<B> cookStateMatch) {
            this.cookStateMatch = cookStateMatch;
            return this;
        }

        public Builder<B> hasEnoughCategory(Predicate<B> hasEnoughCategory) {
            this.hasEnoughCategory = hasEnoughCategory;
            return this;
        }

        public Builder<B> getCategoryItems(Function<B, List<ItemStack>> getCategoryItems) {
            this.getCategoryItems = getCategoryItems;
            return this;
        }

        public Builder<B> build() {
            return this;
        }

        @SuppressWarnings("unchecked")
        public static <B extends BlockEntity> Builder<B> empty() {
            return (Builder<B>) EMPTY;
        }


//        public Function<EntityMaid, CookBe<B>> build() {
//            return this::build;
//        }
//
//        public CookBe<B> build(EntityMaid maid) {
//            return new CookBe<>(maid, this);
//        }
    }
}
