package com.github.wallev.maidsoulkitchenlegacy.task.cook.kitchenkarrot.brewing;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.legacy.task.AutoLegacyCookTaskRegister;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.kitchencarrot.brewing.TaskKkBrewingBarrel;
import io.github.tt432.kitchenkarrot.blockentity.BrewingBarrelBlockEntity;

@AutoLegacyCookTaskRegister(TaskInfo.KK_BREW_BARREL_LEGACY)
public class TaskKkBrewingBarrelLegacy extends TaskKkBrewingBarrel {
    @Override
    protected CookBeBase<BrewingBarrelBlockEntity> createCookBe(EntityMaid maid) {
        return new BrewingBarrelBeLegacy(maid);
    }

}
