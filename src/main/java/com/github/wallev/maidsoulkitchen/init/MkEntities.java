package com.github.wallev.maidsoulkitchen.init;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Optional;

public final class MkEntities {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, MaidsoulKitchen.MOD_ID);
    public static RegistryObject<MemoryModuleType<PositionTracker>> WORK_POS = MEMORY_MODULE_TYPES.register("work_pos", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<List<Vec3>>> CURRENT_WORK_POSES = MEMORY_MODULE_TYPES.register("current_poses", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<Boolean>> CET_CHEST_ITEMHANDLER = MEMORY_MODULE_TYPES.register("cet_chest_itemhandler", () -> new MemoryModuleType<>(Optional.empty()));
    public static RegistryObject<MemoryModuleType<Boolean>> GENERATE_RECS = MEMORY_MODULE_TYPES.register("generate_recs", () -> new MemoryModuleType<>(Optional.empty()));
}
