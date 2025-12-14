package com.github.wallev.maidsoulkitchen.datagen.recipe.itemuse;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.init.ModRecipes;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.recipe.itemuse.ItemUseRecipe.Condition;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class ItemUseRecipeBuilder implements RecipeBuilder {
    private Condition type = Condition.SINGLE;
    private final List<ICondition> conditions = new ArrayList<>();
    private final List<ItemStack> inputs = new ArrayList<>();
    private final List<ItemStack> results =  new ArrayList<>();

    private final Set<String> modIds = new HashSet<>();

    public static ItemUseRecipeBuilder builder() {
        return new ItemUseRecipeBuilder();
    }

    public ItemUseRecipeBuilder addCondition(ICondition condition) {
        this.conditions.add(condition);
        return this;
    }

    public ItemUseRecipeBuilder addModRecipe(IMods mod) {
        if (modIds.contains(mod.modId()))
            return this;

        return this.addModRecipe(mod.modId());
    }

    public ItemUseRecipeBuilder addModRecipe(String modId) {
        if (modIds.contains(modId))
            return this;

        this.modIds.add(modId);
        return addCondition(new ModLoadedCondition(modId));
    }

    public ItemUseRecipeBuilder setType(com.github.wallev.maidsoulkitchen.recipe.itemuse.ItemUseRecipe.Condition type) {
        this.type = type;
        return this;
    }

    public ItemUseRecipeBuilder setInputs(ItemStack... stack) {
        this.inputs.addAll(List.of(stack));
        return this;
    }

    public ItemUseRecipeBuilder setInput(ItemStack stack) {
        this.inputs.add(stack);
        return this;
    }

    public ItemUseRecipeBuilder setInput(ItemLike itemLike) {
        this.inputs.add(new ItemStack(itemLike));
        return this;
    }

    public ItemUseRecipeBuilder setInput(ItemLike itemLike, int count) {
        this.inputs.add(new ItemStack(itemLike, count));
        return this;
    }

    public ItemUseRecipeBuilder setResults(ItemStack... stack) {
        this.results.addAll(List.of(stack));
        return this;
    }

    public ItemUseRecipeBuilder setResult(ItemStack stack) {
        this.results.add(stack);
        return this;
    }

    public ItemUseRecipeBuilder setResult(ItemLike itemLike) {
        this.results.add(new ItemStack(itemLike));
        return this;
    }

    public ItemUseRecipeBuilder setResult(ItemLike itemLike, int count) {
        this.results.add(new ItemStack(itemLike, count));
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.results.get(0).getItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> output) {
        this.checkModRecipe();
        String path = RecipeBuilder.getDefaultRecipeId(this.getResult()).getPath();
        ResourceLocation filePath = new ResourceLocation(MaidsoulKitchen.MOD_ID, type.toString() + "_item_use" + "/" + path);
        this.save(output, filePath);
    }

    @Override
    public void save(Consumer<FinishedRecipe> output, String recipeId) {
        this.checkModRecipe();
        ResourceLocation filePath = new ResourceLocation(MaidsoulKitchen.MOD_ID, type.toString() + "_item_use" + "/" + recipeId);
        this.save(output, filePath);
    }

    @Override
    public void save(Consumer<FinishedRecipe> recipeOutput, ResourceLocation id) {
        this.checkModRecipe();
        recipeOutput.accept(new ItemUseRecipe(type, id, this.inputs, this.results, this.conditions));
    }

    public void checkModRecipe() {
        List<ItemStack> allItems = Lists.newArrayList();
        allItems.addAll(this.inputs);
        allItems.addAll(this.results);
        allItems.forEach(itemStack -> {
            String itemModId = itemStack.getItem().getCreatorModId(itemStack);
            if (itemModId == null)
                return;
            if (itemModId.equals(Mods.MC.modId()))
                return;
            if (itemModId.equals(Mods.MSK.modId()))
                return;
            if (modIds.contains(itemModId))
                return;

            this.addModRecipe(itemModId);
        });

    }

    public record ItemUseRecipe(Condition type,
                                ResourceLocation id,
                                List<ItemStack> input,
                                List<ItemStack> result,
                                List<ICondition> conditions) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            if (!this.conditions.isEmpty()) {
                JsonArray conditionsJson = CraftingHelper.serialize(this.conditions.toArray(new ICondition[]{}));
                json.add("conditions", conditionsJson);
            }

            json.addProperty("condition", this.type.toString());

            JsonArray inputsJson = new JsonArray();
            for (ItemStack itemStack : this.input) {
                JsonObject inputJson = new JsonObject();
                inputJson.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).toString());
                CompoundTag inputTag = itemStack.getTag();
                if (inputTag != null) {
                    String string = inputTag.toString();
                    inputJson.addProperty("nbt", string);
                }
                inputsJson.add(inputJson);
            }
            json.add("inputs", inputsJson);

            JsonArray resultsJson = new JsonArray();
            for (ItemStack itemStack : this.result) {
                JsonObject resultJson = new JsonObject();
                resultJson.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).toString());
                CompoundTag inputTag = itemStack.getTag();
                if (inputTag != null) {
                    String string = inputTag.toString();
                    resultJson.addProperty("nbt", string);
                }
                resultsJson.add(resultJson);
            }
            json.add("results", resultsJson);


//            JsonObject resultJson = new JsonObject();
//            resultJson.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.result.getItem())).toString());
//            CompoundTag outputTag = this.result.getTag();
//            if (outputTag != null) {
//                String string = outputTag.toString();
//                resultJson.addProperty("nbt", string);
//            }
//            json.add("result", resultJson);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipes.ITEM_USE_SERIALIZER.get();
        }

        @Override
        @Nullable
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        @Nullable
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}