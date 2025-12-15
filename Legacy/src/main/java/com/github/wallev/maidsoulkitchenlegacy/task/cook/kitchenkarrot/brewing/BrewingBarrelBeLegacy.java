package com.github.wallev.maidsoulkitchenlegacy.task.cook.kitchenkarrot.brewing;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.kitchencarrot.brewing.BrewingBarrelBe;

public class BrewingBarrelBeLegacy extends BrewingBarrelBe {
    public BrewingBarrelBeLegacy(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean cookStateMatch() {
        return be.hasEnoughWater();
    }

    @Override
    public boolean hasFluid() {
        return be.hasEnoughWater();
    }
}
