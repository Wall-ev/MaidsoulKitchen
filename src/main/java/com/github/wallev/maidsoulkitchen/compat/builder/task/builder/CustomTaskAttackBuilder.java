package com.github.wallev.maidsoulkitchen.compat.builder.task.builder;

import com.github.wallev.maidsoulkitchen.compat.builder.task.ab.CustomTaskABAttackBuilder;
import com.github.wallev.maidsoulkitchen.util.function.Predicate3;
import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class CustomTaskAttackBuilder extends CustomTaskABAttackBuilder<CustomTaskAttack> {
    
    protected CustomTaskAttackBuilder(ResourceLocation id) {
        super(id);
    }

    public CustomTaskAttackBuilder(ResourceLocation id, boolean debug) {
        super(id, debug);
    }

    public static CustomTaskAttackBuilder create(ResourceLocation id) {
        return new CustomTaskAttackBuilder(id);
    }
    @Override
    protected CustomTaskAttack getTask() {
        return new CustomTaskAttack(this);
    }

    @Override
    public CustomTaskAttackBuilder canAttack(Predicate3<IAttackTask, EntityMaid, LivingEntity> canAttack) {
        return (CustomTaskAttackBuilder) super.canAttack(canAttack);
    }

    @Override
    public CustomTaskAttackBuilder hasExtraAttack(Predicate3<IAttackTask, EntityMaid, Entity> hasExtraAttack) {
        return (CustomTaskAttackBuilder) super.hasExtraAttack(hasExtraAttack);
    }

    @Override
    public CustomTaskAttackBuilder doExtraAttack(Predicate3<IAttackTask, EntityMaid, Entity> doExtraAttack) {
        return (CustomTaskAttackBuilder) super.doExtraAttack(doExtraAttack);
    }

    @Nullable
    @Override
    public CustomTaskAttackBuilder setUid(ResourceLocation uid) {
        return (CustomTaskAttackBuilder) super.setUid(uid);
    }

    @Nullable
    @Override
    public CustomTaskAttackBuilder setIcon(ItemStack icon) {
        return (CustomTaskAttackBuilder) super.setIcon(icon);
    }

    @Nullable
    @Override
    public CustomTaskAttackBuilder getAmbientSound(BiFunction<IMaidTask, EntityMaid, SoundEvent> getAmbientSound) {
        return (CustomTaskAttackBuilder) super.getAmbientSound(getAmbientSound);
    }

    @Nullable
    @Override
    public CustomTaskAttackBuilder createBrainTasks(BiFunction<IMaidTask, EntityMaid, List<Pair<Double, BehaviorControl<? super EntityMaid>>>> createBrainTasks) {
        return (CustomTaskAttackBuilder) super.createBrainTasks(createBrainTasks);
    }

    @Override
    public CustomTaskAttackBuilder isEnable(BiFunction<IMaidTask, EntityMaid, Boolean> isEnable) {
        return (CustomTaskAttackBuilder) super.isEnable(isEnable);
    }

    @Override
    public CustomTaskAttackBuilder setName(MutableComponent name) {
        return (CustomTaskAttackBuilder) super.setName(name);
    }

    @Override
    public CustomTaskAttackBuilder getConditionDescription(BiFunction<IMaidTask, EntityMaid, List<Pair<String, Predicate<EntityMaid>>>> getConditionDescription) {
        return (CustomTaskAttackBuilder) super.getConditionDescription(getConditionDescription);
    }

    @Override
    public CustomTaskAttackBuilder getDescription(BiFunction<IMaidTask, EntityMaid, List<String>> getDescription) {
        return (CustomTaskAttackBuilder) super.getDescription(getDescription);
    }

    @Override
    public CustomTaskAttack createObject() {
        return super.createObject();
    }
}
