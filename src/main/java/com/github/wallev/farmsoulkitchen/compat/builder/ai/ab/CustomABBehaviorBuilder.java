package com.github.wallev.farmsoulkitchen.compat.builder.ai.ab;

import com.github.wallev.farmsoulkitchen.util.function.Consumer3;
import com.github.wallev.farmsoulkitchen.util.function.Predicate3;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class CustomABBehaviorBuilder<T extends CustomABBehavior> {
    // 用于任务唯一标识符
    protected final ResourceLocation id;
    protected Consumer<T> acceptThis;
    protected Map<MemoryModuleType<?>, MemoryStatus> entryCondition;
    protected int minDuration;
    protected int maxDuration;
    protected Supplier<Behavior.Status> getStatus;
    protected Consumer3<ServerLevel, EntityMaid, Long> start;
    protected Consumer3<ServerLevel, EntityMaid, Long> tick;
    protected Consumer3<ServerLevel, EntityMaid, Long> stop;
    protected Predicate3<ServerLevel, EntityMaid, Long> canStillUse;
    protected Predicate<Long> timedOut;
    protected BiPredicate<ServerLevel, EntityMaid> checkExtraStartConditions;
    protected Predicate<EntityMaid> hasRequiredMemories;
    // 用于调试
    private boolean debug;
    private T t;

    public CustomABBehaviorBuilder(ResourceLocation id) {
        this.id = id;
    }

    public CustomABBehaviorBuilder(ResourceLocation id, boolean debug) {
        this.id = id;
        this.debug = debug;
    }

    public CustomABBehaviorBuilder<T> constructBehavior(Map<MemoryModuleType<?>, MemoryStatus> entryCondition) {
        this.entryCondition = entryCondition;
        return this;
    }

//    public CustomABBehaviorBuilder<T> constructBehavior(Map<MemoryModuleType<?>, MemoryStatus> entryCondition, int minDuration) {
//        this.entryCondition = entryCondition;
//        this.minDuration = minDuration;
//        return this;
//    }
//
//    public CustomABBehaviorBuilder<T> constructBehavior(Map<MemoryModuleType<?>, MemoryStatus> entryCondition, int minDuration, int maxDuration) {
//        this.entryCondition = entryCondition;
//        this.minDuration = minDuration;
//        this.maxDuration = maxDuration;
//        return this;
//    }

    public CustomABBehaviorBuilder<T> acceptThis(Consumer<T> iAcceptThis) {
        this.acceptThis = iAcceptThis;
        return this;
    }


    public CustomABBehaviorBuilder<T> getStatus(Supplier<Behavior.Status> getStatus) {
        this.getStatus = getStatus;
        return this;
    }


    public CustomABBehaviorBuilder<T> start(Consumer3<ServerLevel, EntityMaid, Long> start) {
        this.start = start;
        return this;
    }


    public CustomABBehaviorBuilder<T> tick(Consumer3<ServerLevel, EntityMaid, Long> tick) {
        this.tick = tick;
        return this;
    }


    public CustomABBehaviorBuilder<T> stop(Consumer3<ServerLevel, EntityMaid, Long> stop) {
        this.stop = stop;
        return this;
    }


    public CustomABBehaviorBuilder<T> canStillUse(Predicate3<ServerLevel, EntityMaid, Long> canStillUse) {
        this.canStillUse = canStillUse;
        return this;
    }


    public CustomABBehaviorBuilder<T> timedOut(Predicate<Long> timedOut) {
        this.timedOut = timedOut;
        return this;
    }


    public CustomABBehaviorBuilder<T> checkExtraStartConditions(BiPredicate<ServerLevel, EntityMaid> checkExtraStartConditions) {
        this.checkExtraStartConditions = checkExtraStartConditions;
        return this;
    }

    public CustomABBehaviorBuilder<T> hasRequiredMemories(Predicate<EntityMaid> hasRequiredMemories) {
        this.hasRequiredMemories = hasRequiredMemories;
        return this;
    }

    public T createObject() {
        if (debug) {
            return debugBuild();
        } else {
            return getT();
        }
    }

    private T debugBuild() {
        if (t == null) {
            t = getT();
        } else {
            t.initBuilderData(this);
        }
        return t;
    }

    public T build() {
        return createObject();
    }

    protected abstract T getT();
}
