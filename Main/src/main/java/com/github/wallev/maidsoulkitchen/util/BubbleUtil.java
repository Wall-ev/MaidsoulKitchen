package com.github.wallev.maidsoulkitchen.util;

import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement.TextChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.vhelper.client.chat.VComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class BubbleUtil {
    public static void makeResultsBubbleWithEmpty(EntityMaid maid) {
        MutableComponent append = VComponent.translatable("chat_bubble.maidsoulkitchen.cook.no_ingredient_cook");
        TextChatBubbleData textChatBubbleData = TextChatBubbleData.type2(append);
        maid.getChatBubbleManager().addChatBubble(textChatBubbleData);
    }

    public static void makeResultsBubble(EntityMaid maid, Collection<MaidRec> recs) {
        Map<ItemStack, Integer> resultsMap = new HashMap<>();
        List<ItemStack> results = new ArrayList<>();

        for (MaidRec maidRec : recs) {
            ItemStack result = maidRec.result();
            resultsMap.put(result, resultsMap.getOrDefault(result, 0) + maidRec.amount());
        }

        for (Map.Entry<ItemStack, Integer> entry : resultsMap.entrySet()) {
            ItemStack key = entry.getKey();
            int value = entry.getValue();
            ItemStack copy = key.copyWithCount(key.getCount() * value);
            results.add(copy);
        }

        makeResultsBubble(maid, results);
    }

    public static void makeResultsBubble(EntityMaid maid, List<ItemStack> results) {
        MutableComponent contact = TextContactUtil.contact(results, (result) -> {
            return VComponent.literal(result.getHoverName().getString() + " " + result.getCount())
                    .append(VComponent.translatable("chat_bubble.maidsoulkitchen.cook.food_amount"));
        });

        MutableComponent append = VComponent.translatable("chat_bubble.maidsoulkitchen.cook.can_cook_these_food").append(contact);
        TextChatBubbleData textChatBubbleData = TextChatBubbleData.type2(append);
        maid.getChatBubbleManager().addChatBubble(textChatBubbleData);
    }

    public static void makeResultsBubble(EntityMaid maid, ItemStack result, int existTick) {
        MutableComponent bubbleText = getBubbleText(maid, result);
        TextChatBubbleData textChatBubbleData = TextChatBubbleData.type2(bubbleText);
        maid.getChatBubbleManager().addChatBubble(textChatBubbleData);
    }

    public static void makeResultsBubble(EntityMaid maid, MaidRec maidRec) {
        makeResultsBubble(maid, maidRec.result(), maidRec.time());
    }

    public static MutableComponent getBubbleText(EntityMaid maid, ItemStack food) {
        LivingEntity owner = maid.getOwner();
        String ownerName = owner == null ? VComponent.translatable("chat_bubble.maidsoulkitchen.cook.master").getString() : owner.getDisplayName().getString();
        String name = food.getHoverName().getString();
        int count = food.getCount();

        int type = maid.getRandom().nextInt(4);
        return switch (type) {
            case 0 -> VComponent.translatable("chat_bubble.maidsoulkitchen.cook.make_food.0", ownerName, count, name);
            case 1 -> VComponent.translatable("chat_bubble.maidsoulkitchen.cook.make_food.1", count, name);
            case 2 -> VComponent.translatable("chat_bubble.maidsoulkitchen.cook.make_food.2", count, name);
            case 3 -> VComponent.translatable("chat_bubble.maidsoulkitchen.cook.make_food.3", name, count);
            default -> VComponent.translatable("chat_bubble.maidsoulkitchen.cook.make_food.4", count, name);
        };

    }
}
