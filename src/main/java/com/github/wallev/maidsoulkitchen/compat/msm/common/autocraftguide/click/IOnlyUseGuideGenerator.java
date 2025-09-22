package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.click;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.List;

public interface IOnlyUseGuideGenerator<R extends Recipe<? extends Container>> extends ICookingRecipeGuideGenerator<R> {
//public interface IOnlyUseGuideGenerator<R extends Recipe<C>, C extends Container> extends ICookingRecipeGuideGenerator<R, C> {

    @Override
    default void generateSteps(BlockPos pos, Level level, R recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        // 右键放置原料
        ItemStack cup = realItems.remove(0);
        craftGuide.addItemUse(cup);

        CraftGuideOperator2.forEachSingleItem(realItems, itemStack -> {
            craftGuide.addItemUse(pos.above(), itemStack);
        });

        // 右键拾取成品
        craftGuide.addEmptyUse(pos.above(), outputs);
    }
}
