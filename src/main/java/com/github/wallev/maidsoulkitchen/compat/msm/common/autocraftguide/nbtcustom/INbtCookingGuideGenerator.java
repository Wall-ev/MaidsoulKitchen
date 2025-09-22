package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.nbtcustom;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import studio.fantasyit.maid_storage_manager.craft.CollectCraftEvent;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.generator.algo.ICachableGeneratorGraph;
import studio.fantasyit.maid_storage_manager.craft.generator.algo.node.IngredientNode;
import studio.fantasyit.maid_storage_manager.craft.generator.algo.node.ItemNode;
import studio.fantasyit.maid_storage_manager.craft.generator.algo.node.Node;
import studio.fantasyit.maid_storage_manager.craft.generator.algo.node.SpecialCraftNode;
import studio.fantasyit.maid_storage_manager.craft.generator.cache.RecipeIngredientCache;
import studio.fantasyit.maid_storage_manager.craft.generator.util.GenerateCondition;
import studio.fantasyit.maid_storage_manager.data.InventoryItem;
import studio.fantasyit.maid_storage_manager.util.StorageAccessUtil;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class INbtCookingGuideGenerator implements ICookingGuideGenerator<ItemStack> {
    private UUID FROM_INGREDIENT_UUID = UUID.randomUUID();

    public INbtCookingGuideGenerator(CollectCraftEvent event) {
        event.addAutoCraftGuideGenerator(this);
        Item nbtItemStackItem = this.getNbtItemStackItem();
        event.addItemStackPredicate(nbtItemStackItem, (stack, target) -> {
            if (!stack.is(nbtItemStackItem)) {
                return false;
            }
            if (!target.is(nbtItemStackItem)) {
                return false;
            }

            return isSameItemStack(stack, target);
        });
    }

    public abstract boolean isSameItemStack(ItemStack stack, ItemStack target);

    public abstract Item getNbtItemStackItem();

    public abstract Ingredient getAllIngredient();

    public Ingredient createAllIngredient(Predicate<Item> predicate) {
        Stream<ItemStack> itemStream = ForgeRegistries.ITEMS
                .getValues()
                .stream()
                .filter(predicate)
                .map(Item::getDefaultInstance);
        return Ingredient.of(itemStream);
    }

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return isValidGroundBlock(level, pos);
    }

    @Override
    public void generate(List<InventoryItem> inventory, Level level, BlockPos pos, ICachableGeneratorGraph graph, Map<ResourceLocation, List<BlockPos>> recognizedTypePositions) {
        StorageAccessUtil.Filter posFilter = GenerateCondition.getFilterOn(level, pos);
        graph.addSpecialCraftNode(id -> new SpecialCraftNode(id) {
            @Override
            public void buildGraph(ICachableGeneratorGraph graph) {
                for (Node node : graph.getNodes()) {
                    if (node instanceof ItemNode in) {
                        ItemStack itemStack = in.itemStack;
                        if (itemStack.is(getNbtItemStackItem())) {
                            ItemNode itemNodeOrCreate = graph.getItemNodeOrCreate(itemStack, false);
                            addEdge(itemNodeOrCreate, 1);
                        }
                    }
                }

                Ingredient ingredient = getAllIngredient();
                IngredientNode ingredientNode = graph.addOrGetCahcedIngredientNode(ingredient, FROM_INGREDIENT_UUID);
                ingredientNode.addEdge(this, 1);
                graph.addToQueue(this);
            }

            @Override
            public void generate(ICachableGeneratorGraph graph) {
                for (Node node : graph.getNodes()) {
                    if (node instanceof ItemNode in && in.itemStack.is(getNbtItemStackItem())) {
                        ItemStack cakeRoll = in.itemStack;

                        if (!posFilter.isAvailable(cakeRoll))
                            continue;
                        if (!graph.getItemNodeOrCreate(cakeRoll, false).related)
                            continue;

                        List<ItemStack> ingredients = getInputsWithItemStack(cakeRoll);
                        CraftGuideOperator2 craftGuide = CraftGuideOperator2.create(pos);
                        INbtCookingGuideGenerator.this.generateStep(
                                inventory,
                                level,
                                pos,
                                graph,
                                recognizedTypePositions,
                                craftGuide,
                                getOutputs(cakeRoll, level.registryAccess()),
                                ingredients);
                        CraftGuideData craftGuideData = craftGuide.makeCraftGuideData();
                        graph.addCraftGuide(craftGuideData);
                    }
                }
            }

            @Override
            public String toString() {
                return "SpecialNode:" + getType().toString().toLowerCase(Locale.ENGLISH) + "#" + id;
            }
        });
    }

    public abstract void generateStep(List<InventoryItem> inventory,
                                                Level level,
                                                BlockPos pos,
                                                ICachableGeneratorGraph graph,
                                                Map<ResourceLocation, List<BlockPos>> recognizedTypePositions,
                                                CraftGuideOperator2 craftGuide,
                                                List<ItemStack> results,
                                                List<ItemStack> inputs);

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return true;
    }

    @Override
    public void consumeRecipes(RecipeManager manager, Consumer<ItemStack> recipeConsumer) {
    }

    @Override
    public void onCache(RecipeManager manager) {
        Ingredient ingredient = getAllIngredient();
        FROM_INGREDIENT_UUID = RecipeIngredientCache.cacheIngredient(ingredient);
    }

    @Override
    public ResourceLocation getRecipeId(ItemStack recipe) {
        return null;
    }

    @Override
    public List<Ingredient> getInputs(ItemStack recipe) {
        return List.of();
    }

    public abstract List<ItemStack> getInputsWithItemStack(ItemStack itemStack);

    @Override
    public abstract List<ItemStack> getOutputs(ItemStack recipe, RegistryAccess registryAccess);

    @Override
    public boolean shouldCacheRecipe(ItemStack recipe) {
        return false;
    }
}