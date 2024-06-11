package com.catbert.tlma.api;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import net.minecraft.world.item.Item;

public interface ILittleMaidBauble extends ILittleMaid, IMaidBauble {

    @Override
    default void bindMaidBauble(BaubleManager manager) {
        manager.bind(getBindingItem(), this);
    }

    Item getBindingItem();

}
