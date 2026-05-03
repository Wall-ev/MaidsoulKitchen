package com.github.wallev.maidsoulkitchen.compat.msm.cuisinedelight.cuisine;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.content.logic.FoodType;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import dev.xkmc.cuisinedelight.content.recipe.BaseCuisineRecipe;
import dev.xkmc.cuisinedelight.init.registrate.CDMisc;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.stream.IntStream;

public class VirtualCookedFoodData {

    public static void makeValidRecipes(Level level) {
        level.getRecipeManager()
                .getAllRecipesFor(CDMisc.RT_CUISINE.get())
                .forEach(recipe -> {
                    NonNullList<Ingredient> ingredients = recipe.getIngredients();
                    ItemStack resultItem = recipe.getResultItem(RegistryAccess.EMPTY);
                    if (ingredients.isEmpty()) {
                        return;
                    }

                    List<List<ItemStack>> flatIngredients = ingredients.stream()
                            .map(ingredient -> Arrays.stream(ingredient.getItems()).toList())
                            .toList();


                    MaidsoulKitchen.LOGGER.debug("Start checker all ingredients");
                    if (flatIngredients.size() > 1) {
                        List<ItemStack> ingredient0 = flatIngredients.get(0);
                        List<ItemStack> ingredient1 = flatIngredients.get(1);

                        for (ItemStack itemStack0 : ingredient0) {
                            for (ItemStack itemStack1 : ingredient1) {
                                CookedFoodData cookedFoodData = create(List.of(itemStack0, itemStack1));
                                ItemStack bestItemStack = getBestItemStack(level, cookedFoodData);
                                if (!bestItemStack.is(resultItem.getItem())) {
                                    MaidsoulKitchen.LOGGER.error("No single ingredients to make food[two ingredients]: {} {} {} {}", itemStack0, itemStack1, bestItemStack, resultItem);
                                }
                            }
                        }
                    } else {
                        for (ItemStack itemStack : flatIngredients.get(0)) {
                            CookedFoodData cookedFoodData = create(List.of(itemStack));
                            ItemStack bestItemStack = getBestItemStack(level, cookedFoodData);
                            if (!bestItemStack.is(resultItem.getItem())) {
                                MaidsoulKitchen.LOGGER.error("No single ingredients to make food[one ingredients]: {} {} {}", itemStack, bestItemStack, resultItem);
                            }
                        }
                    }
                    MaidsoulKitchen.LOGGER.debug("End checker all ingredients");
                });

    }

    public static ItemStack getBestItemStack(Level level, CookedFoodData data) {
        ItemStack bestMatch = BaseCuisineRecipe.findBestMatch(level, data);
        return bestMatch;
    }

    @SuppressWarnings("all")
    public static CookedFoodData create(List<ItemStack> itemStacks) {
        CookedFoodData cookedFoodData = new CookedFoodData();

        int size = 0;
        int nutrition = 0;
        float penalty = 0.0F;

        for (ItemStack item : itemStacks) {
            IngredientConfig.IngredientEntry config = IngredientConfig.get().getEntry(item);
            if (config != null) {
                int itemSize = config.size * item.getCount();
                penalty += (float) itemSize * 0.0F;
                size += itemSize;
                nutrition += config.nutrition * itemSize;
                cookedFoodData.entries.add(new CookedFoodData.Entry(item, itemSize, false, false, false));
                if (config.type != FoodType.NONE) {
                    cookedFoodData.types.add(config.type);
                }
            }
        }

        float goodness = size == 0 ? 0.0F : Mth.clamp(1.0F - penalty / (float) size, 0.0F, 1.0F);
        cookedFoodData.score = 100;
        cookedFoodData.size = size;
        cookedFoodData.total = size;
        cookedFoodData.nutrition = size == 0 ? 0 : Math.round(goodness * (float) nutrition / (float) size);
        cookedFoodData.glowstone = 0;
        cookedFoodData.redstone = 0;

        return cookedFoodData;
    }


    // 58 126
// 196 315
    public static void makeValidRecipes0(Level level) {
        Map<ItemStack, List<Pair<ItemStack, ItemStack>>> vaildRecips = new HashMap<>();

        level.getRecipeManager()
                .getAllRecipesFor(CDMisc.RT_CUISINE.get())
                .forEach(recipe -> {
                    List<Ingredient> ingredients = recipe.list.stream().map(l -> {
                        return l.ingredient();
                    }).toList();
                    ItemStack resultItem = recipe.getResultItem(RegistryAccess.EMPTY);
                    if (ingredients.isEmpty()) {
                        return;
                    }

                    List<List<ItemStack>> flatIngredients = ingredients.stream()
                            .map(ingredient -> Arrays.stream(ingredient.getItems()).toList())
                            .toList();


                    MaidsoulKitchen.LOGGER.debug("----------------------------------------------------------------------");
                    MaidsoulKitchen.LOGGER.debug("Start checker all ingredients: {}", resultItem);
                    for (List<ItemStack> flatIngredient : flatIngredients) {
                        MaidsoulKitchen.LOGGER.debug("ingredients: {}", flatIngredient);
                    }
                    if (flatIngredients.size() > 1) {
                        List<ItemStack> ingredient0 = flatIngredients.get(0);
                        List<ItemStack> ingredient1 = flatIngredients.get(1);

                        for (ItemStack itemStack0 : ingredient0) {
                            for (ItemStack itemStack1 : ingredient1) {
                                CookedFoodData cookedFoodData = create(List.of(itemStack0, itemStack1));
                                ItemStack bestItemStack = getBestItemStack(level, cookedFoodData);
                                if (!bestItemStack.is(resultItem.getItem())) {
                                    MaidsoulKitchen.LOGGER.error("Make other food[two ingredients]: {} {} {}", itemStack0, itemStack1, bestItemStack);
                                }

                                boolean add = false;
                                for (Map.Entry<ItemStack, List<Pair<ItemStack, ItemStack>>> entry : vaildRecips.entrySet()) {
                                    ItemStack key = entry.getKey();
                                    if (key.is(bestItemStack.getItem())) {
                                        CompoundTag keyCompoundTag = key.getOrCreateTag();
                                        int keyTotal = keyCompoundTag.getInt("total");

                                        CompoundTag resultCompoundTag = key.getOrCreateTag();
                                        int resultTotal = resultCompoundTag.getInt("total");

                                        if (keyTotal == resultTotal) {
                                            entry.getValue().add(Pair.of(itemStack0, itemStack1));
                                            add = true;
                                            break;
                                        }
                                    }
                                }

                                if (!add) {
                                    vaildRecips.computeIfAbsent(bestItemStack, (key) -> new ArrayList<>())
                                            .add(Pair.of(itemStack0, itemStack1));
                                }


                            }
                        }
                    } else {
                        for (ItemStack itemStack : flatIngredients.get(0)) {
                            CookedFoodData cookedFoodData = create(List.of(itemStack));
                            ItemStack bestItemStack = getBestItemStack(level, cookedFoodData);
                            if (!bestItemStack.is(resultItem.getItem())) {
                                MaidsoulKitchen.LOGGER.error("Make other food[one ingredients]: {} {}", itemStack, bestItemStack);
                            }

                            boolean add = false;
                            for (Map.Entry<ItemStack, List<Pair<ItemStack, ItemStack>>> entry : vaildRecips.entrySet()) {
                                ItemStack key = entry.getKey();
                                if (key.is(bestItemStack.getItem())) {
                                    CompoundTag keyCompoundTag = key.getOrCreateTag();
                                    int keyTotal = keyCompoundTag.getInt("total");

                                    CompoundTag resultCompoundTag = key.getOrCreateTag();
                                    int resultTotal = resultCompoundTag.getInt("total");

                                    if (keyTotal == resultTotal) {
                                        entry.getValue().add(Pair.of(itemStack, ItemStack.EMPTY));
                                        add = true;
                                        break;
                                    }
                                }
                            }

                            if (!add) {
                                vaildRecips.computeIfAbsent(bestItemStack, (key) -> new ArrayList<>())
                                        .add(Pair.of(itemStack, ItemStack.EMPTY));
                            }
                        }
                    }
                    MaidsoulKitchen.LOGGER.debug("****************************************************");
//                    MaidsoulKitchen.LOGGER.debug("End checker all ingredients");
                });


        HashMap<ItemStack, List<ItemStack>> convert = new HashMap<>();
        new HashMap<>(vaildRecips)
                .entrySet()
                .stream()
                .flatMap(entry -> {
                    ItemStack result = entry.getKey();
                    List<Pair<ItemStack, ItemStack>> inputs = entry.getValue();

                    Map<ItemStack, List<ItemStack>> flatRecipes = new HashMap<>();

                    inputs.forEach(oInput -> {
                        if (oInput.getSecond().isEmpty()) {
                            IntStream.range(0, 9)
                                    .forEach(i -> {

                                        List<ItemStack> jInput = new ArrayList<>();
                                        for (int j = 0; j < i + 1; j++) {
                                            jInput.add(oInput.getFirst());
                                        }

                                        ItemStack copy = result.copy();
                                        CompoundTag compoundTag = copy.getOrCreateTag();
                                        CompoundTag resultCompoundTag = compoundTag.getCompound("CookedFoodData");
                                        int resultTotal = resultCompoundTag.getInt("total");
                                        resultCompoundTag.putInt("total", resultTotal * (i + 1));
                                        copy.setTag(compoundTag);
                                        copy.setCount(resultTotal * (i + 1));


                                        boolean add = false;
                                        for (Map.Entry<ItemStack, List<ItemStack>> o : flatRecipes.entrySet()) {
                                            ItemStack key = o.getKey();
                                            if (key.is(copy.getItem())) {
                                                CompoundTag oresultCompoundTag = key.getOrCreateTag().getCompound("CookedFoodData");
                                                int oresultTotal = oresultCompoundTag.getInt("total") * (i + 1);

                                                if (oresultTotal == resultTotal) {
                                                    o.getValue().addAll(jInput);
                                                    add = true;
                                                    break;
                                                }
                                            }
                                        }

                                        if (!add) {
                                            flatRecipes.computeIfAbsent(copy, (key) -> new ArrayList<>())
                                                    .addAll(jInput);
                                        }

//                                                flatRecipes.put(copy, jInput);
//                                                return jInput;
                                    });
                        } else {
                            IntStream.range(0, 4)
                                    .forEach(i -> {

                                        List<ItemStack> jInput = new ArrayList<>();
                                        for (int j = 0; j < i + 1; j++) {
                                            jInput.add(oInput.getFirst());
                                            jInput.add(oInput.getSecond());
                                        }

                                        ItemStack copy = result.copy();
                                        CompoundTag compoundTag = copy.getOrCreateTag();
                                        CompoundTag resultCompoundTag = compoundTag.getCompound("CookedFoodData");
                                        int resultTotal = resultCompoundTag.getInt("total");
                                        resultCompoundTag.putInt("total", resultTotal * (i + 1));
                                        copy.setTag(compoundTag);
                                        copy.setCount(resultTotal * (i + 1));

                                        boolean add = false;
                                        for (Map.Entry<ItemStack, List<ItemStack>> o : flatRecipes.entrySet()) {
                                            ItemStack key = o.getKey();
                                            if (key.is(copy.getItem())) {
                                                CompoundTag oresultCompoundTag = key.getOrCreateTag().getCompound("CookedFoodData");
                                                int oresultTotal = oresultCompoundTag.getInt("total") * (i + 1);

                                                if (oresultTotal == resultTotal) {
                                                    o.getValue().addAll(jInput);
                                                    add = true;
                                                    break;
                                                }
                                            }
                                        }

                                        if (!add) {
                                            flatRecipes.computeIfAbsent(copy, (key) -> new ArrayList<>())
                                                    .addAll(jInput);
                                        }

//                                                flatRecipes.put(copy, jInput);
//                                                return jInput;
                                    });
                        }
                    });

                    return flatRecipes.entrySet().stream();
                }).forEach(itemStackListEntry -> {
                    convert.put(itemStackListEntry.getKey(), itemStackListEntry.getValue());
                });

        int a = 1;

    }

    public static ItemStack getBestItemStack0(Level level, CookedFoodData data) {
        ItemStack bestMatch = BaseCuisineRecipe.findBestMatch(level, data);
        return bestMatch;
    }

    @SuppressWarnings("all")
    public static CookedFoodData create0(List<ItemStack> itemStacks) {
        CookedFoodData cookedFoodData = new CookedFoodData();

        int size = 0;
        int nutrition = 0;
        float penalty = 0.0F;

        for (ItemStack item : itemStacks) {
            IngredientConfig.IngredientEntry config = IngredientConfig.get().getEntry(item);
            if (config != null) {
                int itemSize = config.size * item.getCount();
                penalty += (float) itemSize * 0.0F;
                size += itemSize;
                nutrition += config.nutrition * itemSize;
                cookedFoodData.entries.add(new CookedFoodData.Entry(item, itemSize, false, false, false));
                if (config.type != FoodType.NONE) {
                    cookedFoodData.types.add(config.type);
                }
            }
        }

        float goodness = size == 0 ? 0.0F : Mth.clamp(1.0F - penalty / (float) size, 0.0F, 1.0F);
        cookedFoodData.score = 100;
        cookedFoodData.size = size;
        cookedFoodData.total = size;
        cookedFoodData.nutrition = size == 0 ? 0 : Math.round(goodness * (float) nutrition / (float) size);
        cookedFoodData.glowstone = 0;
        cookedFoodData.redstone = 0;

        return cookedFoodData;
    }

}
