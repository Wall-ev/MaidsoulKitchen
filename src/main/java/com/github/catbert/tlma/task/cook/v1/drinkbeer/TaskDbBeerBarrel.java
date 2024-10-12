package com.github.catbert.tlma.task.cook.v1.drinkbeer;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.entity.data.inner.task.CookData;
import com.github.catbert.tlma.init.registry.tlm.RegisterData;
import com.github.catbert.tlma.mixin.drinkbeer.BeerBarrelBlockAccessor;
import com.github.catbert.tlma.task.cook.handler.v2.MaidRecipesManager;
import com.github.catbert.tlma.task.cook.v1.common.TaskBaseContainerCook;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import lekavar.lma.drinkbeer.registries.BlockRegistry;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.*;


public class TaskDbBeerBarrel extends TaskBaseContainerCook<BeerBarrelBlockEntity, BrewingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(TLMAddon.MOD_ID, "drinkbeer_beerbarrel");

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof BeerBarrelBlockEntity;
    }

    @Override
    public RecipeType<BrewingRecipe> getRecipeType() {
        return RecipeRegistry.RECIPE_TYPE_BREWING.get();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return BlockRegistry.BEER_BARREL.get().asItem().getDefaultInstance();
    }

    @Override
    public boolean isHeated(BeerBarrelBlockEntity be) {
        return true;
    }

    @Override
    public boolean beInnerCanCook(Container inventory, BeerBarrelBlockEntity be) {
        BeerBarrelBlockAccessor be1 = (BeerBarrelBlockAccessor) be;
//        BrewingRecipe recipe = be.getLevel().getRecipeManager().getRecipeFor(RecipeRegistry.RECIPE_TYPE_BREWING.get(), be.getBrewingInventory(), be.getLevel()).orElse(null);
//        return be1.canBrew$tlma(recipe) && be1.hasEnoughEmptyCap$tlma(recipe);
//        return be1.statusCode$tlma() == 0 && be1.canBrew$tlma(recipe) && be1.hasEnoughEmptyCap$tlma(recipe);

        return be1.statusCode$tlma() == 1;
    }

    @Override
    public int getOutputSlot() {
        return 5;
    }

    @Override
    public int getInputSize() {
        return 5;
    }

    @Override
    public Container getContainer(BeerBarrelBlockEntity be) {
        return be.getBrewingInventory();
    }

    @Override
    public MaidRecipesManager<BrewingRecipe> getRecipesManager(EntityMaid maid) {
        return new MaidRecipesManager<>(maid, this, false){
            @Override
            protected Pair<List<Integer>, List<Item>> getAmountIngredient(BrewingRecipe recipe, Map<Item, Integer> available) {
                List<Ingredient> ingredients = recipe.getIngredients();
                boolean[] canMake = {true};
                boolean[] single = {false};
                List<Item> invIngredient = new ArrayList<>();
                Map<Item, Integer> itemTimes = new HashMap<>();

                for (Ingredient ingredient : ingredients) {
                    boolean hasIngredient = false;
                    for (Item item : available.keySet()) {
                        ItemStack stack = item.getDefaultInstance();
                        if (ingredient.test(stack)) {
                            invIngredient.add(item);
                            hasIngredient = true;

                            if (stack.getMaxStackSize() == 1) {
                                single[0] = true;
                                itemTimes.put(item, 1);
                            } else {
                                itemTimes.merge(item, 1, Integer::sum);
                            }

                            break;
                        }
                    }

                    if (!hasIngredient) {
                        canMake[0] = false;
                        itemTimes.clear();
                        invIngredient.clear();
                        break;
                    }
                }

                ItemStack beerCup = recipe.getBeerCup();
                {
                    boolean hasIngredient = false;
                    for (Item item : available.keySet()) {
                        ItemStack stack = item.getDefaultInstance();
                        if (beerCup.is(stack.getItem()) && available.getOrDefault(item, 0) >= beerCup.getCount()) {
                            invIngredient.add(item);
                            hasIngredient = true;

                            if (stack.getMaxStackSize() == 1) {
                                single[0] = true;
                                itemTimes.put(item, 1);
                            } else {
                                itemTimes.merge(item, beerCup.getCount(), Integer::sum);
                            }

                            break;
                        }
                    }

                    if (!hasIngredient) {
                        canMake[0] = false;
                        itemTimes.clear();
                        invIngredient.clear();
                    }
                }


                if (!canMake[0] || invIngredient.stream().anyMatch(item -> available.get(item) <= 0)) {
                    return Pair.of(Collections.emptyList(), Collections.emptyList());
                }

                int maxCount = 64;
                if (single[0] || this.isSingle()) {
                    maxCount = 1;
                } else {
                    for (Item item : itemTimes.keySet()) {
                        maxCount = Math.min(maxCount, item.getDefaultInstance().getMaxStackSize());
                        maxCount = Math.min(maxCount, available.get(item) / itemTimes.get(item));
                    }
                }

                List<Integer> countList = new ArrayList<>();
                for (int i = 0; i < invIngredient.size() - 1; i++) {
                    countList.add(maxCount);
                    Item item = invIngredient.get(i);
                    available.put(item, available.get(item) - maxCount);
                }
                {
                    countList.add(beerCup.getCount());
                    Item item = invIngredient.get(invIngredient.size() - 1);
                    available.put(item, available.get(item) - maxCount);
                }

                return Pair.of(countList, invIngredient);
            }

            @Override
            protected List<Pair<List<Integer>, List<List<ItemStack>>>> transform(EntityMaid maid, List<Pair<List<Integer>, List<Item>>> oriList, Map<Item, Integer> available ) {
//                repeat(oriList, available);
                return super.transform(maid, oriList, available);
            }
        };
    }

    // statusCode$tlma:
    // 0 - waiting for ingredient,
    // 1 - brewing,
    // 2 - waiting for pickup product
    @Override
    public boolean maidShouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, BeerBarrelBlockEntity blockEntity, MaidRecipesManager<BrewingRecipe> maidRecipesManager) {
        Container inventory = getContainer(blockEntity);
        if (canTakeOutput(inventory, blockEntity)) {
            return true;
        }

        // 啤酒桶现在在酿酒吗
        boolean b = ((BeerBarrelBlockAccessor)blockEntity).statusCode$tlma() == 1;
        List<Pair<List<Integer>, List<List<ItemStack>>>> recipesIngredients = maidRecipesManager.getRecipesIngredients();
        // 可放入原料进行酿酒(啤酒桶可酿酒:statusCode$tlma==2||statusCode$tlma==0和有原料)
        if (!b && !recipesIngredients.isEmpty()) {
            return true;
        }

        // 有输入
        return hasInput(inventory);
    }

    @Override
    public void maidCookMake(ServerLevel serverLevel, EntityMaid entityMaid, BeerBarrelBlockEntity blockEntity, MaidRecipesManager<BrewingRecipe> maidRecipesManager) {
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);
        extractOutputStack(getContainer(blockEntity), availableInv, blockEntity);
        extractInputStack(getContainer(blockEntity), availableInv, blockEntity);
        tryInsertItem(serverLevel, entityMaid, blockEntity, maidRecipesManager);
    }

    @Override
    public void extractOutputStack(Container inventory, IItemHandlerModifiable availableInv, BlockEntity blockEntity) {
        ItemStack stackInSlot = inventory.getItem(this.getOutputSlot());

        if (!stackInSlot.isEmpty() && ((BeerBarrelBlockAccessor)blockEntity).statusCode$tlma() == 2) {
            ItemStack copy = stackInSlot.copy();
            inventory.removeItem(this.getOutputSlot(), stackInSlot.getCount());
            ItemHandlerHelper.insertItemStacked(availableInv, copy, false);
            ((BeerBarrelBlockEntity)blockEntity).markDirty();
        }
    }

    @Override
    public boolean canTakeOutput(Container inventory, BeerBarrelBlockEntity beerBarrelBlockEntity) {
        ItemStack outputStack = inventory.getItem(this.getOutputSlot());

        return !outputStack.isEmpty() && ((BeerBarrelBlockAccessor)beerBarrelBlockEntity).statusCode$tlma() == 2;
    }

    @Override
    public void tryInsertItem(ServerLevel serverLevel, EntityMaid entityMaid, BeerBarrelBlockEntity blockEntity, MaidRecipesManager<BrewingRecipe> maidRecipesManager) {
        if (((BeerBarrelBlockAccessor)blockEntity).statusCode$tlma() != 0) return;
        super.tryInsertItem(serverLevel, entityMaid, blockEntity, maidRecipesManager);
    }

    @Override
    public boolean inputCanTake(boolean beInnerCanCook, Container inventory) {
        return hasInput(inventory);
    }

    @Override
    public void extractInputStack(Container inventory, IItemHandlerModifiable availableInv, BlockEntity blockEntity) {
        for (int i = this.getInputStartSlot(); i < this.getInputSize() + this.getInputStartSlot(); ++i) {
            ItemStack stackInSlot = inventory.getItem(i);
            ItemStack copy = stackInSlot.copy();
            if (!stackInSlot.isEmpty()) {
                inventory.removeItem(i, stackInSlot.getCount());
                ItemHandlerHelper.insertItemStacked(availableInv, copy, false);
                blockEntity.setChanged();
            }
        }
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.DB_BEER;
    }
}
