package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.util.AnnotationHelper;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;

public interface ICropHarvest {
    public static final Set<Block> BLACK_LIST = new HashSet<>();

    /**
     * 是否是可收获的作物
     *
     * @param block 要检查的方块
     * @return 如果是可收获的作物，返回 true
     */
    boolean isFarmCrop(Block block);

    /**
     * 是否覆盖默认的收获行为
     *
     * @return 如果覆盖，返回 true
     */
    default boolean isOverWrite() {
        return false;
    }

    /**
     * 模式 ID，用于后续模式的判断，也用于本地化的 key
     *
     * @return 用 ResourceLocation 类描述的模式 ID
     */
    ResourceLocation getUid();


    /**
     * 检查是否可以移动到指定位置
     *
     * @param worldIn   服务器等级对象
     * @param maid      女仆对象
     * @param cropPos   作物所处的位置
     * @param pathFinding 路径查找对象
     * @param data      收获数据对象
     * @return 是否可以移动到指定位置
     */
    default Result canMoveTo(ServerLevel worldIn, EntityMaid maid, BlockPos cropPos, MaidPathFindingBFS pathFinding, HarvestData data) {
        if (hasAviShape(worldIn, maid, cropPos) && canHarvest(maid, cropPos, worldIn.getBlockState(cropPos)) != Result.FAILURE && checkPathReach(maid, pathFinding, cropPos, data)) {
            data.setCloseEnoughDist(this.getCloseEnoughDist());
            data.setRuleUid(this.getUid());
            return Result.SUCCESS;
        }
        return Result.FAILURE;
    }

    default boolean canPlantTo(ServerLevel worldIn, EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed, HarvestData data) {
        if (this.canPlant(maid, basePos, baseState, seed)) {
            data.setWalkAndLookPos(basePos);
            data.setCloseEnoughDist(this.getCloseEnoughDist());
            data.setRuleUid(this.getUid());
            return true;
        }
        return false;
    }

    /**
     * 当前位置方块是否可以收获
     *
     * @param maid      女仆对象
     * @param cropPos   作物所处的位置
     * @param cropState 作物的 BlockState
     * @return 是否可以收获
     */
    Result canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState);

    /**
     * 对应的收获行为
     *
     * @param maid      女仆对象
     * @param cropPos   作物所处的位置
     * @param cropState 作物的 BlockState
     */
    Result harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState);

    /**
     * 获取最小的种植、收获距离
     *
     * @return 距离
     */
    default double getCloseEnoughDist() {
        return 1.0;
    }

    /**
     * 是否检查作物上方有足够的空间
     *
     * @return 如果不检查，返回 false
     */
    default boolean hasAviShape(ServerLevel worldIn, EntityMaid maid, BlockPos cropPos) {
        BlockPos abovePos = cropPos.above(1);
        BlockState stateUp = worldIn.getBlockState(abovePos);
        return stateUp.getCollisionShape(worldIn, abovePos).isEmpty();
    }


    /**
     * 检查是否可以到达指定位置
     *
     * @param maid      女仆对象
     * @param pathFinding 路径查找对象
     * @param pos       目标位置
     * @param data      收获数据对象
     * @return 是否可以到达指定位置
     */
    default boolean checkPathReach(EntityMaid maid, MaidPathFindingBFS pathFinding, BlockPos pos, HarvestData data) {
        if (pathFinding.canPathReach(pos)) {
            data.setWalkAndLookPos(pos);
            return true;
        }
        return false;
    }

    /**
     * 是否是种子
     *
     * @param stack 物品栈
     * @return 如果是种子，返回 true
     */
    boolean isSeed(ItemStack stack);

    /**
     * 该位置是否可以种植作物
     *
     * @param maid      女仆对象
     * @param basePos   作物基底坐标
     * @param baseState 作物基底状态
     * @param seed      种植的种子
     * @return 是否可以种植
     */
    boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed);

    /**
     * 对应的种植行为
     *
     * @param maid      女仆对象
     * @param basePos   作物基底坐标
     * @param baseState 作物基底状态
     * @param seed      种植的种子
     * @return 种植后返回的物品
     */
    ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed);


    class Handler {
        public static final Map<Item, List<ICropHarvest>> BLOCK_PLANT_MAP = new HashMap<>();
        public static final Map<Block, List<ICropHarvest>> BLOCK_HARVESTS_MAP = new HashMap<>();

        public static final Map<ResourceLocation, ICropHarvest> HARVEST_MAP = new HashMap<>();


        public static void init() {
            clear();

            initCropHarvestRules();
            initBlockHarvestRules();
            initBlockPlantRules();
        }

        private static void clear() {
            BLOCK_HARVESTS_MAP.clear();
            BLOCK_PLANT_MAP.clear();

            HARVEST_MAP.clear();
        }

        private static void initBlockHarvestRules() {
            ForgeRegistries.BLOCKS.getValues().forEach(block -> {
                HARVEST_MAP.values().forEach(rule -> {
                    if (rule.isFarmCrop(block)) {
                        if (rule.isOverWrite()) {
                            BLOCK_HARVESTS_MAP.put(block, Lists.newArrayList(rule));
                        } else {
                            BLOCK_HARVESTS_MAP.computeIfAbsent(block, k -> new ArrayList<>()).add(rule);
                        }
                    }
                });
            });
        }

        private static void initBlockPlantRules() {
            ForgeRegistries.ITEMS.getValues().forEach(item -> {
                HARVEST_MAP.values().forEach(rule -> {
                    if (rule.isSeed(item.getDefaultInstance())) {
                        if (rule.isOverWrite()) {
                            BLOCK_PLANT_MAP.put(item, Lists.newArrayList(rule));
                        } else {
                            BLOCK_PLANT_MAP.computeIfAbsent(item, k -> new ArrayList<>()).add(rule);
                        }
                    }
                });
            });
        }

        private static void initCropHarvestRules() {
            AnnotationHelper.<ICropHarvest.AutoCropHarvestRegister, ICropHarvest>
                    readWithObj(ICropHarvest.AutoCropHarvestRegister.class, obj -> HARVEST_MAP.put(obj.getUid(), obj));
        }

        public static List<ICropHarvest> getRules(Block block) {
            return BLOCK_HARVESTS_MAP.getOrDefault(block, List.of());
        }

        public static ICropHarvest getRule(ResourceLocation uid) {
            return HARVEST_MAP.get(uid);
        }

        public static List<ICropHarvest> getRules(Item item) {
            return BLOCK_PLANT_MAP.getOrDefault(item, List.of());
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface AutoCropHarvestRegister {
    }

    enum Result {
        // 成功
        SUCCESS,
        // 失败
        FAILURE,
        // 跳过
        SKIP,
        NOT_DONE
    }

    class HarvestData {
        private BlockPos cropPos;
        private BlockPos walkPos;
        private BlockPos lookPos;
        private ResourceLocation ruleUid;
        private double closeEnoughDist;
        private Result result;

        public BlockPos getCropPos() {
            return cropPos;
        }

        public void setCropPos(BlockPos cropPos) {
            this.cropPos = cropPos;
        }

        public void setWalkAndLookPos(BlockPos walkPos, BlockPos lookPos) {
            this.walkPos = walkPos;
            this.lookPos = lookPos;
        }

        public void setWalkAndLookPos(BlockPos pos) {
            this.walkPos = pos;
            this.lookPos = pos;
        }

        public BlockPos getWalkPos() {
            return walkPos;
        }

        public void setWalkPos(BlockPos walkPos) {
            this.walkPos = walkPos;
        }

        public BlockPos getLookPos() {
            return lookPos;
        }

        public void setLookPos(BlockPos lookPos) {
            this.lookPos = lookPos;
        }

        public ResourceLocation getRuleUid() {
            return ruleUid;
        }

        public void setRuleUid(ResourceLocation ruleUid) {
            this.ruleUid = ruleUid;
        }

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }

        public double getCloseEnoughDist() {
            return closeEnoughDist;
        }

        public void setCloseEnoughDist(double closeEnoughDist) {
            this.closeEnoughDist = closeEnoughDist;
        }

        public HarvestData copy(){
            HarvestData data = new HarvestData();
            data.setCropPos(this.cropPos);
            data.setWalkAndLookPos(this.walkPos, this.lookPos);
            data.setRuleUid(this.ruleUid);
            data.setCloseEnoughDist(this.closeEnoughDist);
            data.setResult(this.result);
            return data;
        }
    }
}
