package com.github.catbert.tlma.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.catbert.tlma.TLMAddon.LOGGER;

public class MaidDataUtil {

    public static final EntityDataAccessor<Integer> START_Y_OFFSET = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.INT);

    public static void setStartYOffset(EntityMaid maid, int offset) {
        maid.getEntityData().set(START_Y_OFFSET, offset);
    }

    public static ItemStack getMaidSlotStack(EntityMaid maid, Integer slot) {
        return maid.getAvailableInv(true).getStackInSlot(slot);
    }

    public static Integer getStartYOffset(EntityMaid maid) {
        return maid.getEntityData().get(START_Y_OFFSET);
    }

    public static Integer getMaidInventoryItemStackSlot(EntityMaid maid, Predicate<ItemStack> predicate) {
        Map<Integer, ItemStack> map = new HashMap<>();
        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
        for (int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack slotStack = availableInv.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                if (predicate.test(slotStack)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static Integer findMaidInventoryItemStack(EntityMaid maid, Predicate<ItemStack> predicate) {
        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
        for (int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack slotStack = availableInv.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                if (predicate.test(slotStack)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static Integer findMaidInventoryItemStack(CombinedInvWrapper availableInv, Predicate<ItemStack> predicate) {
        for (int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack slotStack = availableInv.getStackInSlot(i);
            if (!slotStack.isEmpty() && predicate.test(slotStack)) return i;
        }
        return -1;
    }

    public static ItemStack findMaidInventoryStack(CombinedInvWrapper availableInv, Predicate<ItemStack> predicate) {
        for (int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack slotStack = availableInv.getStackInSlot(i);
            if (!slotStack.isEmpty() && predicate.test(slotStack)) return slotStack;
        }
        return ItemStack.EMPTY;
    }

    public static void consumerMaidInv(CombinedInvWrapper availableInv, Consumer<ItemStack> printConsumer) {
        for (int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack slotStack = availableInv.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                printConsumer.accept(slotStack);
            }
        }
    }

    public static void consumerPairMaidInv(CombinedInvWrapper availableInv, Consumer<Pair<ItemStack, Integer>> printConsumer) {
        for (int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack slotStack = availableInv.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                printConsumer.accept(new Pair<>(slotStack, i));
            }
        }
    }

    public static Integer findInventoryItemStack(IItemHandler chestInv, Predicate<ItemStack> predicate) {
        for (int i = 0; i < chestInv.getSlots(); ++i) {
            ItemStack slotStack = chestInv.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                if (predicate.test(slotStack)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static Map<Integer, Integer> findCommonVar(List<Integer> list) {
        Map<Integer, Integer> frequencyMap = list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(e -> 1)));

        Map<Integer, Integer> filteredMap = frequencyMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return filteredMap;
    }

    public static Map<Integer, Long> findCommonVar1(List<Integer> list) {
        Map<Integer, Long> frequencyMap = list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

//        frequencyMap = (Map<Integer, Long>) frequencyMap.entrySet().stream()
//                .filter(entry -> entry.getValue() > 1);
//                .forEach(entry -> System.out.println("Element " + entry.getKey() + " appears " + entry.getValue() + " times."));

        Map<Integer, Long> filteredMap = frequencyMap.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

//        for (Map.Entry<Integer, Long> entry : frequencyMap.entrySet()) {
//            System.out.println("Element " + entry.getKey() + " appears " + entry.getValue() + " times.");
//        }

        return filteredMap;
    }

    public static Map<Integer, ItemStack> getMaidInventoryItemStacks(EntityMaid maid) {
        Map<Integer, ItemStack> map = new HashMap<>();
        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
        for (int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack slotStack = availableInv.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                map.put(i, slotStack);
            }
        }
        return map;
    }

    public static Map<ItemStack, List<List<ItemData>>> getFdCookingRecipes(List<CookingPotRecipe> recipes, Level level){
        HashMap<ItemStack, List<List<ItemData>>> fdCookingRecipes = new HashMap<>();


        for (int i = 0; i < recipes.size(); i++) {
            List<List<ItemData>> list = new ArrayList<>();

            CookingPotRecipe recipe = recipes.get(i);

            ItemStack resultItem = recipe.getResultItem(level.registryAccess());

            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            List<ItemData> inputItems = new ArrayList<>();

            AtomicReference<Boolean> itemArrayBool = new AtomicReference<>(false);
            ArrayList<ItemStack> itemStackArray = new ArrayList<>();
            ArrayList<TagKey<Item>> tagKeysArray = new ArrayList<>();

            if (ingredients.size() == 1){
                JsonElement json = ingredients.get(0).toJson();
                if (json instanceof JsonObject){
                    LOGGER.info("json.getAsJsonObject().get(\"tag\"): {}", json.getAsJsonObject().get("tag"));
                    if (json.getAsJsonObject().get("tag") != null && json.getAsJsonObject().get("tag").getAsString() != null){
                        String string = json.getAsJsonObject().get("tag").getAsString();
                        inputItems.add(new ItemData(ItemTags.create(new ResourceLocation(string.split(":")[0], string.split(":")[1]))));
                    }
                }
            }else {
                ingredients.forEach(ingredient -> {
                    if (ingredient.getItems().length == 1) {
                        inputItems.add(new ItemData(ingredient.getItems()[0]));
                    }else {
                        JsonElement json = ingredient.toJson();
                        if (json instanceof JsonObject) {
                            if (json.getAsJsonObject().get("tag") != null){
                                String string = json.getAsJsonObject().get("tag").getAsString();
                                inputItems.add(new ItemData(ItemTags.create(new ResourceLocation(string.split(":")[0], string.split(":")[1]))));
                            }
                        }else if (json.isJsonArray()) {

                            LOGGER.info("json.getAsJsonArray(): {}", json.getAsJsonArray());
                            LOGGER.info("Arrays.stream(ingredient.getItems()).toList(): {}", Arrays.stream(ingredient.getItems()).toList());

                            itemArrayBool.set(true);

                            List<ItemStack> list1 = new ArrayList<>(Arrays.stream(ingredient.getItems()).toList());
                            ArrayList<String> itemsNameId = new ArrayList<>();
                            json.getAsJsonArray().asList().forEach(jsonElement -> {
                                if (jsonElement.getAsJsonObject().get("tag") != null) {
                                    String tagKey = jsonElement.getAsJsonObject().get("tag").getAsString();
                                    TagKey<Item> itemTagKey = ItemTags.create(new ResourceLocation(tagKey.split(":")[0], tagKey.split(":")[1]));
                                    tagKeysArray.add(itemTagKey);
                                }
                                if (jsonElement.getAsJsonObject().get("item") != null) {
                                    itemsNameId.add(jsonElement.getAsJsonObject().get("item").getAsString());
                                }
                            });

                            LOGGER.info("RemoveIf========================== ");
                            list1.removeIf(itemStack -> {
                                String location = itemStack.getItem().builtInRegistryHolder().key().location().toString();
                                LOGGER.info("string: {}", location);
                                return !itemsNameId.contains(location);
                            });
                            itemStackArray.addAll(list1);

                        }


                    }
                });
            }

            if (!inputItems.isEmpty()) {
                if (!itemArrayBool.get()) {
                    list.add(inputItems);
                    List<String> list1 = inputItems.stream().map(ItemData::toString).toList();
                    LOGGER.info("One list1: {}", list1);
                }else {

                    LOGGER.info("Array: start: ");


                    itemStackArray.forEach(itemStack -> {
                        List<ItemData> newInputItems = new ArrayList<>(inputItems);
                        newInputItems.add(new ItemData(itemStack));
                        List<String> list1 = newInputItems.stream().map(ItemData::toString).toList();
                        LOGGER.info("More item list1: {}", list1);
                        list.add(newInputItems);
                    });

                    tagKeysArray.forEach(tagKey -> {
                        List<ItemData> newInputItems = new ArrayList<>(inputItems);
                        newInputItems.add(new ItemData(tagKey));
                        List<String> list1 = newInputItems.stream().map(ItemData::toString).toList();
                        LOGGER.info("More Tag list2: {}", list1);
                        list.add(newInputItems);
                    });
                }
            }
            fdCookingRecipes.put(resultItem, list);

        }

        return fdCookingRecipes;
    }

    public static List<List<ItemData>> getRecipesIngredient3(List<CookingPotRecipe> recipes){
        List<List<ItemData>> list = new ArrayList<>();
        for (int i = 0; i < recipes.size(); i++) {
            CookingPotRecipe recipe = recipes.get(i);

            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            List<ItemData> inputItems = new ArrayList<>();

            AtomicReference<Boolean> itemArrayBool = new AtomicReference<>(false);
            ArrayList<ItemStack> itemStackArray = new ArrayList<>();
            ArrayList<TagKey<Item>> tagKeysArray = new ArrayList<>();

            if (ingredients.size() == 1){
                JsonElement json = ingredients.get(0).toJson();
                if (json instanceof JsonObject){
                    LOGGER.info("json.getAsJsonObject().get(\"tag\"): {}", json.getAsJsonObject().get("tag"));
                    if (json.getAsJsonObject().get("tag") != null && json.getAsJsonObject().get("tag").getAsString() != null){
                        String string = json.getAsJsonObject().get("tag").getAsString();
                        inputItems.add(new ItemData(ItemTags.create(new ResourceLocation(string.split(":")[0], string.split(":")[1]))));
                    }
                }
            }else {
                ingredients.forEach(ingredient -> {
                    if (ingredient.getItems().length == 1) {
                        inputItems.add(new ItemData(ingredient.getItems()[0]));
                    }else {
                        JsonElement json = ingredient.toJson();
                        if (json instanceof JsonObject) {
                            if (json.getAsJsonObject().get("tag") != null){
                                String string = json.getAsJsonObject().get("tag").getAsString();
                                inputItems.add(new ItemData(ItemTags.create(new ResourceLocation(string.split(":")[0], string.split(":")[1]))));
                            }
                        }else if (json.isJsonArray()) {

                            LOGGER.info("json.getAsJsonArray(): {}", json.getAsJsonArray());
                            LOGGER.info("Arrays.stream(ingredient.getItems()).toList(): {}", Arrays.stream(ingredient.getItems()).toList());

                            itemArrayBool.set(true);

                            List<ItemStack> list1 = new ArrayList<>(Arrays.stream(ingredient.getItems()).toList());
                            ArrayList<String> itemsNameId = new ArrayList<>();
                            json.getAsJsonArray().asList().forEach(jsonElement -> {
                                if (jsonElement.getAsJsonObject().get("tag") != null) {
                                    String tagKey = jsonElement.getAsJsonObject().get("tag").getAsString();
                                    TagKey<Item> itemTagKey = ItemTags.create(new ResourceLocation(tagKey.split(":")[0], tagKey.split(":")[1]));
                                    tagKeysArray.add(itemTagKey);
                                }
                                if (jsonElement.getAsJsonObject().get("item") != null) {
                                    itemsNameId.add(jsonElement.getAsJsonObject().get("item").getAsString());
                                }
                            });

                            LOGGER.info("RemoveIf========================== ");
                            list1.removeIf(itemStack -> {
                                String location = itemStack.getItem().builtInRegistryHolder().key().location().toString();
                                LOGGER.info("string: {}", location);
                                return !itemsNameId.contains(location);
                            });
                            itemStackArray.addAll(list1);

                        }


                    }
                });
            }

            if (!inputItems.isEmpty()) {
                if (!itemArrayBool.get()) {
                    list.add(inputItems);
                    List<String> list1 = inputItems.stream().map(ItemData::toString).toList();
                    LOGGER.info("One list1: {}", list1);
                }else {

                    LOGGER.info("Array: start: ");


                    itemStackArray.forEach(itemStack -> {
                        List<ItemData> newInputItems = new ArrayList<>(inputItems);
                        newInputItems.add(new ItemData(itemStack));
                        List<String> list1 = newInputItems.stream().map(ItemData::toString).toList();
                        LOGGER.info("More item list1: {}", list1);
                        list.add(newInputItems);
                    });

                    tagKeysArray.forEach(tagKey -> {
                        List<ItemData> newInputItems = new ArrayList<>(inputItems);
                        newInputItems.add(new ItemData(tagKey));
                        List<String> list1 = newInputItems.stream().map(ItemData::toString).toList();
                        LOGGER.info("More Tag list2: {}", list1);
                        list.add(newInputItems);
                    });
                }
            }

        }

        return list;
    }

    public static List<List<ItemData>> getRecipesIngredient2(List<CookingPotRecipe> recipes){
        List<List<ItemData>> list = new ArrayList<>();
        recipes.forEach(recipe -> {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            List<ItemData> inputItems = new ArrayList<>();

            List<Ingredient> list1 = ingredients.stream().toList();
            LOGGER.info("list1: {}", list1);

            if (ingredients.size() == 1){
                JsonElement json = ingredients.get(0).toJson();
                if (json instanceof JsonObject){
                    LOGGER.info("json.getAsJsonObject().get(\"tag\"): {}", json.getAsJsonObject().get("tag"));
                    if (json.getAsJsonObject().get("tag") != null && json.getAsJsonObject().get("tag").getAsString() != null){
                        String string = json.getAsJsonObject().get("tag").getAsString();
                        inputItems.add(new ItemData(ItemTags.create(new ResourceLocation(string.split(":")[0], string.split(":")[1]))));
                    }
                }
            }else {
                ingredients.forEach(ingredient -> {
                    if (ingredient.getItems().length == 1) {
                        inputItems.add(new ItemData(ingredient.getItems()[0]));
                    }else {
                        JsonElement json = ingredient.toJson();
                        if (json instanceof JsonObject) {
                            if (json.getAsJsonObject().get("tag") != null){
                                String string = json.getAsJsonObject().get("tag").getAsString();
                                inputItems.add(new ItemData(ItemTags.create(new ResourceLocation(string.split(":")[0], string.split(":")[1]))));
                            }
                        }

//                        new ItemStack()

                    }
                });
            }

            if (!inputItems.isEmpty()) {
                list.add(inputItems);
            }

        });
        return list;
    }

    public static List<List<ItemData>> getRecipesIngredient(List<CookingPotRecipe> recipes){
        List<List<ItemData>> list = new ArrayList<>();
        recipes.forEach(recipe -> {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            ingredients.forEach(ingredient -> {
                List<ItemData> inputItems = new ArrayList<>();
                if (Arrays.stream(ingredient.getItems()).count() == 1) {
                    inputItems.add(new ItemData(ingredient.getItems()[0]));
                    list.add(inputItems);
                }else {
                    JsonElement json = ingredient.toJson();
                    if (json.getAsJsonObject().get("tag") != null){
                        String string = json.getAsJsonObject().get("tag").getAsString();
                        inputItems.add(new ItemData(ItemTags.create(new ResourceLocation(string.split(":")[0], string.split(":")[1]))));
                        list.add(inputItems);
                    }
                }
            });
        });
        return list;
    }

    public static List<Map<List<ItemData>, ItemStack>> getCookingPotRecipes(Level level, List<CookingPotRecipe> recipes) {
        List<Map<List<ItemData>, ItemStack>> list = new ArrayList<>();
        recipes.forEach(recipe -> {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            ingredients.forEach(ingredient -> {
                Map<List<ItemData>, ItemStack> pair = new HashMap<>();
                List<ItemData> inputItems = new ArrayList<>();
                ItemStack resultItem = recipe.getResultItem(level.registryAccess());

                if (Arrays.stream(ingredient.getItems()).count() == 1) {
                    inputItems.add(new ItemData(ingredient.getItems()[0]));
                    pair.put(inputItems, resultItem);
                    list.add(pair);
                }else {
//                    if (ingredient)

                    JsonElement json = ingredient.toJson();

                    if (json instanceof JsonObject){
                        LOGGER.info("json.getAsJsonObject().get(\"tag\"): {}", json.getAsJsonObject().get("tag"));
                        if (json.getAsJsonObject().get("tag") != null && json.getAsJsonObject().get("tag").getAsString() != null){
                            String string = json.getAsJsonObject().get("tag").getAsString();
                            inputItems.add(new ItemData(ItemTags.create(new ResourceLocation(string.split(":")[0], string.split(":")[1]))));
                            pair.put(inputItems, resultItem);
                            list.add(pair);
                        }
                    }
                }
            });
        });
        return list;
    }

    public static class ItemData {
        private ItemStack itemStack;
        private TagKey<Item> tagKey;

        public ItemData(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public String toString() {
            return "ItemData{" +
                    "itemStack=" + itemStack +
                    ", tagKey=" + tagKey +
                    '}';
        }

        public ItemData(TagKey<Item> tagKey) {
            this.tagKey = tagKey;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public TagKey<Item> getTagKey() {
            return tagKey;
        }
    }

}
