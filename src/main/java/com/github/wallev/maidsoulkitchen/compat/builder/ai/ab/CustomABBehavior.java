package com.github.wallev.maidsoulkitchen.compat.builder.ai.ab;

import com.github.wallev.maidsoulkitchen.util.function.Consumer3;
import com.github.wallev.maidsoulkitchen.util.function.Predicate3;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class CustomABBehavior<T extends CustomABBehaviorBuilder> extends Behavior<EntityMaid> implements IAcceptThis {
    protected Supplier<Status> getStatus;
    protected Consumer3<ServerLevel, EntityMaid, Long> start;
    protected Consumer3<ServerLevel, EntityMaid, Long> tick;
    protected Consumer3<ServerLevel, EntityMaid, Long> stop;
    protected Predicate3<ServerLevel, EntityMaid, Long> canStillUse;
    protected Predicate<Long> timedOut;
    protected BiPredicate<ServerLevel, EntityMaid> checkExtraStartConditions;
    protected Predicate<EntityMaid> hasRequiredMemories;

    public CustomABBehavior(Map<MemoryModuleType<?>, MemoryStatus> pEntryCondition) {
        super(pEntryCondition);
    }

    public CustomABBehavior(Map<MemoryModuleType<?>, MemoryStatus> pEntryCondition, int pDuration) {
        super(pEntryCondition, pDuration);
    }

    public CustomABBehavior(Map<MemoryModuleType<?>, MemoryStatus> pEntryCondition, int pMinDuration, int pMaxDuration) {
        super(pEntryCondition, pMinDuration, pMaxDuration);
    }

    @Override
    public Status getStatus() {
        return this.getStatus != null ? this.getStatus.get() : this.superGetStatus();
    }

    @Override
    protected void start(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        if (this.start != null) {
            this.start.accept(pLevel, pEntity, pGameTime);
        } else {
            this.superStart(pLevel, pEntity, pGameTime);
        }
//         ??为什么idea会报错？
//        this.start != null ? this.start.accept(pLevel, pEntity, pGameTime) : this.superStart(pLevel, pEntity, pGameTime);
    }

    @Override
    protected void tick(ServerLevel pLevel, EntityMaid pOwner, long pGameTime) {
        if (this.tick != null) {
            this.tick.accept(pLevel, pOwner, pGameTime);
        } else {
            this.superTick(pLevel, pOwner, pGameTime);
        }
    }

    @Override
    protected void stop(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        if (this.stop != null) {
            this.stop.accept(pLevel, pEntity, pGameTime);
        } else {
            this.superStop(pLevel, pEntity, pGameTime);
        }
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        if (this.canStillUse != null) {
            return this.canStillUse.test(pLevel, pEntity, pGameTime);
        } else {
            return this.superCanStillUse(pLevel, pEntity, pGameTime);
        }
    }

    @Override
    protected boolean timedOut(long pGameTime) {
        if (this.timedOut != null) {
            return this.timedOut.test(pGameTime);
        } else {
            return this.superTimedOut(pGameTime);
        }
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, EntityMaid pOwner) {
        if (this.checkExtraStartConditions != null) {
            return (Boolean) this.checkExtraStartConditions.test(pLevel, pOwner);
        } else {
            return this.superCheckExtraStartConditions(pLevel, pOwner);
        }
    }


    @Override
    protected boolean hasRequiredMemories(EntityMaid pOwner) {
        if (this.hasRequiredMemories != null) {
            return this.hasRequiredMemories.test(pOwner);
        } else {
            return this.superHasRequiredMemories(pOwner);
        }
    }

    public void initBuilderData(T t) {
        t.acceptThis.accept(this);

        this.getStatus = t.getStatus;
        this.start = t.start;
        this.tick = t.tick;
        this.stop = t.stop;
        this.canStillUse = t.canStillUse;
        this.timedOut = t.timedOut;
        this.checkExtraStartConditions = t.checkExtraStartConditions;
        this.hasRequiredMemories = t.hasRequiredMemories;
    }

    public boolean superCheckExtraStartConditions(ServerLevel pLevel, EntityMaid pOwner) {
        return super.checkExtraStartConditions(pLevel, pOwner);
    }


    public Status superGetStatus() {
        return super.getStatus();
    }

    public void superStart(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        super.start(pLevel, pEntity, pGameTime);
    }

    public void superTick(ServerLevel pLevel, EntityMaid pOwner, long pGameTime) {
        super.tick(pLevel, pOwner, pGameTime);
    }

    public void superStop(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        super.stop(pLevel, pEntity, pGameTime);
    }

    public boolean superCanStillUse(ServerLevel pLevel, EntityMaid pEntity, long pGameTime) {
        return super.canStillUse(pLevel, pEntity, pGameTime);
    }

    public boolean superTimedOut(long pGameTime) {
        return super.timedOut(pGameTime);
    }

    public boolean superHasRequiredMemories(EntityMaid pOwner) {
        return super.hasRequiredMemories(pOwner);
    }
}
