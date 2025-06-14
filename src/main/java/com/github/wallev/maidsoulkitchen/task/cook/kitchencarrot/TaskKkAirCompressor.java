//package com.github.wallev.maidsoulkitchen.task.cook.kitchencarrot;
//
//import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
//import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
//import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
//import com.github.wallev.maidsoulkitchen.api.task.cook.IHandlerCookBe;
//import com.github.wallev.maidsoulkitchen.api.task.cook.IItemHandlerCook;
//import com.github.wallev.maidsoulkitchen.client.tooltip.RecipeDataTooltip;
//import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
//import com.github.wallev.maidsoulkitchen.entity.passive.IMaidsoulKitchenMaid;
//import com.github.wallev.maidsoulkitchen.init.MkItems;
//import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
//import com.github.wallev.maidsoulkitchen.mixin.kitchkarrot.AirCompressorBlockEntityAccessor;
//import com.github.wallev.maidsoulkitchen.task.TaskInfo;
//import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager;
//import com.google.common.collect.Lists;
//import com.mojang.datafixers.util.Pair;
//import io.github.tt432.kitchenkarrot.blockentity.AirCompressorBlockEntity;
//import io.github.tt432.kitchenkarrot.recipes.recipe.AirCompressorRecipe;
//import io.github.tt432.kitchenkarrot.registries.ModBlocks;
//import io.github.tt432.kitchenkarrot.registries.RecipeTypes;
//import net.minecraft.core.NonNullList;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.inventory.tooltip.TooltipComponent;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.items.ItemHandlerHelper;
//import net.minecraftforge.items.ItemStackHandler;
//import net.minecraftforge.items.wrapper.CombinedInvWrapper;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class TaskKkAirCompressor implements ICookTask<AirCompressorBlockEntity, AirCompressorRecipe>, IHandlerCookBe<AirCompressorBlockEntity>, IItemHandlerCook<AirCompressorBlockEntity, AirCompressorRecipe> {
//    private static void replenishEnergy(EntityMaid maid, AirCompressorBlockEntity brewBe, CombinedInvWrapper maidInv) {
//        int energyItemSlot = ItemsUtil.findStackSlot(maidInv, stack -> stack.is(Items.REDSTONE));
//        if (energyItemSlot > -1) {
//            ItemStack stackInSlot = maidInv.getStackInSlot(energyItemSlot);
//            ItemHandlerHelper.insertItemStacked(brewBe.getInput2(), stackInSlot.split(1), false);
//        }
//    }
//
//    @Override
//    public TaskDataKey<CookData> getCookDataKey() {
//        return DataRegister.KC_AIR_COMPRESSOR;
//    }
//
//    @Override
//    public boolean isCookBE(BlockEntity blockEntity) {
//        return blockEntity instanceof AirCompressorBlockEntity;
//    }
//
//    @Override
//    public RecipeType<AirCompressorRecipe> getRecipeType() {
//        return RecipeTypes.AIR_COMPRESSOR.get();
//    }
//
//    @Override
//    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, AirCompressorBlockEntity blockEntity, MaidRecipesManager<AirCompressorRecipe> recManager) {
//        CombinedInvWrapper maidInv = maid.getAvailableInv(true);
//        AirCompressorBlockEntityAccessor accessor = (AirCompressorBlockEntityAccessor) blockEntity;
//
//        if (!blockEntity.getOutput().getStackInSlot(0).isEmpty()) {
//            return true;
//        }
//
//        boolean findChargeItem = ItemsUtil.findStackSlot(maidInv, stack -> stack.is(Items.REDSTONE)) > -1;
//        if (!accessor.callIsStarted() && !recManager.getRecipesIngredients().isEmpty()) {
//            return accessor.callHasEnergy() || findChargeItem;
//        }
//
//        if (accessor.callGetRecipeFromItems() != null && !accessor.callHasEnergy() && findChargeItem) {
//            return true;
//        }
//
//        if (!accessor.callIsStarted() && hasInput(blockEntity.getInput1())) {
//            return true;
//        }
//
//        return false;
//    }
//
//    @Override
//    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, AirCompressorBlockEntity blockEntity, MaidRecipesManager<AirCompressorRecipe> recManager) {
//        CombinedInvWrapper maidInv = maid.getAvailableInv(true);
//        AirCompressorBlockEntityAccessor accessor = (AirCompressorBlockEntityAccessor) blockEntity;
//
//        if (!accessor.callHasEnergy()) {
//            replenishEnergy(maid, blockEntity, maidInv);
//        }
//
//        if (!blockEntity.getOutput().getStackInSlot(0).isEmpty()) {
//            extractOutputStack(blockEntity.getOutput(), recManager.getOutputInv(), blockEntity);
//        }
//        IMaidsoulKitchenMaid.pickupAction(maid);
//
//        if (!accessor.callIsStarted() && hasInput(blockEntity.getInput1())) {
//            extractInputsStack(blockEntity.getInput1(), recManager.getInputInv(), blockEntity);
//        }
//
//        if (!accessor.callIsStarted() && accessor.callHasEnergy() && !recManager.getRecipesIngredients().isEmpty()) {
//            Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = recManager.getRecipeIngredient();
//            if (recipeIngredient.getFirst().isEmpty()) return;
//            insertInputsStack(blockEntity.getInput1(), maidInv, blockEntity, recipeIngredient);
//        }
//
//        IMaidsoulKitchenMaid.pickupAction(maid);
//        recManager.syncInv();
//    }
//
//    @Override
//    public ResourceLocation getUid() {
//        return TaskInfo.KK_AIR_COMPRESSOR.uid;
//    }
//
//    @Override
//    public ItemStack getIcon() {
//        return ModBlocks.AIR_COMPRESSOR.get().asItem().getDefaultInstance();
//    }
//
//    @Override
//    public ItemStackHandler getItemStackHandler(AirCompressorBlockEntity airCompressorBlockEntity) {
//        return airCompressorBlockEntity.getInput1();
//    }
//
//    @Override
//    public int getOutputSlot() {
//        return 0;
//    }
//
//    @Override
//    public int getInputSize() {
//        return 5;
//    }
//
//    @Override
//    public ItemStackHandler getBeInv(AirCompressorBlockEntity airCompressorBlockEntity) {
//        return airCompressorBlockEntity.getInput1();
//    }
//
//    @Override
//    public NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
//        AirCompressorRecipe airCompressorRecipe = (AirCompressorRecipe) recipe;
//        NonNullList<Ingredient> ingredient = airCompressorRecipe.getIngredient();
//        Ingredient container = airCompressorRecipe.getContainer();
//        if (container != null) {
//            NonNullList<Ingredient> ingredients = NonNullList.create();
//            ingredients.addAll(ingredient);
//            ingredients.add(container);
//            return ingredients;
//        } else {
//            return ingredient;
//        }
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
//        List<Ingredient> waters = List.of(Ingredient.of(Items.REDSTONE));
//        List<List<RecipeDataTooltip.IngredientSourceType>> waterSources = new ArrayList<>();
//        waterSources.add(Lists.newArrayList(RecipeDataTooltip.IngredientSourceType.MAIN_HAND, RecipeDataTooltip.IngredientSourceType.OFF_HAND, RecipeDataTooltip.IngredientSourceType.MAID_BACKPACK));
//        int containerRuleMatchIndex = 0;
//        RecipeDataTooltip.TooltipRecIngredient tooltipRecContainerSources = new RecipeDataTooltip.TooltipRecIngredient(waters, waterSources, RecipeDataTooltip.IngredientType.MAYBE, containerRuleMatchIndex);
//
//        RecipeDataTooltip.TooltipRecIngredient tooltipRecResultIngredient = getTooltipRecResultIngredient(recipe, maid);
//        RecipeDataTooltip.TooltipRecipeData tooltipRecipeData = new RecipeDataTooltip.TooltipRecipeData(cookData, recipe.getId().toString(), List.of(tooltipRecIngredient, tooltipRecContainerSources), tooltipRecResultIngredient, modeIsBlacklist, overSize);
//        return Optional.of(tooltipRecipeData);
//    }
//}
