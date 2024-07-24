package com.github.catbert.tlma.compat.builder.task.ab;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public abstract class CustomTaskABBase<T extends CustomTaskABBaseBuilder> implements ILittleMaid, IMaidTask {
    // 用于任务唯一标识符
    public final ResourceLocation id;

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

    public CustomTaskABBase(T t) {
        this.id = t.id;
        initBuilderData(t);
    }

    @Override
    public ResourceLocation getUid() {
        return this.uid;
    }

    @Override
    public ItemStack getIcon() {
        return this.icon;
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return this.getAmbientSound.apply(this, maid);
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        // js只有number类型，传进来变成了double
        // 所以用double接受，后面处理成int类型
        List<Pair<Double, BehaviorControl<? super EntityMaid>>> apply = this.createBrainTasks.apply(this, maid);
        TouhouLittleMaid.LOGGER.info("CustomTaskBase:  createBrainTasks = " + apply);

        ArrayList<Pair<Integer, BehaviorControl<? super EntityMaid>>> pairList1 = new ArrayList<>();
        for (Pair<Double, BehaviorControl<? super EntityMaid>> pair1 : apply) {
            pairList1.add(Pair.of(pair1.getFirst().intValue(), pair1.getSecond()));
        }
        TouhouLittleMaid.LOGGER.info("CustomTaskBase:  createBrainTasks  TRANS = " + pairList1);

        return pairList1;
    }

    @Override
    public boolean isEnable(EntityMaid maid) {
        TouhouLittleMaid.LOGGER.info("CustomTaskBase:  isEnable = " + this.isEnable);
        if (this.isEnable != null) {
            return this.isEnable.apply(this, maid);
        }
        return IMaidTask.super.isEnable(maid);
    }

    @Override
    public MutableComponent getName() {
        if (this.name != null) {
            return this.name;
        }
        return IMaidTask.super.getName();
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        if (this.getConditionDescription != null) {
            return this.getConditionDescription.apply(this, maid);
        }
        return IMaidTask.super.getConditionDescription(maid);
    }

    @Override
    public List<String> getDescription(EntityMaid maid) {
        if (this.getDescription != null) {
            return this.getDescription.apply(this, maid);
        }
        return IMaidTask.super.getDescription(maid);
    }

    @Override
    public void addMaidTask(TaskManager manager) {
        manager.add(this);
    }

    public void initBuilderData(T t){
        t.acceptThis.accept(this);

        this.uid = t.uid;
        this.icon = t.icon;
        this.getAmbientSound = t.getAmbientSound;
        this.createBrainTasks = t.createBrainTasks;
        this.isEnable = t.isEnable;
        this.name = t.name;
        this.getConditionDescription = t.getConditionDescription;
        this.getDescription = t.getDescription;
    }
}
