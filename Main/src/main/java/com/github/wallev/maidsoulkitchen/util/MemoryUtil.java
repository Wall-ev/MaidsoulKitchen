package com.github.wallev.maidsoulkitchen.util;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.init.ModEntities;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;

public class MemoryUtil {

    public static void rememberMaidRecs(EntityMaid maid, LinkedList<MaidRec> maidRecs) {
//        maid.getBrain().setMemory(MkEntities.MAID_RECS.get(), maidRecs);
    }

    public static void eraseMaidRecs(EntityMaid maid) {
        maid.getBrain().eraseMemory(ModEntities.MAID_RECS.get());
    }

    public static List<MaidRec> getMaidRecs(EntityMaid maid) {
        return maid.getBrain().getMemory(ModEntities.MAID_RECS.get()).orElseGet(ArrayList::new);
    }

    public static void rememberHubInputInventory(EntityMaid maid, ItemInventory itemInventory) {
        maid.getBrain().setMemory(ModEntities.HUB_INPUT_INVENTORY.get(), itemInventory);
    }

    public static void eraseHubInputInventory(EntityMaid maid) {
        maid.getBrain().eraseMemory(ModEntities.HUB_INPUT_INVENTORY.get());
    }

    public static ItemInventory getHubInputInventory(EntityMaid maid) {
        return maid.getBrain().getMemory(ModEntities.HUB_INPUT_INVENTORY.get()).orElseGet(ItemInventory::new);
    }

    public static void rememberHubOutputInventory(EntityMaid maid, ItemInventory itemInventory) {
        maid.getBrain().setMemory(ModEntities.HUB_OUTPUT_INVENTORY.get(), itemInventory);
    }

    public static void eraseHubOutputInventory(EntityMaid maid) {
        maid.getBrain().eraseMemory(ModEntities.HUB_OUTPUT_INVENTORY.get());
    }

    public static ItemInventory getHubOutputInventory(EntityMaid maid) {
        return maid.getBrain().getMemory(ModEntities.HUB_OUTPUT_INVENTORY.get()).orElseGet(ItemInventory::new);
    }

    public static void rememberChestInputInventory(EntityMaid maid, ItemInventory itemInventory) {
//        maid.getBrain().setMemory(MkEntities.INPUT_CHEST_INVENTORY.get(), itemInventory);
    }

    public static void eraseChestInputInventory(EntityMaid maid) {
        maid.getBrain().eraseMemory(ModEntities.INPUT_CHEST_INVENTORY.get());
    }

    public static ItemInventory getChestInputInventory(EntityMaid maid) {
        return maid.getBrain().getMemory(ModEntities.INPUT_CHEST_INVENTORY.get()).orElseGet(ItemInventory::new);
    }

    public static void rememberChestOutputInventory(EntityMaid maid, ItemInventory itemInventory) {
        maid.getBrain().setMemory(ModEntities.OUTPUT_CHEST_INVENTORY.get(), itemInventory);
    }

    public static void eraseChestOutputInventory(EntityMaid maid) {
        maid.getBrain().eraseMemory(ModEntities.OUTPUT_CHEST_INVENTORY.get());
    }

    public static ItemInventory getChestOutputInventory(EntityMaid maid) {
        return maid.getBrain().getMemory(ModEntities.OUTPUT_CHEST_INVENTORY.get()).orElseGet(ItemInventory::new);
    }


    public static void rememberWorkPos(EntityMaid maid, BlockPos walkPos, BlockPos workPos, float pSpeed, int pDistance) {
        Brain<EntityMaid> brain = maid.getBrain();
        brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(walkPos, pSpeed, pDistance));
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(workPos));

        brain.setMemory(InitEntities.TARGET_POS.get(), new BlockPosTracker(workPos));
        brain.setMemory(ModEntities.WORK_POS.get(), new BlockPosTracker(workPos));

        rememberCurrentWorkPos(maid, workPos);
    }

    public static void rememberWorkPos(EntityMaid maid, BlockPos workPos, float pSpeed, int pDistance) {
        Brain<EntityMaid> brain = maid.getBrain();
        brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(workPos, pSpeed, pDistance));
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(workPos));

        brain.setMemory(InitEntities.TARGET_POS.get(), new BlockPosTracker(workPos));
        brain.setMemory(ModEntities.WORK_POS.get(), new BlockPosTracker(workPos));

        rememberCurrentWorkPos(maid, workPos);
    }

    public static void rememberWalkPos(EntityMaid maid, BlockPos walkPos, float pSpeed, int pDistance) {
        Brain<EntityMaid> brain = maid.getBrain();
        brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(walkPos, pSpeed, pDistance));
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(walkPos));
        brain.setMemory(InitEntities.TARGET_POS.get(), new BlockPosTracker(walkPos));
    }

    public static void rememberCurrentWorkPos(EntityMaid maid, BlockPos workPos) {
//        Brain<EntityMaid> brain = maid.getBrain();
//        Vec3 center = workPos.getCenter();
//        Optional<List<Vec3>> currentWorkPos = brain.getMemory(MkEntities.CURRENT_WORK_POSES.get());
//        if (currentWorkPos.isEmpty()) {
//            brain.setMemory(MkEntities.CURRENT_WORK_POSES.get(), Lists.newArrayList(center));
//        } else {
//            List<Vec3> list = currentWorkPos.get();
//            if (!list.contains(center)) {
//                list.add(center);
//            }
//        }
    }

    public static void eraseCurrentWorkPos(EntityMaid maid, BlockPos workPos) {
        maid.getBrain().getMemory(ModEntities.CURRENT_WORK_POSES.get()).ifPresent(poses -> {
            poses.remove(workPos.getCenter());
        });
    }

    public static void eraseWorkPos(EntityMaid maid) {
        Brain<EntityMaid> brain = maid.getBrain();
        brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);

        brain.eraseMemory(InitEntities.TARGET_POS.get());
        brain.eraseMemory(ModEntities.WORK_POS.get());

//        brain.eraseMemory(MkEntities.CURRENT_WORK_POSES.get());
        brain.eraseMemory(ModEntities.CET_CHEST_ITEMHANDLER.get());
        brain.eraseMemory(ModEntities.GENERATE_RECS.get());
    }

    public static void makeCollectChestItemHandler(EntityMaid maid) {
//        maid.getBrain().setMemory(MkEntities.CET_CHEST_ITEMHANDLER.get(), true);
    }

    public static void eraseCollectChestItemHandler(EntityMaid maid) {
//        maid.getBrain().eraseMemory(MkEntities.CET_CHEST_ITEMHANDLER.get());
    }

    public static void makeGenerateRecs(EntityMaid maid) {
//        maid.getBrain().setMemory(MkEntities.GENERATE_RECS.get(), true);
    }

    public static void eraseGenerateRecs(EntityMaid maid) {
//        maid.getBrain().eraseMemory(MkEntities.GENERATE_RECS.get());
    }

    public static List<Vec3> getCurrentWorkPos(EntityMaid maid) {
        return maid.getBrain().getMemory(ModEntities.CURRENT_WORK_POSES.get()).orElse(List.of());
    }
    
    public static Optional<PositionTracker> getWorkPos(EntityMaid maid) {
        return maid.getBrain().getMemory(ModEntities.WORK_POS.get());
    }

    public static void resetCookWorkState(EntityMaid maid) {
        IMaidTask task = maid.getTask();
        if (!(task instanceof ICookTask<?,?>)) {
            return;
        }

        eraseWorkPos(maid);
    }

    public static void clearWorkMemories(EntityMaid maid, MemoryModuleType<?>... types) {
        Brain<EntityMaid> brain = maid.getBrain();
        for (MemoryModuleType<?> type : types) {
            brain.eraseMemory(type);
        }
    }

    public static Map<MemoryModuleType<?>, MemoryStatus> getMemoryStateMap(MemoryStatus status) {
        Map<MemoryModuleType<?>, MemoryStatus> map = new HashMap<>();
        for (RegistryObject<MemoryModuleType<?>> entry : ModEntities.MEMORY_MODULE_TYPES.getEntries()) {
            map.put(entry.get(), status);
        }
        return ImmutableMap.copyOf(map);
    }

    public static void makePlacePicnicFoodState(EntityMaid maid) {
        maid.getBrain().setMemory(ModEntities.MAID_PLACE_PICNIC_FOOD.get(), true);
    }

    public static void erasePlacePicnicFoodState(EntityMaid maid) {
        maid.getBrain().eraseMemory(ModEntities.MAID_PLACE_PICNIC_FOOD.get());
    }

    public static boolean isPlacePicnicFoodState(EntityMaid maid) {
        return maid.getBrain().hasMemoryValue(ModEntities.MAID_PLACE_PICNIC_FOOD.get());
    }


}
