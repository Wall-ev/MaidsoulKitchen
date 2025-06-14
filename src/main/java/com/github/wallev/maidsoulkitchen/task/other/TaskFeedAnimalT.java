package com.github.wallev.maidsoulkitchen.task.other;

import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.DefaultMaidTaskConfigContainer;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.other.ai.MaidFeedAnimalTaskT;
import com.github.wallev.verhelper.server.ai.*;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.github.wallev.maidsoulkitchen.config.subconfig.TaskConfig.FEED_SINGLE_ANIMAL_MAX_NUMBER;

public class TaskFeedAnimalT implements IAttackTask, IMaidsoulKitchenTask {
    private static final int MAX_STOP_ATTACK_DISTANCE = 8;

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.FEED_ANIMAL_T.uid;
    }

    @Override
    public ItemStack getIcon() {
        return Items.WHEAT.getDefaultInstance();
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.environmentSound(maid, InitSounds.MAID_FEED_ANIMAL.get(), 0.5f);
    }

    @Override
    public List<Pair<Integer, VBehaviorControl>> vCreateBrainTasks(EntityMaid maid) {
        VBehaviorControl supplementedTask = VStartAttacking.create(this::hasAssaultWeapon, this::findFirstValidAttackTarget);
        VBehaviorControl findTargetTask = VStopAttackingIfTargetInvalid.create(
                (target) -> !hasAssaultWeapon(maid) || farAway(target, maid));
        VBehaviorControl moveToTargetTask = VSetWalkTargetFromAttackTargetIfTargetOutOfReach.create(0.6f);
        VBehaviorControl attackTargetTask = VMeleeAttack.create(20);

        return Lists.newArrayList(
                Pair.of(5, new MaidFeedAnimalTaskT(0.6f, FEED_SINGLE_ANIMAL_MAX_NUMBER.get())),
                Pair.of(6, supplementedTask),
                Pair.of(6, findTargetTask),
                Pair.of(6, moveToTargetTask),
                Pair.of(6, attackTargetTask)
        );
    }

    private Optional<? extends LivingEntity> findFirstValidAttackTarget(EntityMaid maid) {
        maid.getLookControl().setLookAt(maid.getRestrictCenter().getCenter());
        Map<EntityType<?>, List<Animal>> typeAnimals = maid.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES)
                .orElse(Collections.emptyList()).stream()
                .filter(e -> maid.isWithinRestriction(e.blockPosition()))
                .filter(e -> e instanceof Animal && e.isAlive())
                .map(Animal.class::cast)
                .collect(Collectors.groupingBy(Entity::getType));

        for (List<Animal> value : typeAnimals.values()) {
            if (value.size() >= (FEED_SINGLE_ANIMAL_MAX_NUMBER.get() - 2)) {
                return value.stream().filter(e -> maid.isWithinRestriction(e.blockPosition()))
                        .filter(e -> !e.isBaby())
                        .filter(e -> ItemsUtil.isStackIn(maid.getAvailableInv(false), ((Animal) e)::isFood))
                        .filter(maid::canPathReach)
                        .findFirst();
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean canAttack(EntityMaid maid, LivingEntity target) {
        return true;
    }

    @Override
    public List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(EntityMaid maid) {
        return Lists.newArrayList(Pair.of("can_feed", Predicates.alwaysTrue()), Pair.of("assault_weapon", this::hasAssaultWeapon));
    }

    public MenuProvider getTaskConfigGuiProvider(EntityMaid maid) {
        final int entityId = maid.getId();
        return new MenuProvider() {
            public Component getDisplayName() {
                return Component.literal("Maid Task Config Container");
            }

            public AbstractMaidContainer createMenu(int index, Inventory playerInventory, Player player) {
                return new DefaultMaidTaskConfigContainer(index, playerInventory, entityId);
            }
        };
    }

    private NearestVisibleLivingEntities getEntities(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
    }

    private boolean hasAssaultWeapon(EntityMaid maid) {
        return maid.getMainHandItem().getAttributeModifiers(EquipmentSlot.MAINHAND).containsKey(Attributes.ATTACK_DAMAGE);
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        return maid.distanceTo(target) > MAX_STOP_ATTACK_DISTANCE;
    }

    @Override
    public String getBookEntry() {
        return "feed_animal_t";
    }
}
