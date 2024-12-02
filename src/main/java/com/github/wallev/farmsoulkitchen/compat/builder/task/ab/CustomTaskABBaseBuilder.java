package com.github.wallev.farmsoulkitchen.compat.builder.task.ab;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class CustomTaskABBaseBuilder<T extends CustomTaskABBase> {

    // 用于任务唯一标识符
    protected final ResourceLocation id;

//    // 用于调试
    private boolean debug;
    private T t;

    protected Consumer<T> acceptThis;

    // IMaidTask
    protected ResourceLocation uid;
    protected ItemStack icon;
    protected BiFunction<IMaidTask, EntityMaid, SoundEvent> getAmbientSound;
    protected BiFunction<IMaidTask, EntityMaid, List<Pair<Double, BehaviorControl<? super EntityMaid>>>> createBrainTasks;
    // default
    protected BiFunction<IMaidTask, EntityMaid, Boolean> isEnable;
    protected MutableComponent name;
    protected BiFunction<IMaidTask, EntityMaid, List<Pair<String, Predicate<EntityMaid>>>> getConditionDescription;
    protected BiFunction<IMaidTask, EntityMaid, List<String>> getDescription;


    protected CustomTaskABBaseBuilder(ResourceLocation id) {
        this.id = id;
    }

    public CustomTaskABBaseBuilder(ResourceLocation id, boolean debug) {
        this.id = id;
        this.debug = debug;
    }

    protected void setDebug(boolean debug) {
        this.debug = debug;
    }

    public CustomTaskABBaseBuilder<T> acceptThis(Consumer<T> acceptThis) {
        this.acceptThis = acceptThis;
        return this;
    }

    /**
     * method
     */
    // IMaidTask
    @Nullable
    public CustomTaskABBaseBuilder<T> setUid(ResourceLocation uid) {
        this.uid = uid;
        return this;
    }
    @Nullable
    public CustomTaskABBaseBuilder<T> setIcon(ItemStack icon) {
        this.icon = icon;
        return this;
    }
    @Nullable
    public CustomTaskABBaseBuilder<T> getAmbientSound(BiFunction<IMaidTask, EntityMaid, SoundEvent> getAmbientSound) {
        this.getAmbientSound = getAmbientSound;
        return this;
    }
    @Nullable
    public CustomTaskABBaseBuilder<T> createBrainTasks(BiFunction<IMaidTask, EntityMaid, List<Pair<Double, BehaviorControl<? super EntityMaid>>>> createBrainTasks) {
        this.createBrainTasks = createBrainTasks;
        return this;
    }
    // default
    public CustomTaskABBaseBuilder<T> isEnable(BiFunction<IMaidTask, EntityMaid, Boolean> isEnable) {
        this.isEnable = isEnable;
        return this;
    }

    public CustomTaskABBaseBuilder<T> setName(MutableComponent name) {
        this.name = name;
        return this;
    }

    public CustomTaskABBaseBuilder<T> getConditionDescription(BiFunction<IMaidTask, EntityMaid, List<Pair<String, Predicate<EntityMaid>>>> getConditionDescription) {
        this.getConditionDescription = getConditionDescription;
        return this;
    }

    public CustomTaskABBaseBuilder<T> getDescription(BiFunction<IMaidTask, EntityMaid, List<String>> getDescription) {
        this.getDescription = getDescription;
        return this;
    }

    public T createObject() {
        if (debug) {
            return debugBuild();
        }else {
            T task = getTask();
            TouhouLittleMaid.EXTENSIONS.add(task);
            return task;
        }
    }

    private T debugBuild(){
        if (t == null) {
            t = getTask();
        }
        if (!TouhouLittleMaid.EXTENSIONS.contains(t)){
            TouhouLittleMaid.EXTENSIONS.add(t);
        }else {
            t.initBuilderData(this);
        }
        return t;
    }

    public T build() {
        return createObject();
    }

    protected abstract T getTask();

    protected void init() {
        TaskManager.init();
    }
}
