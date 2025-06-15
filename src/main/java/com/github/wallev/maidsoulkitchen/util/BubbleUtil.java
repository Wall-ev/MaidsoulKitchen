package com.github.wallev.maidsoulkitchen.util;

import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement.TextChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.verhelper.client.chat.VComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class BubbleUtil {
    public static void makeResultsBubble(EntityMaid maid, List<ItemStack> results) {
        MutableComponent contact = TextContactUtil.contact(results, (result) -> {
            return VComponent.literal(result.getHoverName().getString() + " " + result.getCount() + "份");
        });

        MutableComponent append = VComponent.literal("哇，可以制作这么多食物诶: ").append(contact);
        TextChatBubbleData textChatBubbleData = TextChatBubbleData.type2(append);
        maid.getChatBubbleManager().addChatBubble(textChatBubbleData);
    }

    public static void makeResultsBubble(EntityMaid maid, ItemStack result, int existTick) {
//        MutableComponent displayName = (MutableComponent) result.getDisplayName();
//        displayName.append(" " + result.getCount() + "份, 启动！");
//        TextChatBubbleData textChatBubbleData = TextChatBubbleData.create(existTick, displayName, TYPE_2, DEFAULT_PRIORITY);

//        TextChatBubbleData textChatBubbleData = TextChatBubbleData.type2(((MutableComponent)result.getDisplayName()).append(" " + result.getCount() + "份, 启动！"));

        MutableComponent bubbleText = getBubbleText(maid, result);
        TextChatBubbleData textChatBubbleData = TextChatBubbleData.type2(bubbleText);
        maid.getChatBubbleManager().addChatBubble(textChatBubbleData);
    }

    public static void makeResultsBubble(EntityMaid maid, MaidRec maidRec) {
        makeResultsBubble(maid, maidRec.result(), maidRec.time());
    }

    public static MutableComponent getBubbleText(EntityMaid maid, ItemStack food) {
        LivingEntity owner = maid.getOwner();
        String ownerName = owner == null ? "主人" : owner.getDisplayName().getString();
        String name = food.getHoverName().getString();
        int count = food.getCount();

        int type = maid.getRandom().nextInt(4);
        return switch (type) {
            case 0 -> VComponent.literal("是时候给" + ownerName + "炒" + count + "份" + name + "爱心便当了~");
            case 1 -> VComponent.literal("是时候来" + count + "份").append(name).append("了！");
            case 2 -> VComponent.literal(name).append("好吃么，来" + count + "份看看~");
            case 3 -> VComponent.literal(count + "份").append(name).append("，启动！");
            default -> VComponent.literal("是时候给主人炒" + count + "份" + name + "爱心便当了~");
        };

    }
}
