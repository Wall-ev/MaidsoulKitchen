package com.github.wallev.maidsoulkitchen.task.other.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.IChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement.TextChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.entity.chatbubble.MaidChatBubbleData;
import com.github.wallev.maidsoulkitchen.util.TextContactUtil;
import com.github.wallev.verhelper.client.chat.VComponent;
import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import com.google.common.collect.ImmutableMap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MaidFeedAnimalTaskT extends MaidCheckRateTask implements VBehaviorControl {
    // 20秒读一次,反正一次能运行的话会一直运行下去.
    private static final int MAX_DELAY_TIME = 400;
    private final float speedModifier;
    private final int maxAnimalCount;
    private Animal feedEntity = null;
    private EntityType<?> lastFeedType = null;
    private long lastFeedTime;

    public MaidFeedAnimalTaskT(float speedModifier, int maxAnimalCount) {
        super(ImmutableMap.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.speedModifier = speedModifier;
        this.maxAnimalCount = maxAnimalCount;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        return feedEntity != null || super.checkExtraStartConditions(worldIn, owner);
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        if (feedEntity != null) {
            if (feedEntity.isAlive() && feedEntity.closerThan(maid, 2)) {
                ItemStack food = ItemsUtil.getStack(maid.getAvailableInv(false), feedEntity::isFood);
                if (!food.isEmpty()) {
                    MaidChatBubbleData bubbleData = MaidChatBubbleData.type2(food.copy());
                    maid.getChatBubbleManager().addChatBubble(bubbleData);

                    food.shrink(1);
                    maid.swing(InteractionHand.MAIN_HAND);
                    feedEntity.setInLove(null);

                    long gameTime = maid.level.getGameTime();
                    EntityType<?> feedEntityType = feedEntity.getType();
                    if (lastFeedType != null && lastFeedType != feedEntityType) {
                        maid.getChatBubbleManager().addChatBubble(TextChatBubbleData.type2(VComponent.literal("小" + feedEntity.getDisplayName().getString() + "君, 快快长大哦~")));
                    } else if (lastFeedType == feedEntityType && (gameTime - lastFeedTime) / 1000 >= IChatBubbleData.DEFAULT_EXIST_TICK) {
                        maid.getChatBubbleManager().addChatBubble(TextChatBubbleData.type2(VComponent.literal("小" + feedEntity.getDisplayName().getString() + "君, 快快长大哦~")));
                    }

                    lastFeedTime = gameTime;
                    lastFeedType = feedEntityType;
                }
            }
            feedEntity = null;
        }

        // 看向中心点（一般来说都会放在养殖的中心点附近把，那里动物居多），模拟养殖的为看到的动物
        maid.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(maid.getRestrictCenter()));

        List<List<Animal>> typeAnimals = this.getEntities(maid).stream()
                .filter(e -> maid.isWithinRestriction(e.blockPosition()))
                .filter(e -> e instanceof Animal animal && e.isAlive())
                .map(Animal.class::cast)
                .collect(Collectors.groupingBy(Entity::getType))
                .values()
                .stream()
                .sorted((o1, o2) -> {
                    return o1.size() - o2.size();
                })
                .toList();

        if (typeAnimals.isEmpty()) {
            maid.getChatBubbleManager().addChatBubble(TextChatBubbleData.type2(VComponent.literal("这似乎没有可以养殖的动物诶~")));
        }

        List<EntityType<?>> noneFoodAnimalTypes = new ArrayList<>();
        List<EntityType<?>> maxAnimalTypes = new ArrayList<>();
        for (List<Animal> animals : typeAnimals) {
            EntityType<?> animalType = animals.get(0).getType();
            if (animals.size() < maxAnimalCount - 2) {
                Animal animal0 = animals.get(0);
                boolean hasFood = ItemsUtil.isStackIn(maid.getAvailableInv(true), animal0::isFood);

                if (!hasFood) {
                    noneFoodAnimalTypes.add(animalType);
                    continue;
                }

                animals.stream().filter(e -> maid.isWithinRestriction(e.blockPosition()))
                        .filter(e -> e.getAge() == 0)
                        .filter(e -> e.canFallInLove())
                        .filter(maid::canPathReach)
                        .findFirst()
                        .ifPresent(e -> {
                            feedEntity = e;
                            BehaviorUtils.setWalkAndLookTargetMemories(maid, e, this.speedModifier, 0);
                        });

                if (feedEntity != null) {
                    return;
                }
            } else {
                maxAnimalTypes.add(animalType);
            }
        }

        this.addFeedChatBubbleIfNeed(noneFoodAnimalTypes, maid, "none_food");
        this.addFeedChatBubbleIfNeed(maxAnimalTypes, maid, "max_number");
    }

    private void addFeedChatBubbleIfNeed(List<EntityType<?>> maxAnimalTypes, EntityMaid maid, String typeKey) {
        if (maxAnimalTypes.isEmpty()) {
            return;
        }

        Component typeComponent = TextContactUtil.contact(maxAnimalTypes, EntityType::getDescription);
        MutableComponent maxAnimalComponent = VComponent.translatable(String.format("chat_bubble.maidsoulkitchen.inner.feed_animal.type." + typeKey + ".%d", maid.getRandom().nextInt(3)), typeComponent);
        maid.getChatBubbleManager().addChatBubble(TextChatBubbleData.type2(maxAnimalComponent));
    }

    private List<LivingEntity> getEntities(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(Collections.emptyList());
    }
}
