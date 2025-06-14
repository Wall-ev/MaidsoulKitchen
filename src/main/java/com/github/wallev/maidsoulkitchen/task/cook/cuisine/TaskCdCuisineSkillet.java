//package com.github.wallev.maidsoulkitchen.task.cook.cuisine;
//
//import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
//import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
//import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
//import com.github.wallev.maidsoulkitchen.client.tooltip.RecipeDataTooltip;
//import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
//import com.github.wallev.maidsoulkitchen.init.MkItems;
//import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
//import com.github.wallev.maidsoulkitchen.mixin.cuisinedelight.CookingDataAccessor;
//import com.github.wallev.maidsoulkitchen.mixin.cuisinedelight.CookingEntryAccessor;
//import com.github.wallev.maidsoulkitchen.task.TaskInfo;
//import com.github.wallev.maidsoulkitchen.task.cook.common.ai.MaidCookMoveTask;
//import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager;
//import com.github.wallev.verhelper.server.ai.VBehaviorControl;
//import com.google.common.collect.Lists;
//import com.mojang.datafixers.util.Pair;
//import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
//import dev.xkmc.cuisinedelight.content.logic.CookingData;
//import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
//import dev.xkmc.cuisinedelight.content.logic.transform.Stage;
//import dev.xkmc.cuisinedelight.content.recipe.BaseCuisineRecipe;
//import dev.xkmc.cuisinedelight.content.recipe.CuisineRecipeMatch;
//import dev.xkmc.cuisinedelight.init.registrate.CDBlocks;
//import dev.xkmc.cuisinedelight.init.registrate.CDItems;
//import dev.xkmc.cuisinedelight.init.registrate.CDMisc;
//import net.minecraft.core.NonNullList;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.util.Mth;
//import net.minecraft.world.inventory.tooltip.TooltipComponent;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.items.wrapper.CombinedInvWrapper;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class TaskCdCuisineSkillet implements ICookTask<CuisineSkilletBlockEntity, BaseCuisineRecipe<?>> {
//    @Override
//    public boolean isCookBE(BlockEntity blockEntity) {
//        return blockEntity instanceof CuisineSkilletBlockEntity;
//    }
//
//    @Override
//    public RecipeType<BaseCuisineRecipe<?>> getRecipeType() {
//        return CDMisc.RT_CUISINE.get();
//    }
//
//    @Override
//    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, CuisineSkilletBlockEntity blockEntity, MaidRecipesManager<BaseCuisineRecipe<?>> recManager) {
//        CombinedInvWrapper maidAvailableInv = maid.getAvailableInv(true);
//        if (!blockEntity.isCooking() && blockEntity.canCook()
//                && ItemsUtil.findStackSlot(maidAvailableInv, stack -> stack.is(CDItems.SPATULA.get())) > -1
//                && ItemsUtil.findStackSlot(maidAvailableInv, stack -> stack.is(CDItems.PLATE.get())) > -1
//                && !recManager.getRecipesIngredients().isEmpty()) {
//            return true;
//        }
//
//        if (canExtractFood(serverLevel, maid, blockEntity, recManager)) {
//            return true;
//        }
//
//        return false;
//    }
//
//    public boolean canExtractFood(ServerLevel serverLevel, EntityMaid maid, CuisineSkilletBlockEntity blockEntity, MaidRecipesManager<BaseCuisineRecipe<?>> recManager) {
//        CombinedInvWrapper maidAvailableInv = maid.getAvailableInv(true);
//        CookingData cookingData = blockEntity.cookingData;
//        List<CookingData.CookingEntry> contents = cookingData.contents;
//        if (!contents.isEmpty() && ItemsUtil.findStackSlot(maidAvailableInv, stack -> stack.is(CDItems.PLATE.get())) > -1) {
//
//            for (CookingData.CookingEntry content : contents) {
//                Stage stage = content.getStage(cookingData);
//                if (stage == Stage.COOKED) {
//
//                }
//            }
//
//            boolean isCook = false;
//            for (CookingData.CookingEntry entry : contents) {
//                ItemStack food = entry.getItem();
//                IngredientConfig.IngredientEntry config = IngredientConfig.get().getEntry(food);
//                if (config != null) {
//                    float cook_needle = Mth.clamp(this.getDuration(cookingData, entry, maid) / 400.0F, 0.0F, 1.0F);
//                    if (cook_needle < 1) {
//                        isCook = true;
//                        break;
//                    }
//                }
//            }
//
//            return !isCook;
//        }
//
//        return false;
//    }
//
//    public float getDuration(CookingData data, CookingData.CookingEntry cookingEntry, EntityMaid maid) {
//        return (maid.level.getGameTime() - ((CookingEntryAccessor) cookingEntry).tlmk$getStartTime()) * ((CookingDataAccessor) data).tlmk$getSpeed();
//    }
//
//    @Override
//    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, CuisineSkilletBlockEntity blockEntity, MaidRecipesManager<BaseCuisineRecipe<?>> recManager) {
//
//    }
//
//    @Override
//    public ResourceLocation getUid() {
//        return TaskInfo.CD_CUISINE_SKILLET.uid;
//    }
//
//    @Override
//    public ItemStack getIcon() {
//        return CDBlocks.SKILLET.asStack();
//    }
//
//    @Override
//    public TaskDataKey<CookData> getCookDataKey() {
//        return DataRegister.CUISINE_SKILLET;
//    }
//
//    @Override
//    public NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
//        NonNullList<Ingredient> nonNullList = NonNullList.create();
//        List<Ingredient> list = ((BaseCuisineRecipe<?>) recipe).list.stream().map(CuisineRecipeMatch::ingredient).toList();
//        nonNullList.addAll(list);
//        return nonNullList;
//    }
//
//    @Override
//    public List<Pair<Integer, VBehaviorControl>> vCreateBrainTasks(EntityMaid maid) {
//        MaidRecipesManager<BaseCuisineRecipe<?>> cookingPotRecipeMaidRecipesManager = createRecipesManager(maid);
//        MaidCookMoveTask<CuisineSkilletBlockEntity, BaseCuisineRecipe<?>> maidCookMoveTask = new MaidCookMoveTask<>(this, cookingPotRecipeMaidRecipesManager);
//        MaidCuisineMakeTask maidCookMakeTask = new MaidCuisineMakeTask(this, cookingPotRecipeMaidRecipesManager);
//        return Lists.newArrayList(Pair.of(5, maidCookMoveTask), Pair.of(6, maidCookMakeTask));
//    }
//
//
//    @OnlyIn(Dist.CLIENT)
//    public Optional<TooltipComponent> getRecClientAmountTooltip(Recipe<?> recipe, boolean modeIsBlacklist, boolean overSize, CookData cookData, EntityMaid maid) {
//        List<Ingredient> ingres = this.getIngredients(recipe);
//
//        List<List<RecipeDataTooltip.IngredientSourceType>> source = new ArrayList<>();
//        source.add(List.of(RecipeDataTooltip.IngredientSourceType.MAIN_HAND, RecipeDataTooltip.IngredientSourceType.OFF_HAND, RecipeDataTooltip.IngredientSourceType.MAID_BACKPACK));
//        source.add(List.of(RecipeDataTooltip.IngredientSourceType.HUB_INGREDIENT));
//        int ruleMatchIndex = maid.getMaidInv().getStackInSlot(4).is(MkItems.CULINARY_HUB.get()) ? 1 : 0;
//        RecipeDataTooltip.TooltipRecIngredient tooltipRecIngredient = new RecipeDataTooltip.TooltipRecIngredient(ingres, source, RecipeDataTooltip.IngredientType.MANDATORY, ruleMatchIndex);
//
//        List<Ingredient> waters = List.of(Ingredient.of(CDItems.SPATULA.get()), Ingredient.of(CDItems.PLATE.get()));
//        List<List<RecipeDataTooltip.IngredientSourceType>> waterSources = new ArrayList<>();
//        waterSources.add(Lists.newArrayList(RecipeDataTooltip.IngredientSourceType.MAIN_HAND, RecipeDataTooltip.IngredientSourceType.OFF_HAND, RecipeDataTooltip.IngredientSourceType.MAID_BACKPACK));
//        int containerRuleMatchIndex = 0;
//        RecipeDataTooltip.TooltipRecIngredient tooltipRecContainerSources = new RecipeDataTooltip.TooltipRecIngredient(waters, waterSources, RecipeDataTooltip.IngredientType.MANDATORY, containerRuleMatchIndex);
//
//        RecipeDataTooltip.TooltipRecIngredient tooltipRecResultIngredient = getTooltipRecResultIngredient(recipe, maid);
//        RecipeDataTooltip.TooltipRecipeData tooltipRecipeData = new RecipeDataTooltip.TooltipRecipeData(cookData, recipe.getId().toString(), List.of(tooltipRecIngredient, tooltipRecContainerSources), tooltipRecResultIngredient, modeIsBlacklist, overSize);
//        return Optional.of(tooltipRecipeData);
//    }
//}
