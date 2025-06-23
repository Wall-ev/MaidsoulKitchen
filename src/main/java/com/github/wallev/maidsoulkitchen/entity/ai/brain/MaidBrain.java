package com.github.wallev.maidsoulkitchen.entity.ai.brain;

import com.github.tartaricacid.touhoulittlemaid.api.entity.ai.IExtraMaidBrain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

import static com.github.wallev.maidsoulkitchen.init.MkEntities.MEMORY_MODULE_TYPES;

public class MaidBrain implements IExtraMaidBrain {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public List<MemoryModuleType<?>> getExtraMemoryTypes() {
        return (List) MEMORY_MODULE_TYPES.getEntries().stream()
                .map(RegistryObject::get)
                .toList();
    }
}
