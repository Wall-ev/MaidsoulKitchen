package studio.fantasyit.maid_storage_manager.craft.generator.type.misc;

import com.tacz.guns.block.entity.GunSmithTableBlockEntity;
import com.tacz.guns.crafting.GunSmithTableIngredient;
import com.tacz.guns.crafting.GunSmithTableRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.context.special.TaczRecipeAction;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.generator.algo.ICachableGeneratorGraph;
import studio.fantasyit.maid_storage_manager.craft.generator.algo.node.ItemNode;
import studio.fantasyit.maid_storage_manager.craft.generator.algo.node.SpecialCraftNode;
import studio.fantasyit.maid_storage_manager.craft.generator.type.base.IAutoCraftGuideGenerator;
import studio.fantasyit.maid_storage_manager.craft.generator.util.GenerateCondition;
import studio.fantasyit.maid_storage_manager.craft.type.CraftingType;
import studio.fantasyit.maid_storage_manager.craft.type.TaczType;
import studio.fantasyit.maid_storage_manager.data.InventoryItem;
import studio.fantasyit.maid_storage_manager.integration.tacz.TaczRecipe;
import studio.fantasyit.maid_storage_manager.storage.Target;
import studio.fantasyit.maid_storage_manager.util.StorageAccessUtil;

import java.util.List;
import java.util.Map;

public class GeneratorTACZ implements IAutoCraftGuideGenerator {
//    private UUID FROM_INGREDIENT_UUID = UUID.randomUUID();

    @Override
    public @NotNull ResourceLocation getType() {
        return TaczType.TYPE;
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof GunSmithTableBlockEntity;
    }

    @Override
    public boolean allowMultiPosition() {
        return true;
    }

    @Override
    public void generate(List<InventoryItem> inventory, Level level, BlockPos pos, ICachableGeneratorGraph graph, Map<ResourceLocation, List<BlockPos>> recognizedTypePositions) {
        StorageAccessUtil.Filter posFilter = GenerateCondition.getFilterOn(level, pos);
        ResourceLocation blockId = TaczRecipe.getBlockId(level, pos);
        List<RecipeHolder<GunSmithTableRecipe>> allRecipesForBlockId = TaczRecipe.getAllRecipesForBlockId(level, blockId);
        graph.addSpecialCraftNode(id -> new SpecialCraftNode(id) {
            @Override
            public void buildGraph(ICachableGeneratorGraph graph) {
                //法1
//                for (Node node : graph.getNodes()) {
//                    if (node instanceof ItemNode in && (in.itemStack.is(ModItems.AMMO) || in.itemStack.is(ModItems.MODERN_KINETIC_GUN))) {
//                        addEdge(in, 1);
//                    }
//                }
                //法2
                allRecipesForBlockId.forEach(holder -> {
                    ItemNode in = graph.getItemNodeOrCreate(holder.value().getOutput(), false);
                    //这个in可以缓存，在后续generate时使用
                    addEdge(in, 1);
                });

                //如有需要，也可以这样设置当前节点需要的前置原料
                //注意：请搜索FROM_INGREDIENT_UUID来了解获取方式。此处UUID不允许传入空值，必须通过cacheIngredient获取
//                IngredientNode ingredientNode = graph.addOrGetCahcedIngredientNode(Ingredient.of(ModItems.AMMO), FROM_INGREDIENT_UUID);
//                ingredientNode.addEdge(this, 1);
                //否则，请手动将本节点加入队列，表示该节点没有前置节点
                graph.addToQueue(this);
            }

            @Override
            public void generate(ICachableGeneratorGraph graph) {
                allRecipesForBlockId.forEach(holder -> {
                    GunSmithTableRecipe recipe = holder.value();
                    List<GunSmithTableIngredient> ingredients = recipe.getInputs();
                    if (!posFilter.isAvailable(recipe.getOutput()))
                        return;
                    if (!graph.getItemNodeOrCreate(recipe.getOutput(), false).related)
                        return;
                    CraftGuideStepData step = new CraftGuideStepData(
                            new Target(CraftingType.TYPE, pos),
                            ingredients.stream().map(t -> t.getIngredient().getItems()[0]).toList(),
                            List.of(recipe.getOutput()),
                            TaczType.TYPE,
                            ActionOptionSet.with(TaczRecipeAction.OPTION_TACZ_RECIPE_ID, true, holder.id().toString())
                                    .add(TaczRecipeAction.OPTION_TACZ_BLOCK_ID, true, blockId.toString())
                    );
                    graph.addCraftGuide(new CraftGuideData(
                            List.of(step),
                            TaczType.TYPE
                    ));
                });
            }

            @Override
            public String toString() {
                return "SpecialNode:TACZ#" + id;
            }
        });
    }

    @Override
    public void onCache(RecipeManager manager) {
//        FROM_INGREDIENT_UUID = RecipeIngredientCache.cacheIngredient(Ingredient.of(ModItems.AMMO));
    }

    @Override
    public Component getConfigName() {
        return Component.translatable("config.maid_storage_manager.crafting.generating.maid_storage_manager.work_bench");
    }
}
