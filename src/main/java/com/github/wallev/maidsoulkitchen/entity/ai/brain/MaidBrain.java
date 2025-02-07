package com.github.wallev.maidsoulkitchen.entity.ai.brain;

import com.github.tartaricacid.touhoulittlemaid.api.entity.ai.IExtraMaidBrain;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.List;

public class MaidBrain implements IExtraMaidBrain {

    @Override
    public List<MemoryModuleType<?>> getExtraMemoryTypes() {
        return Lists.newArrayList(MkEntities.WORK_POS.get());
    }
}
