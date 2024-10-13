package com.github.catbert.tlma.task.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManger;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaidFeedAnimalTaskT extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 12;
    private final float speedModifier;
    private final int maxAnimalCount;
    private Animal feedEntity = null;

    public MaidFeedAnimalTaskT(float speedModifier, int maxAnimalCount) {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.speedModifier = speedModifier;
        this.maxAnimalCount = maxAnimalCount;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        feedEntity = null;
        List<LivingEntity> list = this.getEntities(maid)
                .find(e -> maid.isWithinRestriction(e.blockPosition()))
                .filter(livingEntity -> livingEntity instanceof Animal)
                .filter(Entity::isAlive)
                .toList();

        Map<EntityType<?>, List<Animal>> resourceLocationListHashMap = new HashMap<>();
        for (LivingEntity livingEntity : list) {
            resourceLocationListHashMap.computeIfAbsent(livingEntity.getType(), k -> Lists.newArrayList()).add((Animal) livingEntity);
        }

        for (List<Animal> value : resourceLocationListHashMap.values()) {
            if (value.size() < maxAnimalCount - 2) {
                value.stream().filter(e -> maid.isWithinRestriction(e.blockPosition()))
                        .filter(e -> e.getAge() == 0)
                        .filter(e -> e.canFallInLove())
                        .filter(e -> ItemsUtil.isStackIn(maid.getAvailableInv(false), ((Animal) e)::isFood))
                        .filter(maid::canPathReach)
                        .findFirst()
                        .ifPresent(e -> {
                            feedEntity = (Animal) e;
                            BehaviorUtils.setWalkAndLookTargetMemories(maid, e, this.speedModifier, 0);
                        });

                if (feedEntity != null && feedEntity.closerThan(maid, 2)) {
                    ItemStack food = ItemsUtil.getStack(maid.getAvailableInv(false), feedEntity::isFood);
                    if (!food.isEmpty()) {
                        food.shrink(1);
                        maid.swing(InteractionHand.MAIN_HAND);
                        feedEntity.setInLove(null);
                        return;
                    }
                    feedEntity = null;
                }
            }
        }

        ChatBubbleManger.addInnerChatText(maid, "chat_bubble.touhou_little_maid.inner.feed_animal.max_number");
    }

    private NearestVisibleLivingEntities getEntities(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
    }
}
