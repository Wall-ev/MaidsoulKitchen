package com.github.wallev.maidsoulkitchen.task.cook.minecraft;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor.IAbstractFurnaceAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor.ICbeAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.common.inventory.MaidRecipesManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AbstractCookingRecipesManager extends MaidRecipesManager<AbstractCookingRecipe> {
    private final Map<RecipeType<AbstractCookingRecipe>, List<AbstractCookingRecipe>> typeRecMap = new HashMap<>();

    private Map<RecipeType<AbstractCookingRecipe>, List<Integer>> recipeTypeListIngredients;
    // 额外尝试标志位，因为酿酒有温度要求，是可实时变化的。
    private int extraTryTime = 0;

    public AbstractCookingRecipesManager(EntityMaid maid, TaskFurnace task) {
        super(maid, task, false);

        recipeTypeListIngredients = new HashMap<>();
    }

    public boolean hasRecipeIngredients() {
        return !recipeTypeListIngredients.isEmpty();
    }

    public boolean hasRecipeIngredientsWithTemp(RecipeType<AbstractCookingRecipe> recipeType) {
        if (!recipeTypeListIngredients.isEmpty()) {
            for (RecipeType<AbstractCookingRecipe> type : recipeTypeListIngredients.keySet()) {
                if (type.equals(recipeType)) {
                    extraTryTime = 0;
                    return true;
                }
            }

            if (extraTryTime++ > 20) {
                recipeTypeListIngredients.clear();
                return false;
            }
        }

        return false;
    }

    public Pair<List<Integer>, List<List<ItemStack>>> getRecipeIngredient(RecipeType<AbstractCookingRecipe> recipeType) {
        RecipeType<AbstractCookingRecipe> tempType = null;
        for (RecipeType<AbstractCookingRecipe> type : recipeTypeListIngredients.keySet()) {
            if (type.equals(recipeType)) {
                tempType = type;
                break;
            }
        }
        if (tempType == null) {
            return Pair.of(Collections.emptyList(), Collections.emptyList());
        }

        List<Integer> orDefault = recipeTypeListIngredients.getOrDefault(tempType, Collections.emptyList());
        if (orDefault.isEmpty()) return Pair.of(Collections.emptyList(), Collections.emptyList());

        int remove = orDefault.remove(0);
        if (orDefault.isEmpty()) recipeTypeListIngredients.remove(tempType);


        if (recipeTypeListIngredients.isEmpty()) {
            Pair<List<Integer>, List<List<ItemStack>>> ingredients = recipesIngredients.get(remove);
            recipesIngredients.clear();
            return ingredients;
        } else {
            return recipesIngredients.get(remove);
        }

    }

    @NotNull
    protected List<Pair<List<Integer>, List<Item>>> getRecIngreMake(Map<Item, Integer> available) {
        if (recipeTypeListIngredients == null) {
            recipeTypeListIngredients = new HashMap<>();
        }
        Set<RecipeType<? extends AbstractCookingRecipe>> canRecipeTypes = searchAndCreateTemperate((ServerLevel) maid.level, maid);

        List<Pair<List<Integer>, List<Item>>> _make = new ArrayList<>();
        int index = 0;
        for (AbstractCookingRecipe r : this.currentRecs) {
            if (!canRecipeTypes.contains(r.getType())) {
                continue;
            }

            Pair<List<Integer>, List<Item>> maxCount = this.getAmountIngredient(r, available);
            if (!maxCount.getFirst().isEmpty()) {
                _make.add(Pair.of(maxCount.getFirst(), maxCount.getSecond()));

                RecipeType<?> type = r.getType();
                if (recipeTypeListIngredients.containsKey(type)) {
                    recipeTypeListIngredients.get(type).add(index);
                } else {
                    recipeTypeListIngredients.put((RecipeType<AbstractCookingRecipe>) type, Lists.newArrayList(index));
                }
                index++;
            }
        }
        repeat(_make, available, this.repeatTimes);
        return _make;
    }

    protected Set<RecipeType<? extends AbstractCookingRecipe>> searchAndCreateTemperate(ServerLevel worldIn, EntityMaid maid) {
        Set<RecipeType<? extends AbstractCookingRecipe>> canRecipeTypes = new HashSet<>();

        BlockPos centrePos = getSearchPos(maid);
        int searchRange = (int) maid.getRestrictRadius();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int y = 0; y <= 2; y = y > 0 ? -y : 1 - y) {
            for (int i = 0; i < searchRange; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        mutableBlockPos.setWithOffset(centrePos, x, y + 1, z);
                        if (maid.isWithinRestriction(mutableBlockPos)) {
                            BlockEntity blockEntity = worldIn.getBlockEntity(mutableBlockPos);
                            if (blockEntity instanceof AbstractFurnaceBlockEntity abstractFurnaceBlockEntity && !((ICbeAccessor) abstractFurnaceBlockEntity).tlmk$innerCanCook()) {
                                canRecipeTypes.add(((IAbstractFurnaceAccessor) abstractFurnaceBlockEntity).tlmk$getRecipeType());
                            }
                        }
                    }
                }
            }
        }
        return canRecipeTypes;
    }

    private static BlockPos getSearchPos(EntityMaid maid) {
        return maid.hasRestriction() ? maid.getRestrictCenter() : maid.blockPosition().below();
    }

}
