package com.github.catbert.tlma.api;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;

public interface ILittleMaidBackpack extends ILittleMaid{
    @Override
    default void addMaidBackpack(BackpackManager manager) {
        manager.add((IMaidBackpack) this);
    }
}
