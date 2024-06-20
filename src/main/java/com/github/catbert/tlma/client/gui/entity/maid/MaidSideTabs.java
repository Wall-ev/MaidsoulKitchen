package com.github.catbert.tlma.client.gui.entity.maid;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;

public class MaidSideTabs<T extends AbstractMaidContainer> {
    private final int entityId;
    private final int leftPos;
    private final int topPos;

    public MaidSideTabs(int entityId, int leftPos, int topPos) {
        this.entityId = entityId;
        this.leftPos = leftPos;
        this.topPos = topPos;
    }
}
