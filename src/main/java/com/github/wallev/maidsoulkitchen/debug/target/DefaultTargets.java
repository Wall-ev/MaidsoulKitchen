package com.github.wallev.maidsoulkitchen.debug.target;

import com.github.tartaricacid.touhoulittlemaid.debug.target.DebugTarget;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.entity.ai.Brain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@VisibleForDebug
public class DefaultTargets {
    private static final int COLOR = 0x9FFFFF00;
    private static final String TEXT = "Work Pos";
    private static final int LIFE_TIME = 2 * 1000;

    public static List<Function<EntityMaid, List<DebugTarget>>> getDefaultTargets() {
        return List.of(DefaultTargets::getDefaultTargets);
    }

    public static List<DebugTarget> getDefaultTargets(EntityMaid maid) {
        List<DebugTarget> list = new ArrayList<>();

        Brain<EntityMaid> brain = maid.getBrain();
        brain.getMemory(MkEntities.WORK_POS.get()).ifPresent(m -> {
            list.add(new DebugTarget(m.currentBlockPosition(), COLOR, TEXT, LIFE_TIME));
        });
        return list;
    }
}
