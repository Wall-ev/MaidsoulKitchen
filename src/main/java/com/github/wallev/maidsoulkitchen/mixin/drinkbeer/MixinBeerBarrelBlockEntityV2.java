package com.github.wallev.maidsoulkitchen.mixin.drinkbeer;

import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.ICookBeAccessor;
import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = BeerBarrelBlockEntity.class, remap = false)
public class MixinBeerBarrelBlockEntityV2 implements ICookBeAccessor<BeerBarrelBlockEntity, BrewingRecipe> {

    @Shadow private int statusCode;

    /**
     * 判断厨具内部的原料是否可以烹饪
     * <br>即有符合配方的原料
     * <br>但不会检测额外条件
     * <br>比如：需要燃料，加水等
     *
     * @return 是否可以烹饪
     */
    @Override
    public boolean canCook$msk() {
        return this.statusCode != 0;
    }
}
