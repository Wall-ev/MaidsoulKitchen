package com.github.wallev.maidsoulkitchen.task.cook.cuisine.cuisine;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.mixin.cuisinedelight.CookingDataAccessor;
import com.github.wallev.maidsoulkitchen.mixin.cuisinedelight.CookingEntryAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager2;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.TickCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidItem;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.util.fakeplayer.WrappedMaidFakePlayer;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.content.logic.CookingData;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import dev.xkmc.cuisinedelight.content.logic.transform.Stage;
import dev.xkmc.cuisinedelight.content.recipe.BaseCuisineRecipe;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.util.*;

public class CuisineCookRule extends TickCookRule<CuisineSkilletBlockEntity, BaseCuisineRecipe<?>> {
    private static final CuisineCookRule INSTANCE = new CuisineCookRule();
    private int tickAll = 0;
    private int tickMax = 0;
    private int tickSpace = Integer.MAX_VALUE;
    private List<Pair<Integer, ItemStack>> processTickStacks = new ArrayList<>();
    private ItemStack plateItem = ItemStack.EMPTY;
    private boolean end = false;

    public static CuisineCookRule getInstance() {
        return INSTANCE;
    }

    private static int getReduction(ItemStack stack) {
        return stack.getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0 ? 20 : 0;
    }

    public static void playSound(EntityMaid maid, Level level, SoundEvent event) {
        Vec3 pos = maid.position();
        double x = pos.x() + 0.5;
        double y = pos.y();
        double z = pos.z() + 0.5;
        level.playLocalSound(x, y, z, event, SoundSource.BLOCKS, 0.4F, level.random.nextFloat() * 0.2F + 0.9F, false);
    }

    private static void processV1(Level worldIn, EntityMaid maid, CuisineSkilletBlockEntity cuisineSkilletBlockEntity) {
        if (!cuisineSkilletBlockEntity.cookingData.contents.isEmpty()) {
            if (!worldIn.isClientSide()) {
                cuisineSkilletBlockEntity.stir(worldIn.getGameTime(), getReduction(maid.getMainHandItem()));
                maid.swing(InteractionHand.MAIN_HAND);
            } else {
                playSound(maid, worldIn, ModSounds.BLOCK_SKILLET_SIZZLE.get());
                maid.swing(InteractionHand.MAIN_HAND);
            }
        }
    }

    @Override
    public boolean canMoveTo(CookBeBase<CuisineSkilletBlockEntity> cookBeBase, MaidRecipesManager2<BaseCuisineRecipe<?>> rm) {
        CuisineSkilletBlockEntity blockEntity = cookBeBase.getBe();
        CombinedInvWrapper maidAvailableInv = rm.getMaid().getAvailableInv(true);
        if (!blockEntity.isCooking() && blockEntity.canCook()
                && ItemsUtil.findStackSlot(maidAvailableInv, stack -> stack.is(CDItems.SPATULA.get())) > -1
                && ItemsUtil.findStackSlot(maidAvailableInv, stack -> stack.is(CDItems.PLATE.get())) > -1
                && rm.hasMaidRecs(cookBeBase)) {
            return true;
        }

        if (canExtractFood(cookBeBase, rm)) {
            return true;
        }

        return false;
    }

    @Override
    public void cookMake(CookBeBase<CuisineSkilletBlockEntity> cookBeBase, MaidRecipesManager2<BaseCuisineRecipe<?>> rm) {
        this.init(cookBeBase, rm);
        CuisineSkilletBlockEntity cuisineSkilletBlockEntity = cookBeBase.getBe();
        CombinedInvWrapper maidAvailableInv = maid.getAvailableInv(true);
        int plateSlot = ItemsUtil.findStackSlot(maidAvailableInv, itemStack -> itemStack.is(CDItems.PLATE.get()));
        if (plateSlot > -1) {
            plateItem = maidAvailableInv.getStackInSlot(plateSlot);
            if (!foodExistAndTake(cookBeBase, rm)) {
                this.tickStop(cookBeBase, rm);
                cuisineSkilletBlockEntity.sync();
                return;
            }
        }

        if (plateItem == null || plateItem.getCount() < 1 || !rm.hasMaidRecs(cookBeBase)) {
            this.tickStop(cookBeBase, rm);
            cuisineSkilletBlockEntity.sync();
            return;
        }

        ItemStack mainHandItem = maid.getMainHandItem();
        if (!mainHandItem.is(CDItems.SPATULA.get())) {
            int stackSlot = ItemsUtil.findStackSlot(maidAvailableInv, itemStack -> itemStack.is(CDItems.SPATULA.get()));
            if (stackSlot == -1) return;
            ItemStack leftStack = ItemHandlerHelper.insertItemStacked(maidAvailableInv, mainHandItem, false);
            if (!leftStack.isEmpty()) return;
            maid.setItemInHand(InteractionHand.MAIN_HAND, maidAvailableInv.getStackInSlot(stackSlot));
        }

        MaidRec maidRec = rm.pollMaidRec(cookBeBase);
        Map<Item, LinkedList<ItemStack>> invIngredients = rm.getInvIngredients();
        if (maidRec == null) {
            this.tickStop(cookBeBase, rm);
            cuisineSkilletBlockEntity.sync();
            return;
        }

        for (MaidItem maidItem : maidRec.maidItems()) {
            ItemStack itemStack = contItemStack(maidItem, invIngredients);
            if (!itemStack.isEmpty()) {
                IngredientConfig.IngredientEntry entry = IngredientConfig.get().getEntry(itemStack);
                if (entry != null) {
                    tickMax = Math.max(tickMax, entry.min_time);
                    tickSpace = Math.min(tickSpace, entry.stir_time);
                    processTickStacks.add(Pair.of(entry.min_time, itemStack));
                }
            }
        }
        processTickStacks.sort((a, b) -> a.getFirst() == 0 ? -1 : Integer.compare(b.getFirst(), a.getFirst()));

        {
            WrappedMaidFakePlayer fakePlayer = WrappedMaidFakePlayer.get(maid);
            Integer time = processTickStacks.get(0).getFirst();
            List<Pair<Integer, ItemStack>> list = processTickStacks.stream().filter(pair -> pair.getFirst() == time || pair.getFirst() == tickMax).toList();
            processTickStacks.removeAll(list);
            BlockPos blockPos = cuisineSkilletBlockEntity.getBlockPos();
            for (Pair<Integer, ItemStack> integerItemStackPair : list) {
                fakePlayer.useOnByItem(blockPos, integerItemStackPair.getSecond().split(1));
                fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, integerItemStackPair.getSecond().split(1));
            }
        }
    }

    @Override
    public boolean tickCan(CookBeBase<CuisineSkilletBlockEntity> cookBeBase, MaidRecipesManager2<BaseCuisineRecipe<?>> rm) {
        return super.tickCan(cookBeBase, rm) && !end;
    }

    @Override
    public void tickCookMake(CookBeBase<CuisineSkilletBlockEntity> cookBeBase, MaidRecipesManager2<BaseCuisineRecipe<?>> rm) {
        tickAll++;

        CuisineSkilletBlockEntity cuisineSkilletBlockEntity = cookBeBase.getBe();
        Level worldIn = cuisineSkilletBlockEntity.getLevel();
        if ((tickAll + 10) % tickSpace == 0) {
            processV1(worldIn, maid, cuisineSkilletBlockEntity);
        }

        if (!processTickStacks.isEmpty()) {
            Pair<Integer, ItemStack> pair = processTickStacks.get(0);
            if (tickAll + 10 == tickMax - pair.getFirst()) {
                WrappedMaidFakePlayer fakePlayer = WrappedMaidFakePlayer.get(maid);
                Integer time = processTickStacks.get(0).getFirst();
                List<Pair<Integer, ItemStack>> list = processTickStacks.stream().filter(pair0 -> pair0.getFirst() == time || pair0.getFirst() == tickMax).toList();
                processTickStacks.removeAll(list);
                BlockPos blockPos = cuisineSkilletBlockEntity.getBlockPos();
                for (Pair<Integer, ItemStack> integerItemStackPair : list) {
                    fakePlayer.useOnByItem(blockPos, integerItemStackPair.getSecond().split(1));
                    fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, integerItemStackPair.getSecond().split(1));
                }
            }
        }


        if (tickAll - 10 >= tickMax) {
            CookingData data = cuisineSkilletBlockEntity.cookingData;
            data.stir(worldIn.getGameTime(), 0);
            CookedFoodData food = new CookedFoodData(data);
            ItemStack foodStack = BaseCuisineRecipe.findBestMatch(worldIn, food);
            plateItem.shrink(1);
            ItemHandlerHelper.insertItemStacked(rm.getOutputInv(), foodStack, false);

            cuisineSkilletBlockEntity.cookingData = new CookingData();
            cuisineSkilletBlockEntity.sync();

            maid.swing(InteractionHand.MAIN_HAND);

            this.end = true;
        }


    }

    @Override
    public void tickStop(CookBeBase<CuisineSkilletBlockEntity> cookBeBase, MaidRecipesManager2<BaseCuisineRecipe<?>> rm) {
        super.tickStop(cookBeBase, rm);
        this.tickAll = 0;
        this.tickMax = 0;
        this.tickSpace = Integer.MAX_VALUE;
        this.processTickStacks.clear();
        this.plateItem = ItemStack.EMPTY;
        this.end = false;
    }

    private boolean foodExistAndTake(CookBeBase<CuisineSkilletBlockEntity> cookBeBase, MaidRecipesManager2<BaseCuisineRecipe<?>> rm) {
        CuisineSkilletBlockEntity cuisineSkilletBlockEntity = cookBeBase.getBe();
        CookingData cookingData = cuisineSkilletBlockEntity.cookingData;
        List<CookingData.CookingEntry> contents = cookingData.contents;
        if (!contents.isEmpty()) {
            for (CookingData.CookingEntry entry : contents) {
                ItemStack food = entry.getItem();
                IngredientConfig.IngredientEntry config = IngredientConfig.get().getEntry(food);
                if (config != null) {
                    float cook_needle = Mth.clamp(getDuration(cookingData, entry, maid) / 400.0F, 0.0F, 1.0F);
                    if (cook_needle < 1) {
                        return false;
                    }
                }
            }

            CookedFoodData food = new CookedFoodData(cookingData);
            ItemStack foodStack = BaseCuisineRecipe.findBestMatch(cuisineSkilletBlockEntity.getLevel(), food);
            plateItem.shrink(1);
            ItemHandlerHelper.insertItemStacked(rm.getOutputInv(), foodStack, false);

            cuisineSkilletBlockEntity.cookingData = new CookingData();
            cuisineSkilletBlockEntity.sync();

            maid.swing(InteractionHand.MAIN_HAND);

            return true;
        }

        return true;
    }

    public boolean canExtractFood(CookBeBase<CuisineSkilletBlockEntity> cookBeBase, MaidRecipesManager2<BaseCuisineRecipe<?>> rm) {
        CuisineSkilletBlockEntity blockEntity = cookBeBase.getBe();
        EntityMaid maid = rm.getMaid();
        CombinedInvWrapper maidAvailableInv = maid.getAvailableInv(true);
        CookingData cookingData = blockEntity.cookingData;
        List<CookingData.CookingEntry> contents = cookingData.contents;
        if (!contents.isEmpty() && ItemsUtil.findStackSlot(maidAvailableInv, stack -> stack.is(CDItems.PLATE.get())) > -1) {

            for (CookingData.CookingEntry content : contents) {
                Stage stage = content.getStage(cookingData);
                if (stage == Stage.COOKED) {

                }
            }

            boolean isCook = false;
            for (CookingData.CookingEntry entry : contents) {
                ItemStack food = entry.getItem();
                IngredientConfig.IngredientEntry config = IngredientConfig.get().getEntry(food);
                if (config != null) {
                    float cook_needle = Mth.clamp(this.getDuration(cookingData, entry, maid) / 400.0F, 0.0F, 1.0F);
                    if (cook_needle < 1) {
                        isCook = true;
                        break;
                    }
                }
            }

            return !isCook;
        }

        return false;
    }

    public float getDuration(CookingData data, CookingData.CookingEntry cookingEntry, EntityMaid maid) {
        return (maid.level.getGameTime() - ((CookingEntryAccessor) cookingEntry).tlmk$getStartTime()) * ((CookingDataAccessor) data).tlmk$getSpeed();
    }

    @Override
    public AbstractCookRule<CuisineSkilletBlockEntity, BaseCuisineRecipe<?>> getOrCreate() {
        return new CuisineCookRule();
    }
}
