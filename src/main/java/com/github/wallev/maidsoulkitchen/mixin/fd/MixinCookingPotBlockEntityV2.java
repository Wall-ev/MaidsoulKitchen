package com.github.wallev.maidsoulkitchen.mixin.fd;

import com.github.wallev.maidsoulkitchen.handler.base.mkcontainer.ICookBeAccessor;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.Optional;

@Mixin(value = CookingPotBlockEntity.class, remap = false)
public abstract class MixinCookingPotBlockEntityV2 implements ICookBeAccessor<CookingPotBlockEntity, CookingPotRecipe> {
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
        RecipeWrapper inventoryWrapper = new RecipeWrapper(this.getInventory());
        Optional<CookingPotRecipe> matchingRecipe = this.getMatchingRecipe(inventoryWrapper);
        return matchingRecipe.isPresent() && this.canCook(matchingRecipe.get());
    }

    @Shadow
    protected abstract Optional<CookingPotRecipe> getMatchingRecipe(RecipeWrapper inventoryWrapper);

    @Shadow
    protected abstract boolean canCook(CookingPotRecipe recipe);

    @Shadow public abstract ItemStackHandler getInventory();
}
