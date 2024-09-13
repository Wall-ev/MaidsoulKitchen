package com.github.catbert.tlma.api;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.MenuProvider;

public interface ILittleMaidTask extends IMaidTask {

    default MenuProvider getGuiProvider(EntityMaid maid, int entityId) {
        return maid.getMaidBackpackType().getGuiProvider(maid.getId());
    }
}
