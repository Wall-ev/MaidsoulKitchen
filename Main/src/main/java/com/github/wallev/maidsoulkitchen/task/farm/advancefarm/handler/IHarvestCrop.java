package com.github.wallev.maidsoulkitchen.task.farm.advancefarm.handler;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm.ai.CropResult;
import com.github.wallev.maidsoulkitchen.util.AnnotationHelper;
import com.github.wallev.maidsoulkitchen.util.fakeplayer.WrappedMaidFakePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Predicate;

import static com.github.wallev.maidsoulkitchen.vhelper.IModInfo.LOGGER;

public abstract class IHarvestCrop {
    static Set<Block> BLACK_LIST = new HashSet<>();

    public abstract ResourceLocation getUid();

    public abstract boolean isFarmCrop(Block block);

    /**
     *
     * @param maid
     * @param cropPos
     * @param cropState
     * @param pathFinding
     * @return
     */
    public final boolean checkAndGetPos(EntityMaid maid, BlockPos cropPos, BlockState cropState, CropResult cropResult, MaidPathFindingBFS pathFinding) {
        return this.canHarvest(maid, cropPos, cropState, cropResult).isSuccess() && this.canPathReach(pathFinding, cropPos, cropResult);
    }

    public boolean canPathReach(MaidPathFindingBFS pathFindingBFS, BlockPos targetPos, CropResult cropResult) {
        if (pathFindingBFS.canPathReach(targetPos)) {
            cropResult.setToMovePos(targetPos);
            return true;
        }
        return false;
    }

    /**
     * 传入的 ItemStack 对象是否可以作为种子
     *
     * @param stack ItemStack
     * @return boolean
     */
    public abstract boolean isSeed(ItemStack stack);

    /**
     * 当前位置方块是否可以收获
     *
     * @param maid      女仆对象
     * @param cropPos   作物所处的位置
     * @param cropState 作物的 BlockState
     * @return 是否可以收获
     */
    public abstract Result canHarvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, CropResult cropResult);

    /**
     * 对应的收获行为
     *
     * @param maid      女仆对象
     * @param cropPos   作物所处的位置
     * @param cropState 作物的 BlockState
     */
    public abstract Result harvest(EntityMaid maid, BlockPos cropPos, BlockState cropState, CropResult cropResult);

    /**
     * 该位置是否可以种植作物
     *
     * @param maid      女仆对象
     * @param basePos   作物基底坐标
     * @param baseState 作物基底状态
     * @param seed      种植的种子
     * @return 是否可以种植
     */
    public abstract boolean canPlant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed, CropResult cropResult);

    /**
     * 对应的种植行为
     *
     * @param maid      女仆对象
     * @param basePos   作物基底坐标
     * @param baseState 作物基底状态
     * @param seed      种植的种子
     * @return 种植后返回的物品
     */
    public abstract ItemStack plant(EntityMaid maid, BlockPos basePos, BlockState baseState, ItemStack seed, CropResult cropResult);

    /**
     * 获取最小的种植、收获距离
     *
     * @return 距离
     */
    public double getCloseEnoughDist() {
        return 1.0;
    }

    /**
     * 是否检查作物上方有足够的空间
     *
     * @return 如果不检查，返回 false
     */
    public boolean checkCropPosAbove() {
        return true;
    }


    protected final boolean harvestWithTool(EntityMaid maid, BlockPos cropPos, BlockState cropState, Predicate<ItemStack> predicate, CropResult cropResult) {
        if (this.canHarvest(maid, cropPos, cropState, cropResult).isSuccess()) {
            ItemStack toolStack = ItemsUtil.getStack(maid.getAvailableInv(true), predicate);
            if (!toolStack.isEmpty()) {
                InteractionResult result = WrappedMaidFakePlayer.get(maid).useOnByItem(cropPos, toolStack);
                if (result == InteractionResult.PASS) {
                    BLACK_LIST.add(cropState.getBlock());
                    LOGGER.warn(BLACK_LIST.toString());
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    protected final boolean harvestWithoutTool(EntityMaid maid, BlockPos cropPos, BlockState cropState, CropResult cropResult) {
        if (this.canHarvest(maid, cropPos, cropState, cropResult).isSuccess()) {
            InteractionResult result = WrappedMaidFakePlayer.get(maid).useOnByHand(cropPos);
            if (result == InteractionResult.PASS) {
                BLACK_LIST.add(cropState.getBlock());
                LOGGER.warn(BLACK_LIST.toString());
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean checkPathReach(EntityMaid maid, MaidPathFindingBFS pathFinding, BlockPos pos) {
        return pathFinding.canPathReach(pos);
    }

    public static class Handler {

        static final Map<Block, List<IHarvestCrop>> MAP = new HashMap<>();

        static final Map<ResourceLocation, IHarvestCrop> RULE = new HashMap<>();

        public static void init() {
            initCropHarvestRules();
            initBlockHarvestRules();
        }

        private static void initBlockHarvestRules() {
            ForgeRegistries.BLOCKS.getValues().forEach(block -> {
                RULE.values().forEach(rule -> {
                    if (rule.isFarmCrop(block)) {
                        MAP.computeIfAbsent(block, k -> new ArrayList<>()).add(rule);
                    }
                });
            });
        }

        private static void initCropHarvestRules() {
            AnnotationHelper.read(AutoCropHarvestRegister.class, annotationData -> {
                try {
                    String clazzName = annotationData.memberName();
                    Class<?> asmClazz = Class.forName(clazzName);
                    Constructor<?> constructor = asmClazz.getDeclaredConstructor();
                    IHarvestCrop o = (IHarvestCrop) constructor.newInstance();
                    RULE.put(o.getUid(), o);
                } catch (ClassNotFoundException | InvocationTargetException |
                         InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException ignored) {
                }
            });
        }


        public static List<IHarvestCrop> getRules(Block block) {
            return MAP.getOrDefault(block, List.of());
        }

        public static IHarvestCrop getRule(ResourceLocation uid) {
            return RULE.get(uid);
        }
    }

    public enum Result {
        SUCCESS,
        FAIL,
        PASS,

        ;
        public boolean isSuccess() {
            return this == SUCCESS;
        }

        public boolean isFail() {
            return this == FAIL;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface AutoCropHarvestRegister {
    }
}
