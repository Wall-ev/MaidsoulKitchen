package com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.clickharvest;

import com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.RightClickCropHarvest;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ClickHarvestHandler {
    public static Set<ResourceLocation> CLICK_HARVESTS = new HashSet<>();
    public static Set<Block> CLICK_BLOCK_HARVESTS = new HashSet<>();

    public static void init() {
        ForgeRegistries.BLOCKS.getValues().forEach(block -> {
            BlockState defaultBlockState = block.defaultBlockState();
            if (!RightClickCropHarvest.hasAgeProperty(defaultBlockState)) {
                return;
            }

//            BlockUseMethodDetector.
//
            // 获取SweetBerryBushBlock类的use方法
//            try {
//                block.getClass().getDeclaredMethod(
//                        "use",
//                        BlockState.class,
//                        net.minecraft.world.level.Level.class,
//                        net.minecraft.core.BlockPos.class,
//                        net.minecraft.world.entity.player.Player.class,
//                        net.minecraft.world.InteractionHand.class,
//                        net.minecraft.world.phys.BlockHitResult.class
//                );
//            } catch (NoSuchMethodException ignored) {
//                return;
//            }

            // 检测方法内部是否调用了popResource
            boolean hasCall;
            try {
                hasCall = new BlockUseMethodDetectorVER1().detectPopResourceCall(block.getClass());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (hasCall) {
                System.out.println(block.getClass() + " " + ForgeRegistries.BLOCKS.getKey(block) + ".use()方法内部" + "包含" + "popResource调用");
                CLICK_HARVESTS.add(ForgeRegistries.BLOCKS.getKey(block));
                CLICK_BLOCK_HARVESTS.add(block);
            }
        });
    }

//    public static void init() {
//        ForgeRegistries.BLOCKS.getValues().forEach(block -> {
//            BlockState defaultBlockState = block.defaultBlockState();
//            if (!RightClickCropHarvest.hasAgeProperty(defaultBlockState)) {
//                return;
//            }
//
//            // 获取SweetBerryBushBlock类的use方法
//            Method useMethod = null;
//            try {
//                useMethod = block.getClass().getDeclaredMethod(
//                        "use",
//                        BlockState.class,
//                        net.minecraft.world.level.Level.class,
//                        net.minecraft.core.BlockPos.class,
//                        net.minecraft.world.entity.player.Player.class,
//                        net.minecraft.world.InteractionHand.class,
//                        net.minecraft.world.phys.BlockHitResult.class
//                );
//            } catch (NoSuchMethodException ignored) {
//                return;
//            }
//
//            // 检测方法内部是否调用了popResource
//            boolean hasCall = false;
//            try {
//                hasCall = AsmMethodCallDetector.detectPopResourceCall(useMethod);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            System.out.println(block.getClass() + ".use()方法内部" + (hasCall ? "包含" : "不包含") + "popResource调用");
//
//            if (hasCall) {
//                CLICK_HARVESTS.add(ResourceLocation.tryParse(block.getDescriptionId()));
//            }
//        });
//    }


}
