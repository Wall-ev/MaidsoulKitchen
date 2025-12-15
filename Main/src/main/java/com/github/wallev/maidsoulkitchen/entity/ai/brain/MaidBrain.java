package com.github.wallev.maidsoulkitchen.entity.ai.brain;

import com.github.tartaricacid.touhoulittlemaid.api.entity.ai.IExtraMaidBrain;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.ai.PlaceFoodForPicnicWithRideIdleTask;
import com.github.wallev.maidsoulkitchen.util.DevUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static com.github.wallev.maidsoulkitchen.init.ModEntities.MEMORY_MODULE_TYPES;

public class MaidBrain implements IExtraMaidBrain {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public List<MemoryModuleType<?>> getExtraMemoryTypes() {
        return (List) MEMORY_MODULE_TYPES.getEntries().stream()
                .map(RegistryObject::get)
                .toList();
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> getIdleBehaviors() {
        return List.of();
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> getRideIdleBehaviors() {
        return DevUtil.isDevEnv() ? Lists.newArrayList(Pair.of(5, new PlaceFoodForPicnicWithRideIdleTask())) : List.of();
    }
}
