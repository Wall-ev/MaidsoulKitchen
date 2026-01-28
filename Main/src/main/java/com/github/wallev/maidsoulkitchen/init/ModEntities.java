package com.github.wallev.maidsoulkitchen.init;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.entity.ai.behavior.manager.BehaviorType;
import com.github.wallev.maidsoulkitchen.entity.ai.behavior.manager.MaidBehaviorManager;
import com.github.wallev.maidsoulkitchen.entity.ai.behavior.manager.MaidWorldBlockManager;
import com.github.wallev.maidsoulkitchen.entity.ai.behavior.work.MaidPlaceItemBehavior;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm.ai.CropResult;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.handler.ICropHarvest;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Optional;

public final class ModEntities {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, MaidsoulKitchen.MOD_ID);

    public static RegistryObject<MemoryModuleType<MaidWorldBlockManager>> WORLD_BLOCK_MANAGER = MEMORY_MODULE_TYPES.register("world_block_manager", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<MaidBehaviorManager>> BEHAVIOR_MANAGER = MEMORY_MODULE_TYPES.register("behavior_manager", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<BehaviorType>> CURRENT_BEHAVIOR = MEMORY_MODULE_TYPES.register("current_behavior", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<MaidPlaceItemBehavior.PlaceData>> PLACE_DATA = MEMORY_MODULE_TYPES.register("place_data", () -> new MemoryModuleType<>(Optional.empty()));

    public static RegistryObject<MemoryModuleType<CropResult>> CROP_RESULT = MEMORY_MODULE_TYPES.register("crop_result", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<ICropHarvest.HarvestData>> HARVEST_DATA = MEMORY_MODULE_TYPES.register("harvest_data", () -> new MemoryModuleType<>(Optional.empty()));

    public static RegistryObject<MemoryModuleType<PositionTracker>> WORK_POS = MEMORY_MODULE_TYPES.register("work_pos", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<List<Vec3>>> CURRENT_WORK_POSES = MEMORY_MODULE_TYPES.register("current_poses", () -> new MemoryModuleType<>(Optional.of(Vec3.CODEC.listOf())));
    public static RegistryObject<MemoryModuleType<Boolean>> CET_CHEST_ITEMHANDLER = MEMORY_MODULE_TYPES.register("cet_chest_itemhandler", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<Boolean>> GENERATE_RECS = MEMORY_MODULE_TYPES.register("generate_recs", () -> new MemoryModuleType<>(Optional.empty()));

    public static RegistryObject<MemoryModuleType<ItemInventory>> HUB_INPUT_INVENTORY = MEMORY_MODULE_TYPES.register("hub_input_inventory", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<ItemInventory>> HUB_OUTPUT_INVENTORY = MEMORY_MODULE_TYPES.register("hub_output_inventory", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<ItemInventory>> INPUT_CHEST_INVENTORY = MEMORY_MODULE_TYPES.register("input_chest_inventory", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<ItemInventory>> OUTPUT_CHEST_INVENTORY = MEMORY_MODULE_TYPES.register("output_chest_inventory", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<List<MaidRec>>> MAID_RECS = MEMORY_MODULE_TYPES.register("maid_recs", () -> new MemoryModuleType<>(Optional.empty()));

    public static RegistryObject<MemoryModuleType<Boolean>> MAID_PLACE_PICNIC_FOOD = MEMORY_MODULE_TYPES.register("maid_place_picnic_food", () -> new MemoryModuleType<>(Optional.empty()));
}
