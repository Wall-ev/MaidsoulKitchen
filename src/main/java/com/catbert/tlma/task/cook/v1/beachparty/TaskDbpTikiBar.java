//package com.catbert.tlma.task.cook.beachparty;
//
//import com.catbert.tlma.foundation.utility.Mods;
//import com.catbert.tlma.task.cook.v1.common.TaskNormalCook;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.items.ItemStackHandler;
//import net.minecraftforge.items.wrapper.RecipeWrapper;
//import org.jetbrains.annotations.Nullable;
//import satisfy.beachparty.block.entity.TikiBarBlockEntity;
//import satisfy.beachparty.recipe.TikiBarRecipe;
//
//import java.util.Optional;
//
////@LittleMaidExtension
//public class TaskDbpTikiBar extends TaskNormalCook<TikiBarBlockEntity, TikiBarRecipe> {
//    @Override
//    public boolean canLoaded() {
//        return Mods.DBP.isLoaded;
//    }
//
//    @Override
//    public ItemStackHandler getItemStackHandler(TikiBarBlockEntity be) {
//        return null;
//    }
//
//    @Override
//    public Optional<TikiBarRecipe> getMatchingRecipe(TikiBarBlockEntity be, RecipeWrapper recipeWrapper) {
//        return Optional.empty();
//    }
//
//    @Override
//    public boolean canCook(TikiBarBlockEntity be, TikiBarRecipe recipe) {
//        return false;
//    }
//
//    @Override
//    public int getOutputSlot() {
//        return 0;
//    }
//
//    @Override
//    public int getInputSize() {
//        return 0;
//    }
//
//    /**
//     * 判断厨具是否在烹任
//     *
//     * @param be     厨具
//     * @param recipe 厨具内容器对应的配方，可谓空
//     * @param level  Level
//     * @return 厨具内的物品是否可以烹饪
//     */
//    @Override
//    public boolean innerCanCraft(TikiBarBlockEntity be, @Nullable TikiBarRecipe recipe, Level level) {
//        return false;
//    }
//
//    @Override
//    public boolean isCookBE(BlockEntity blockEntity) {
//        return false;
//    }
//
//    @Override
//    public RecipeType<TikiBarRecipe> getRecipeType() {
//        return null;
//    }
//
//    @Override
//    public ResourceLocation getUid() {
//        return null;
//    }
//
//    @Override
//    public ItemStack getIcon() {
//        return null;
//    }
//}
