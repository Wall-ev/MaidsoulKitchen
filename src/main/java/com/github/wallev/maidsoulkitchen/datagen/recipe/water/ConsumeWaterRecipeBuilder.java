package com.github.wallev.maidsoulkitchen.datagen.recipe.water;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.init.ModRecipes;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.recipe.water.ConsumeWaterRecipe;
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

public class ConsumeWaterRecipeBuilder implements RecipeBuilder {
    private ConsumeWaterRecipe.Condition type = ConsumeWaterRecipe.Condition.SINGLE;
    private final List<ICondition> conditions = new ArrayList<>();
    private ItemStack input = ItemStack.EMPTY;
    private ItemStack result = ItemStack.EMPTY;

    private final Set<String> modIds = new HashSet<>();

    public static ConsumeWaterRecipeBuilder builder() {
        return new ConsumeWaterRecipeBuilder();
    }

    public ConsumeWaterRecipeBuilder addCondition(ICondition condition) {
        this.conditions.add(condition);
        return this;
    }

    public ConsumeWaterRecipeBuilder addModRecipe(IMods mod) {
        if (modIds.contains(mod.modId()))
            return this;

        return this.addModRecipe(mod.modId());
    }

    public ConsumeWaterRecipeBuilder addModRecipe(String modId) {
        if (modIds.contains(modId))
            return this;

        this.modIds.add(modId);
        return addCondition(new ModLoadedCondition(modId));
    }

    public ConsumeWaterRecipeBuilder setType(ConsumeWaterRecipe.Condition type) {
        this.type = type;
        return this;
    }

    public ConsumeWaterRecipeBuilder setInput(ItemStack stack) {
        this.input = stack;
        return this;
    }

    public ConsumeWaterRecipeBuilder setInput(ItemLike itemLike) {
        this.input = new ItemStack(itemLike);
        return this;
    }

    public ConsumeWaterRecipeBuilder setInput(ItemLike itemLike, int count) {
        this.input = new ItemStack(itemLike, count);
        return this;
    }

    public ConsumeWaterRecipeBuilder setResult(ItemStack stack) {
        this.result = stack;
        return this;
    }

    public ConsumeWaterRecipeBuilder setResult(ItemLike itemLike) {
        this.result = new ItemStack(itemLike);
        return this;
    }

    public ConsumeWaterRecipeBuilder setResult(ItemLike itemLike, int count) {
        this.result = new ItemStack(itemLike, count);
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
        return this.result.getItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> output) {
        this.checkModRecipe();
        String path = RecipeBuilder.getDefaultRecipeId(this.getResult()).getPath();
        ResourceLocation filePath = new ResourceLocation(MaidsoulKitchen.MOD_ID, type.toString() + "_water" + "/" + path);
        this.save(output, filePath);
    }

    @Override
    public void save(Consumer<FinishedRecipe> output, String recipeId) {
        this.checkModRecipe();
        ResourceLocation filePath = new ResourceLocation(MaidsoulKitchen.MOD_ID, type.toString() + "_water" + "/" + recipeId);
        this.save(output, filePath);
    }

    @Override
    public void save(Consumer<FinishedRecipe> recipeOutput, ResourceLocation id) {
        this.checkModRecipe();
        recipeOutput.accept(new GetterWaterRecipe(type, id, this.input, this.result, this.conditions));
    }

    public void checkModRecipe() {
        List<ItemStack> allItems = Lists.newArrayList();
        allItems.add(this.input);
        allItems.add(this.result);
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

    public record GetterWaterRecipe(ConsumeWaterRecipe.Condition type,
                                    ResourceLocation id,
                                    ItemStack input,
                                    ItemStack result,
                                    List<ICondition> conditions) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            if (!this.conditions.isEmpty()) {
                JsonArray conditionsJson = CraftingHelper.serialize(this.conditions.toArray(new ICondition[]{}));
                json.add("conditions", conditionsJson);
            }

            json.addProperty("condition", this.type.toString());

            JsonObject inputJson = new JsonObject();
            inputJson.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.input.getItem())).toString());
            CompoundTag inputTag = this.input.getTag();
            if (inputTag != null) {
                String string = inputTag.toString();
                inputJson.addProperty("nbt", string);
            }
            json.add("input", inputJson);

            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.result.getItem())).toString());
            CompoundTag outputTag = this.result.getTag();
            if (outputTag != null) {
                String string = outputTag.toString();
                resultJson.addProperty("nbt", string);
            }
            json.add("result", resultJson);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipes.CONSUME_WATER_SERIALIZER.get();
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