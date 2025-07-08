package com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record MaidItem(ItemDefinition item, int count) {

    public static final MaidItem EMPTY = new MaidItem(ItemDefinition.EMPTY, 0);
    public static final Codec<MaidItem> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            ItemDefinition.CODEC.fieldOf("item").forGetter(MaidItem::item),
            Codec.INT.fieldOf("count").forGetter(MaidItem::count)
    ).apply(ins, (o1, o2) -> new MaidItem(o1, o2)));

    public boolean isEmpty() {
        return this == EMPTY;
    }
}
