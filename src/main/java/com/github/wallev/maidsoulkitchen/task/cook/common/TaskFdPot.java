//package com.github.wallev.maidsoulkitchen.task.cook.common;
//
//import com.github.wallev.maidsoulkitchen.api.task.cook.IFdPotCook;
//import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
//import com.github.wallev.maidsoulkitchen.client.tooltip.RecipeDataTooltip;
//import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
//import com.github.wallev.maidsoulkitchen.init.MkItems;
//import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager;
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
//import com.google.common.collect.Lists;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.Container;
//import net.minecraft.world.inventory.tooltip.TooltipComponent;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public abstract class TaskFdPot<B extends BlockEntity, R extends Recipe<? extends Container>> implements ICookTask<B, R>, IFdPotCook<B, R> {
//    @Override
//    public boolean shouldMoveTo(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> manager) {
//        return maidShouldMoveTo(level, maid, be, manager);
//    }
//
//    @Override
//    public void processCookMake(ServerLevel level, EntityMaid maid, B be, MaidRecipesManager<R> manager) {
//        maidCookMake(level, maid, be, manager);
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public Optional<TooltipComponent> getRecClientAmountTooltip(Recipe<?> recipe, boolean modeIsBlacklist, boolean overSize, CookData cookData, EntityMaid maid) {
//        List<Ingredient> ingres = this.getIngredients(recipe);
//
//        List<List<RecipeDataTooltip.IngredientSourceType>> ingreSources = new ArrayList<>();
//        ingreSources.add(Lists.newArrayList(RecipeDataTooltip.IngredientSourceType.MAIN_HAND, RecipeDataTooltip.IngredientSourceType.OFF_HAND, RecipeDataTooltip.IngredientSourceType.MAID_BACKPACK));
//        ingreSources.add(Lists.newArrayList(RecipeDataTooltip.IngredientSourceType.HUB_INGREDIENT));
//        int sourceRuleMatchIndex = maid.getMaidInv().getStackInSlot(4).is(MkItems.CULINARY_HUB.get()) ? 1 : 0;
//        RecipeDataTooltip.TooltipRecIngredient tooltipRecIngredient = new RecipeDataTooltip.TooltipRecIngredient(ingres, ingreSources, RecipeDataTooltip.IngredientType.MANDATORY, sourceRuleMatchIndex);
//
//        List<Ingredient> outputContainers = getContainers((R) recipe);
//        RecipeDataTooltip.TooltipRecIngredient tooltipRecResultIngredient = getTooltipRecResultIngredient(recipe, maid);
//
//        if (outputContainers.isEmpty()) {
//            RecipeDataTooltip.TooltipRecipeData tooltipRecipeData = new RecipeDataTooltip.TooltipRecipeData(cookData, recipe.getId().toString(), List.of(tooltipRecIngredient), tooltipRecResultIngredient, modeIsBlacklist, overSize);
//            return Optional.of(tooltipRecipeData);
//        }
//
//        List<List<RecipeDataTooltip.IngredientSourceType>> containerSources = new ArrayList<>();
//        containerSources.add(Lists.newArrayList(RecipeDataTooltip.IngredientSourceType.MAIN_HAND, RecipeDataTooltip.IngredientSourceType.OFF_HAND, RecipeDataTooltip.IngredientSourceType.MAID_BACKPACK));
//        containerSources.add(Lists.newArrayList(RecipeDataTooltip.IngredientSourceType.HUB_OUTPUT_ADDITION));
//        int containerRuleMatchIndex = maid.getMaidInv().getStackInSlot(4).is(MkItems.CULINARY_HUB.get()) ? 1 : 0;
//        RecipeDataTooltip.TooltipRecIngredient tooltipRecContainerSources = new RecipeDataTooltip.TooltipRecIngredient(outputContainers, containerSources, RecipeDataTooltip.IngredientType.MAYBE, containerRuleMatchIndex);
//
//        RecipeDataTooltip.TooltipRecipeData tooltipRecipeData = new RecipeDataTooltip.TooltipRecipeData(cookData, recipe.getId().toString(), List.of(tooltipRecIngredient, tooltipRecContainerSources), tooltipRecResultIngredient, modeIsBlacklist, overSize);
//        return Optional.of(tooltipRecipeData);
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public abstract List<Ingredient> getContainers(R rec);
//}
