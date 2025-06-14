//package com.github.wallev.maidsoulkitchen.task.cook.minersdelight;
//
//import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
//import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
//import com.github.wallev.maidsoulkitchen.task.TaskInfo;
//import com.github.wallev.maidsoulkitchen.task.cook.common.TaskFdPot;
//import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
//import com.sammy.minersdelight.content.block.copper_pot.CopperPotBlockEntity;
//import com.sammy.minersdelight.logic.CupConversionReloadListener;
//import com.sammy.minersdelight.setup.MDBlocks;
//import com.sammy.minersdelight.setup.MDItems;
//import net.minecraft.core.RegistryAccess;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.items.ItemStackHandler;
//import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
//import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
//
//import java.util.List;
//import java.util.Optional;
//
//
//public class TaskMdCopperPot extends TaskFdPot<CopperPotBlockEntity, CookingPotRecipe> {
//    @Override
//    public boolean isCookBE(BlockEntity blockEntity) {
//        return blockEntity instanceof CopperPotBlockEntity;
//    }
//
//    @Override
//    public RecipeType<CookingPotRecipe> getRecipeType() {
//        return ModRecipeTypes.COOKING.get();
//    }
//
//    @Override
//    public ResourceLocation getUid() {
//        return TaskInfo.MD_COOK_POT.uid;
//    }
//
//    @Override
//    public ItemStack getIcon() {
//        return MDBlocks.COPPER_POT.asStack();
//    }
//
//    @Override
//    public List<CookingPotRecipe> getRecipes(Level level) {
//        return super.getRecipes(level).stream().filter(cookingPotRecipe -> {
//            return cookingPotRecipe.getIngredients().size() <= 4;
//        }).toList();
//    }
//
//    @Override
//    public int getOutputSlot() {
//        return CopperPotBlockEntity.OUTPUT_SLOT;
//    }
//
//    @Override
//    public int getInputSize() {
//        return 4;
//    }
//
//    @Override
//    public ItemStackHandler getBeInv(CopperPotBlockEntity copperPotBlockEntity) {
//        return copperPotBlockEntity.getInventory();
//    }
//
//    @Override
//    public int getMealStackSlot() {
//        return CopperPotBlockEntity.MEAL_DISPLAY_SLOT;
//    }
//
//    @Override
//    public int getContainerStackSlot() {
//        return CopperPotBlockEntity.CONTAINER_SLOT;
//    }
//
//    @Override
//    public ItemStack getFoodContainer(CopperPotBlockEntity blockEntity) {
//        return blockEntity.getMeal().isEmpty() ? ItemStack.EMPTY : blockEntity.getContainer();
//    }
//
//    @Override
//    public ItemStackHandler getItemStackHandler(CopperPotBlockEntity be) {
//        return be.getInventory();
//    }
//
//    @Override
//    public boolean isHeated(CopperPotBlockEntity be) {
//        return be.isHeated();
//    }
//
//    @Override
//    public TaskDataKey<CookData> getCookDataKey() {
//        return DataRegister.MD_COPPER_POT;
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public List<Ingredient> getContainers(CookingPotRecipe rec) {
//        ItemStack bowlItem = super.getResultItem(rec, null);
//        boolean cupServed = CupConversionReloadListener.BOWL_TO_CUP.containsKey(bowlItem.getItem());
//        ItemStack mealContainerStack = cupServed ? MDItems.COPPER_CUP.asStack() : rec.getOutputContainer();
//        if (mealContainerStack.isEmpty()){
//            return List.of();
//        }
//
//        return List.of(Ingredient.of(mealContainerStack));
//    }
//
//    @Override
//    public ItemStack getResultItem(Recipe<?> recipe, RegistryAccess pRegistryAccess) {
//        ItemStack bowlItem = super.getResultItem(recipe, pRegistryAccess);
//        return Optional.ofNullable(CupConversionReloadListener.BOWL_TO_CUP.get(bowlItem.getItem()))
//                .map(Item::getDefaultInstance)
//                .orElse(bowlItem);
//    }
//}
