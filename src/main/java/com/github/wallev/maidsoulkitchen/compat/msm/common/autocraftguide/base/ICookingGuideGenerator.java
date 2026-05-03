package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonPickupItemAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.base.EnchantCommonTakeItemAction;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftActionTypes;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.action.TargetUtil;
import com.github.wallev.maidsoulkitchen.vhelper.client.chat.VComponent;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOption;
import studio.fantasyit.maid_storage_manager.craft.action.ActionOptionSet;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.generator.algo.ICachableGeneratorGraph;
import studio.fantasyit.maid_storage_manager.craft.generator.cache.RecipeIngredientCache;
import studio.fantasyit.maid_storage_manager.craft.generator.type.base.IAutoCraftGuideGenerator;
import studio.fantasyit.maid_storage_manager.craft.generator.util.GenerateCondition;
import studio.fantasyit.maid_storage_manager.data.InventoryItem;
import studio.fantasyit.maid_storage_manager.registry.ItemRegistry;
import studio.fantasyit.maid_storage_manager.storage.ItemHandler.ItemHandlerStorage;
import studio.fantasyit.maid_storage_manager.storage.Target;
import studio.fantasyit.maid_storage_manager.util.PosUtil;
import studio.fantasyit.maid_storage_manager.util.StorageAccessUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ICookingGuideGenerator<R> extends IAutoCraftGuideGenerator {
    CraftActionTypes CRAFT_ACTION_TYPE = CraftActionTypes.INSTANCE;
    Item DEFAULT_ITEM = ItemStack.EMPTY.getItem();


    /**
     * 检查位置为适合盛水的地方。
     *
     * @param level 世界
     * @param pos   位置
     * @return 是否合法
     */
    default boolean isValidWaterBlock(Level level, BlockPos pos) {
        if (!level.getBlockState(pos).is(Blocks.WATER)) return false;
        MutableInt count = new MutableInt(0);
        PosUtil.findAround(pos, t -> {
            if (level.getBlockState(t).is(Blocks.WATER))
                count.increment();
            return null;
        });
        return count.intValue() >= 3;
    }

    /**
     * 检查位置为女仆当前的位置
     *
     * @param level 世界
     * @param pos   位置
     * @return 是否合法
     */
    default boolean isSameMaidPos(EntityMaid maid, BlockPos pos) {
        return maid.blockPosition.equals(pos);
    }

    /**
     * 检查位置为地面。
     *
     * @param level 世界
     * @param pos   位置
     * @return 是否合法
     */
    default boolean isValidGroundBlock(Level level, BlockPos pos) {
        if (level.getBlockState(pos).isCollisionShapeFullBlock(level, pos)) {
            if (StorageAccessUtil.getMarksForPosSet(level, Target.virtual(pos, null), List.of(pos.east(), pos.west(), pos.north(), pos.south()))
                    .stream()
                    .map(Pair::getB)
                    .anyMatch(t -> t.is(ItemRegistry.ALLOW_ACCESS.get()))) {
                if (PosUtil.findInUpperSquare(pos.above(), 1, 3, 1, t -> (level.getBlockState(t).isAir() ? null : true)) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查位置是否合法。用于判断和世界相关的成立条件。
     *
     * @param level       世界
     * @param maid        女仆
     * @param pos         位置
     * @param pathFinding 路径查找
     * @return 是否合法
     */
    boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding);

    @Override
    default boolean positionalAvailable(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return isValidBlockInWorld(level, maid, pos, pathFinding) && IAutoCraftGuideGenerator.super.positionalAvailable(level, maid, pos, pathFinding);
    }

    /**
     * 获取合成指南生成器类型。建议取RecipeType或者CraftType
     *
     * @return 合成指南生成器类型
     */
    @Override
    @NotNull
    ResourceLocation getType();

    default String toTypeStr() {
        String craftGuideLang = this.getType().toString().replace(":", ".").replace("/", "");
        return craftGuideLang;
    }

    /**
     * 生成配方。生成时请调用 GeneratorGraph#addNode() 添加配方。当配方被认为需要时，将会调用最后一个参数生成合成指南。
     *
     * @param inventory               女仆仓库
     * @param level                   世界
     * @param pos                     位置
     * @param graph                   生成图
     * @param recognizedTypePositions 已经生成过的生成器类型和位置
     */
    @Override
    default void generate(List<InventoryItem> inventory, Level level, BlockPos pos, ICachableGeneratorGraph graph, Map<ResourceLocation, List<BlockPos>> recognizedTypePositions) {
        StorageAccessUtil.Filter posFilter = GenerateCondition.getFilterOn(level, pos);
        RegistryAccess registryAccess = level.registryAccess();

        consumeRecipes(level.getRecipeManager(), (recipe) -> {
            if (!isValidRecipe(recipe))
                return;

            List<ItemStack> outputs = this.getOutputs(recipe, registryAccess);
            if (!posFilter.isAvailable(outputs.get(0)))
                return;

            generateCraftGuide(level, pos, graph, recipe, outputs);
        });
    }

    /**
     * 生成合成指南
     *
     * @param level   世界
     * @param pos     位置
     * @param graph   生成图
     * @param recipe  配方
     * @param outputs 输出物品
     */
    default void generateCraftGuide(Level level, BlockPos pos, ICachableGeneratorGraph graph, R recipe, List<ItemStack> outputs) {
        List<Ingredient> inputs = Lists.newArrayList(this.getInputs(recipe));
        List<Ingredient> containers = getContainers(recipe);
        boolean needContainer = !containers.isEmpty();
        if (needContainer) {
            inputs.addAll(containers);
        }
        List<Integer> inputCounts = this.getInputCounts(inputs);

        ResourceLocation recipeId = getRecipeId(recipe);
        List<Ingredient> allInputs = getAllInputs(recipe);

        Function<List<ItemStack>, CraftGuideData> craftGuideSupplier = items -> {
            CraftGuideOperator2 craftGuide = CraftGuideOperator2.create(pos);
            generateCraftGuideSteps(level, pos, recipe, outputs, items, needContainer, craftGuide);
            CraftGuideData craftGuideData = craftGuide.makeCraftGuideData();
            return craftGuideData;
        };

        graph.addRecipe(
                recipeId,
                allInputs,
                inputCounts,
                outputs,
                craftGuideSupplier
        );
    }

    /**
     * 生成合成指南步骤
     *
     * @param level         世界
     * @param pos           位置
     * @param recipe        配方
     * @param outputs       输出物品
     * @param items         输入物品
     * @param needContainer 是否需要容器
     * @param craftGuide    合成指南
     */
    default void generateCraftGuideSteps(Level level, BlockPos pos, R recipe, List<ItemStack> outputs, List<ItemStack> items, boolean needContainer, CraftGuideOperator2 craftGuide) {
        List<ItemStack> realItems = new ArrayList<>(items);
        List<ItemStack> remains = this.getRemains(recipe, realItems)
                .stream().filter(itemStack -> !itemStack.isEmpty()).toList();

        List<ItemStack> oContainers = new ArrayList<>();
        if (needContainer) {
            int count = outputs.get(0).getCount();
            ItemStack remove = realItems.remove(items.size() - 1);
            oContainers.add(matchResultCount() ? remove.copyWithCount(count) : remove);
        }

        generateSteps(pos, level, recipe, craftGuide, realItems, needContainer, oContainers, outputs, remains);
    }

    default boolean matchResultCount() {
        return false;
    }

    /**
     * 生成合成指南
     *
     * @param pos           位置
     * @param level         世界
     * @param recipe        配方
     * @param craftGuide    合成指南
     * @param realItems     输入物品
     * @param needContainer 是否需要容器
     * @param containers    容器物品
     * @param outputs       输出物品
     * @param remains       剩余物品
     */
    default void generateSteps(BlockPos pos, Level level, R recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        // 置入 input 物品
        inputsStep(pos, craftGuide, realItems);

        // 置入 container 物品
        outputContainerStep(pos, needContainer, craftGuide, containers);

        // 等待产物
        waitToOutputStep(pos, craftGuide, realItems, recipe);

        // 取出 output 物品
        outputsStep(pos, craftGuide, outputs);

        // 取出 remain 物品
        remainStep(pos, craftGuide, remains);
    }


    /**
     * 输入物品合成步骤
     *
     * @param pos        位置
     * @param craftGuide 合成指南
     * @param realItems  输入物品
     */
    default void inputsStep(BlockPos pos, CraftGuideOperator2 craftGuide, List<ItemStack> realItems) {
        CraftGuideOperator2.forEach3Items(realItems, is -> {
            craftGuide.addItemInsert(this.getInputStorageType(), Direction.UP, is);
        });
    }

    /**
     * 获取输入物品容器存储类型
     *
     * @return 容器存储类型
     */
    default ResourceLocation getInputStorageType() {
        return ItemHandlerStorage.TYPE;
    }

    /**
     * 输出容器物品合成步骤
     *
     * @param pos           位置
     * @param needContainer 是否需要容器
     * @param craftGuide    合成指南
     * @param containers    容器物品
     */
    default void outputContainerStep(BlockPos pos, boolean needContainer, CraftGuideOperator2 craftGuide, List<ItemStack> containers) {
        if (needContainer) {
            CraftGuideOperator2.forEach3Items(containers, is -> {
                craftGuide.addItemInsert(this.getOutputContainerStorageType(), Direction.EAST, is);
            });
        }
    }

    /**
     * 获取输出物品餐具容器存储类型
     *
     * @return 餐具容器存储类型
     */
    default ResourceLocation getOutputContainerStorageType() {
        return ItemHandlerStorage.TYPE;
    }

    /**
     * 等待产物合成步骤
     *
     * @param pos        位置
     * @param craftGuide 合成指南
     * @param realItems  输入物品
     * @param recipe     配方
     */
    default void waitToOutputStep(BlockPos pos, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, R recipe) {
        craftGuide.addIdle(this.getRecipeTime(recipe) + fixTime());
    }

    default int fixTime() {
        return 5;
    }

    /**
     * 输出物品合成步骤
     *
     * @param pos        位置
     * @param craftGuide 合成指南
     * @param outputs    输出物品
     */
    default void outputsStep(BlockPos pos, CraftGuideOperator2 craftGuide, List<ItemStack> outputs) {
        CraftGuideOperator2.forEach3Items(outputs, is -> {
            craftGuide.addItemTake(this.getOutputStorageType(), Direction.DOWN, is);
        });
    }

    /**
     * 获取输出物品餐具容器存储类型
     *
     * @return 餐具容器存储类型
     */
    default ResourceLocation getOutputStorageType() {
        return ItemHandlerStorage.TYPE;
    }

    /**
     * 剩余物品合成步骤
     *
     * @param pos        位置
     * @param craftGuide 合成指南
     * @param remains    剩余物品
     */
    default void remainStep(BlockPos pos, CraftGuideOperator2 craftGuide, List<ItemStack> remains) {
    }

    default void remainPickupStep(BlockPos pos, CraftGuideOperator2 craftGuide, List<ItemStack> remains) {
        CraftGuideOperator2.forEach3Items(remains, is -> {
            craftGuide.addStep(new CraftGuideStepData(
                    TargetUtil.makeTargetVirtualNoSide(pos),
                    List.of(),
                    is,
                    EnchantCommonPickupItemAction.TYPE,
                    ActionOptionSet.with(ActionOption.OPTIONAL, true)
            ));
        });
    }

    default void remainTakeStep(BlockPos pos, CraftGuideOperator2 craftGuide, List<ItemStack> remains) {
        CraftGuideOperator2.forEach3Items(remains, craftGuide::addItemTake);
    }

    default ResourceLocation getRemainStorageType() {
        return ItemHandlerStorage.TYPE;
    }


    /**
     * 获取配方消耗时间
     *
     * @param recipe 配方
     * @return 时间
     */
    int getRecipeTime(R recipe);

    /**
     * 获取input后的remainItem。
     *
     * @param recipe 配方
     * @param inputs 输入
     * @return 剩余物品
     */
    default List<ItemStack> getRemains(R recipe, List<ItemStack> inputs) {
        return List.of();
    }

    /**
     * 获取输入计数。
     *
     * @param inputs 输入
     * @return 输入计数
     */
    default List<Integer> getInputCounts(List<Ingredient> inputs) {
        List<Integer> inputCounts = inputs
                .stream()
                .map(t -> Arrays.stream(t.getItems()).findFirst().map(ItemStack::getCount).orElse(1))
                .toList();

        return inputCounts;
    }

    /**
     * 缓存回调。缓存发生在数据包加载后。你可以在此为配方添加缓存。<b>缓存的配方原料必须和下次添加同ID配方时完全一致，否则可能出现不可预料的错误</b>
     *
     * @param manager 配方管理器
     */
    @Override
    default void onCache(RecipeManager manager) {
        consumeRecipes(manager, (recipe) -> {
            if (!shouldCacheRecipe(recipe))
                return;
            if (!isValidRecipe(recipe))
                return;

            ResourceLocation recipeId = getRecipeId(recipe);
            List<Ingredient> allInputs = getAllInputs(recipe);

            RecipeIngredientCache.addRecipeCache(recipeId, allInputs);
        });
    }

    /**
     * 消耗配方。用于缓存配方。<b>缓存的配方信息必须和下次添加同ID配方时完全一致，否则可能出现不可预料的错误</b>
     *
     * @param manager        配方管理器
     * @param recipeConsumer 配方消费者
     */
    void consumeRecipes(RecipeManager manager, Consumer<R> recipeConsumer);

    /**
     * 是否合法配方。用于缓存配方和生成步骤配方。
     *
     * @param inventory 女仆仓库
     * @param level     世界
     * @param pos       位置
     * @param recipe    配方
     * @return 是否合法配方
     */
    default boolean isValidRecipe(List<InventoryItem> inventory, Level level, BlockPos pos, R recipe) {
        return isValidRecipe(recipe);
    }

    /**
     * 是否合法配方。用于缓存配方和生成步骤配方。
     *
     * @param recipe 配方
     * @return 是否合法配方
     */
    default boolean isValidRecipe(R recipe) {
        return true;
    }

    /**
     * 是否缓存配方。用于缓存配方。
     *
     * @param recipe 配方
     * @return 是否缓存配方
     */
    default boolean shouldCacheRecipe(R recipe) {
        return true;
    }

    /**
     * 获取配方ID。用于缓存配方。<b>缓存的配方ID必须和下次添加同ID配方时完全一致，否则可能出现不可预料的错误</b>
     *
     * @param recipe 配方
     * @return 配方ID
     */
    ResourceLocation getRecipeId(R recipe);

    /**
     * 获取配方所有输入。用于缓存配方。<b>缓存的配方所有输入必须和下次添加同ID配方时完全一致，否则可能出现不可预料的错误</b>
     *
     * @param recipe 配方
     * @return 配方所有输入
     */
    default List<Ingredient> getAllInputs(R recipe) {
        List<Ingredient> inputs = getInputs(recipe);
        List<Ingredient> containers = getContainers(recipe);

        if (!containers.isEmpty()) {
            List<Ingredient> allInputs = new ArrayList<>(inputs);
            allInputs.addAll(containers);
            return allInputs;
        } else {
            return inputs;
        }
    }

    /**
     * 获取配方输入。用于缓存配方。<b>缓存的配方输入必须和下次添加同ID配方时完全一致，否则可能出现不可预料的错误</b>
     *
     * @param recipe 配方
     * @return 配方输入
     */
    List<Ingredient> getInputs(R recipe);

    /**
     * 获取配方容器。用于缓存配方。<b>缓存的配方容器必须和下次添加同ID配方时完全一致，否则可能出现不可预料的错误</b>
     *
     * @param recipe 配方
     * @return 配方容器
     */
    default List<Ingredient> getContainers(R recipe) {
        return List.of();
    }

    /**
     * 获取配方输出。用于缓存配方。<b>缓存的配方输出必须和下次添加同ID配方时完全一致，否则可能出现不可预料的错误</b>
     *
     * @param recipe 配方
     * @return 配方输出
     */
    List<ItemStack> getOutputs(R recipe, RegistryAccess registryAccess);

    /**
     * 获取合成指南生成器名称。用于显示在配置文件中
     *
     * @return 合成指南生成器名称
     */
    @Override
    default Component getConfigName() {
        String finalKey = "";
        String translateKey = this.getTranslateKey();
        if (I18n.exists(translateKey)) {
            finalKey = translateKey;
        }

        String recipeIdFromJei = this.getRecipeTranslateKeyFromJei();
        if (finalKey.isEmpty() && !recipeIdFromJei.isEmpty()) {
            finalKey = recipeIdFromJei;
        }

        String descriptionId = this.getBlockItemForTranslate().getDescriptionId();
        if (finalKey.isEmpty() && !descriptionId.isEmpty()) {
            finalKey = descriptionId;
        }

        return VComponent.translatable(finalKey);
    }

    default String getRecipeTranslateKeyFromJei() {
        // JEI
        String case0 = String.format("gui.jei.category.%s", this.getType().toString().replaceAll(":", "."));
        if (I18n.exists(case0)) {
            return case0;
        }
        // Caupona
        String case01 = String.format("gui.jei.category.%s.title", this.getType().toString().replaceAll(":", "."));
        if (I18n.exists(case01)) {
            return case01;
        }
        // Create
        String case02 = String.format("%s.recipe.%s", this.getType().getNamespace(), this.getType().getPath());
        if (I18n.exists(case02)) {
            return case02;
        }
        // 农夫系列
        String case1 = String.format("%s.jei.%s", this.getType().getNamespace(), this.getType().getPath());
        if (I18n.exists(case1)) {
            return case1;
        }
        // drinkbeer
        String case2 = String.format("%s.jei.title.%s", this.getType().getNamespace(), this.getType().getPath());
        if (I18n.exists(case2)) {
            return case2;
        }
        // 车万女仆
        String case3 = String.format("jei.%s.title", this.getType().toString().replaceAll(":", "."));
        if (I18n.exists(case3)) {
            return case3;
        }
        return "";
    }

    default String getTranslateKey() {
        String craftGuideLang = this.getType().toString().replace(":", ".").replace("/", "");
        return  "config.maid_storage_manager.crafting.generating." + craftGuideLang;
    }

    default List<Ingredient> toIngredients(ItemStack... itemStack) {
        return Arrays.stream(itemStack)
                .filter(itemStack1 -> !itemStack1.isEmpty())
                .map(Ingredient::of)
                .toList();
    }

    default List<ItemStack> toSpilt(List<ItemStack> items) {
        List<ItemStack> spiltItems = new ArrayList<>();
        for (ItemStack item : items) {
            for (int i = 0; i < item.getCount(); i++) {
                spiltItems.add(item.copyWithCount(1));
            }
        }
        return spiltItems;
    }

    Item getBlockItemForTranslate();
//    default Item getBlockItemForTranslate() {
//        return ItemStack.EMPTY.getItem();
//    }
}
