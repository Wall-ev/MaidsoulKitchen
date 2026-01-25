package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.fruitharvest;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.lib.auto.event.AutoEventSubscriber;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.ICropHarvest;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.RightClickCropHarvest;
import com.github.wallev.maidsoulkitchen.util.InvUtil;
import com.google.common.collect.Lists;
import knightminer.simplytea.block.TeaTrunkBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.satisfy.vinery.core.block.CherryLeavesBlock;
import net.satisfy.vinery.core.block.StemBlock;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@AutoEventSubscriber
public class FruitHarvestHandler {
    public static Set<Block> FRUIT_BLOCKS = new HashSet<>();
    public static Map<Block, FruitCropHarvest> FRUIT_BLOCK_HANDLERS = new HashMap<>();

    @SuppressWarnings("unchecked")
    private static <T extends Block> boolean addFruitBlockWithDestroy(Block block,
                                                                      Predicate<Block> blockPredicate,
                                                                      IFruitCropCanHarvest<T> cropCanHarvest,
                                                                      ResourceLocation harvestId) {

        if (blockPredicate.test(block)) {
            System.out.println(block.getClass() + " " + ForgeRegistries.BLOCKS.getKey(block) + " 是果实方块");
            FRUIT_BLOCKS.add(block);
            FruitCropHarvest fruitCropHarvest = new FruitCropHarvest(
                    (maid, cropPos, cropState) -> cropCanHarvest.canHarvest((T) block, maid, cropPos, cropState),
                    FruitCropHarvest.IFruitCropHarvest.DestroyHarvestRule.INSTANCE,
                    harvestId
            );
            FRUIT_BLOCK_HANDLERS.put(block, fruitCropHarvest);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Block> boolean addFruitBlockWithEmptyClick(Block block,
                                                                   Predicate<Block> blockPredicate,
                                                                   IFruitCropCanHarvest<T> cropCanHarvest,
                                                                   ResourceLocation harvestId) {

        if (blockPredicate.test(block)) {
            System.out.println(block.getClass() + " " + ForgeRegistries.BLOCKS.getKey(block) + " 是果实方块");
            FRUIT_BLOCKS.add(block);
            FruitCropHarvest fruitCropHarvest = new FruitCropHarvest(
                    (maid, cropPos, cropState) -> cropCanHarvest.canHarvest((T) block, maid, cropPos, cropState),
                    FruitCropHarvest.IFruitCropHarvest.EmptyHarvestRule.INSTANCE,
                    harvestId
            );
            FRUIT_BLOCK_HANDLERS.put(block, fruitCropHarvest);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Block> boolean addFruitBlockWithToolClick(Block block,
                                                                   Predicate<Block> blockPredicate,
                                                                   IFruitCropCanHarvest<T> cropCanHarvest,
                                                                   Item tool,
                                                                   ResourceLocation harvestId) {

        if (blockPredicate.test(block)) {
            System.out.println(block.getClass() + " " + ForgeRegistries.BLOCKS.getKey(block) + " 是果实方块");
            FRUIT_BLOCKS.add(block);
            FruitCropHarvest fruitCropHarvest = new FruitCropHarvest(
                    (maid, cropPos, cropState) -> InvUtil.hasStack(maid.getAvailableInv(true), tool) ? cropCanHarvest.canHarvest((T) block, maid, cropPos, cropState) : ICropHarvest.Result.FAILURE,
                    FruitCropHarvest.IFruitCropHarvest.ToolHarvestRule.of(tool),
                    harvestId
            );
            FRUIT_BLOCK_HANDLERS.put(block, fruitCropHarvest);
            return true;
        }
        return false;
    }

    public interface IFruitCropCanHarvest<T extends Block> {
        ICropHarvest.Result canHarvest(T block, EntityMaid maid, BlockPos cropPos, BlockState cropState);
    }

    public static Map<Block, AgeProperty> AGE_PROPERTIES = new HashMap<>();
    public record AgeProperty(IntegerProperty ageProperty, int maxAge) {
        public boolean isMature(BlockState cropState) {
            return cropState.getValue(ageProperty) >= maxAge;
        }
    }
    public static void addAgeProperty(Block block) {
        block.defaultBlockState().getProperties().stream()
                .filter(p -> p instanceof IntegerProperty)
                .map(p -> (IntegerProperty) p)
                .filter(p -> p.getName().equals("age"))
                .findFirst()
                .ifPresent(ageProperty -> {
                    int maxAge = ageProperty.getPossibleValues().stream().max(Integer::compareTo).orElse(0);
                    AGE_PROPERTIES.put(block, new AgeProperty(ageProperty, maxAge));
                });
    }

    public static void init() {
        FRUIT_BLOCKS.clear();
        if (AGE_PROPERTIES == null) {
            AGE_PROPERTIES = new HashMap<>();
        }
        AGE_PROPERTIES.clear();


        ForgeRegistries.BLOCKS.getValues().forEach(block -> {
            if (FruitHarvestHandler.<com.teammoeg.caupona.blocks.plants.FruitBlock>addFruitBlockWithDestroy(block,
                    b -> b instanceof com.teammoeg.caupona.blocks.plants.FruitBlock,
                    (b, maid, cropPos, cropState) -> cropState.getValue(com.teammoeg.caupona.blocks.plants.FruitBlock.AGE) >= b.getMaxAge() ? ICropHarvest.Result.SUCCESS : ICropHarvest.Result.FAILURE,
                    new ResourceLocation(MaidsoulKitchen.MOD_ID, "fruit_crop_harvest_with_max_age_destroy")
            ))
                return;

            if (FruitHarvestHandler.<net.satisfy.vinery.core.block.AppleLeavesBlock>addFruitBlockWithEmptyClick(block,
                    b -> b instanceof net.satisfy.vinery.core.block.AppleLeavesBlock,
                    (b, maid, cropPos, cropState) -> cropState.getValue(net.satisfy.vinery.core.block.AppleLeavesBlock.HAS_APPLES) ? ICropHarvest.Result.SUCCESS : ICropHarvest.Result.FAILURE,
                    new ResourceLocation(MaidsoulKitchen.MOD_ID, "fruit_crop_harvest_with_vinery_apple")
            ))
                return;

            if (FruitHarvestHandler.<net.satisfy.vinery.core.block.CherryLeavesBlock>addFruitBlockWithEmptyClick(block,
                    b -> b instanceof net.satisfy.vinery.core.block.CherryLeavesBlock,
                    (b, maid, cropPos, cropState) -> cropState.getValue(CherryLeavesBlock.HAS_CHERRIES) ? ICropHarvest.Result.SUCCESS : ICropHarvest.Result.FAILURE,
                    new ResourceLocation(MaidsoulKitchen.MOD_ID, "fruit_crop_harvest_with_vinery_cherry")
            ))
                return;

            if (block instanceof StemBlock) {
                System.out.println(block.getClass() + " " + ForgeRegistries.BLOCKS.getKey(block) + " 是果实方块");
                FRUIT_BLOCKS.add(block);
                FRUIT_BLOCK_HANDLERS.put(block, FruitCropHarvest.MAX_AGE_CLICK_HARVEST);
                addAgeProperty(block);
                return;
            }

            if (FruitHarvestHandler.<TeaTrunkBlock>addFruitBlockWithToolClick(block,
                    b -> b instanceof TeaTrunkBlock,
                    (b, maid, cropPos, cropState) -> !cropState.getValue(TeaTrunkBlock.CLIPPED) && cropState.getValue(TeaTrunkBlock.TYPE) != TeaTrunkBlock.TrunkType.STUMP ? ICropHarvest.Result.SUCCESS : ICropHarvest.Result.FAILURE,
                    Items.SHEARS,
                    new ResourceLocation(MaidsoulKitchen.MOD_ID, "fruit_crop_harvest_with_vinery_tea")
            ))
                return;

            if (FruitHarvestHandler.addFruitBlockWithEmptyClick(block,
                    b -> b instanceof CaveVines,
                    (b, maid, cropPos, cropState) -> cropState.getValue(CaveVines.BERRIES) ? ICropHarvest.Result.SUCCESS : ICropHarvest.Result.FAILURE,
                    new ResourceLocation(MaidsoulKitchen.MOD_ID, "fruit_crop_harvest_with_cave_vines")
            ))
                return;



            BlockState defaultBlockState = block.defaultBlockState();
            if (!RightClickCropHarvest.hasAgeProperty(defaultBlockState)) {
                return;
            }
            if (block instanceof LeavesBlock) {
                System.out.println(block.getClass() + " " + ForgeRegistries.BLOCKS.getKey(block) + " 是果实方块");
                FRUIT_BLOCKS.add(block);
                FRUIT_BLOCK_HANDLERS.put(block, FruitCropHarvest.MAX_AGE_CLICK_HARVEST);
                addAgeProperty(block);
                return;
            }
            // 检测方法内部是否调用了popResource
            boolean hasCall = false;
            try {
                hasCall = new FruitBlockDetector().isFruitBlock(block);
            } catch (IOException e) {
                e.printStackTrace();
//                throw new RuntimeException(e);
            }

            if (hasCall) {
                System.out.println(block.getClass() + " " + ForgeRegistries.BLOCKS.getKey(block) + " 是果实方块");
                FRUIT_BLOCKS.add(block);
                FRUIT_BLOCK_HANDLERS.put(block, FruitCropHarvest.MAX_AGE_CLICK_HARVEST);
                addAgeProperty(block);
            }
        });


        FRUIT_BLOCK_HANDLERS.forEach((block, fruitCropHarvest) -> {
            ICropHarvest.Handler.BLOCK_HARVESTS_MAP.put(block, Lists.newArrayList(fruitCropHarvest));
            if (!ICropHarvest.Handler.HARVEST_MAP.containsKey(fruitCropHarvest.getUid())) {
                ICropHarvest.Handler.HARVEST_MAP.put(fruitCropHarvest.getUid(), fruitCropHarvest);
            }
        });

//        new FruitCropHarvest(
//                new FruitCropHarvest.IFruitCropCanHarvest.IFruitCropProperty.BoolProperty(TeaTrunkBlock)
//        )
    }
//
//    public static class Helper {
//        public static boolean isFruitBlock(Block block) {
//
//        }
//    }

    @SubscribeEvent
    public static void onEvent(TagsUpdatedEvent event) {
        ICropHarvest.Handler.init();

        init();
        int a = 1;
    }
}
