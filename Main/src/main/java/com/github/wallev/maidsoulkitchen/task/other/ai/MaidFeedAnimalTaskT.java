package com.github.wallev.maidsoulkitchen.task.other.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.IChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement.TextChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.debug.annotation.SafeRun;
import com.github.wallev.maidsoulkitchen.debug.annotation.TimeRecord;
import com.github.wallev.maidsoulkitchen.util.TextContactUtil;
import com.github.wallev.maidsoulkitchen.vhelper.client.chat.VComponent;
import com.github.wallev.maidsoulkitchen.vhelper.server.ai.VBehaviorControl;
import com.google.common.collect.ImmutableMap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

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

    @TimeRecord
    @SafeRun
    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        if (feedEntity != null) {
            if (feedEntity.isAlive() && feedEntity.closerThan(maid, 2)) {
                ItemStack food = ItemsUtil.getStack(maid.getAvailableInv(false), feedEntity::isFood);
                if (!food.isEmpty()) {
                    food.shrink(1);
                    maid.swing(InteractionHand.MAIN_HAND);
                    feedEntity.setInLove(null);

                    long gameTime = maid.level.getGameTime();
                    EntityType<?> feedEntityType = feedEntity.getType();
                    if (lastFeedType != null && lastFeedType != feedEntityType) {
                        maid.getChatBubbleManager().addChatBubble(TextChatBubbleData.type2(VComponent.translatable("chat_bubble.maidsoulkitchen.inner.feed_animal.feed_end")));
                    } else if (lastFeedType == feedEntityType && (gameTime - lastFeedTime) / 1000 >= IChatBubbleData.DEFAULT_EXIST_TICK + 5) {
                        maid.getChatBubbleManager().addChatBubble(TextChatBubbleData.type2(VComponent.translatable("chat_bubble.maidsoulkitchen.inner.feed_animal.feed_end")));
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
                .collect(() -> {
                    return new LinkedHashMap<EntityType<?>, List<Animal>>();
                }, (map, b) -> {
                    EntityType<?> entityType = b.getType();
                    List<Animal> animals = map.computeIfAbsent(entityType, (type) -> new ArrayList<>());
                    animals.add(b);
                }, (a, b) -> {

                })

                .values()
                .stream()
                .filter((o1) -> {
                    return o1.size() > 2;
                })
                .sorted((o1, o2) -> {
                    return o1.size() - o2.size();
                })
                .sorted((o1, o2) -> {
                    Animal animal0 = o1.get(0);
                    Animal animal1 = o2.get(0);

                    Vec3 position0 = animal0.position;
                    Vec3 position1 = animal1.position;

                    Vec3 maidPosition = maid.position;

                    return (int) (maidPosition.distanceToSqr(position0) - maidPosition.distanceToSqr(position1));
                })
                .toList();

        if (typeAnimals.isEmpty()) {
            maid.getChatBubbleManager().addChatBubble(TextChatBubbleData.type2(VComponent.translatable("chat_bubble.maidsoulkitchen.inner.feed_animal.no_animals")));
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
