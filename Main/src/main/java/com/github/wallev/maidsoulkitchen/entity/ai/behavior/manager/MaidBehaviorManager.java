package com.github.wallev.maidsoulkitchen.entity.ai.behavior.manager;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.entity.ai.behavior.work.IMaidBehavior;
import com.github.wallev.maidsoulkitchen.init.ModEntities;
import com.github.wallev.maidsoulkitchen.util.AnnotationHelper;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

//@AutoInit
public final class MaidBehaviorManager {
    private final Map<ResourceLocation, IMaidBehavior> behaviors = new HashMap<>();
    private static final MaidBehaviorManager INSTANCE = new MaidBehaviorManager();

    public static MaidBehaviorManager getInstance() {
        return INSTANCE;
    }

    public static IMaidBehavior get(ResourceLocation uid) {
        return INSTANCE.behaviors.get(uid);
    }

    public MaidBehaviorManager() {
        autoRegister();
    }

    private void autoRegister() {
        AnnotationHelper.<BehaviorAutoRegister, IMaidBehavior>readWithObj(BehaviorAutoRegister.class, (behavior) -> {
            behaviors.put(behavior.getUid(), behavior);
        });
    }

    public static BehaviorType get(EntityMaid maid) {
        return maid.getBrain().getMemory(ModEntities.CURRENT_BEHAVIOR.get()).orElse(BehaviorType.IDLE);
    }

    public static void set(EntityMaid maid, BehaviorType behaviorType) {
        maid.getBrain().setMemory(ModEntities.CURRENT_BEHAVIOR.get(), behaviorType);
    }

    public @interface BehaviorAutoRegister {
    }
}

