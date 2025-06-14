//package com.github.wallev.maidsoulkitchen.task.cook.minecraft;
//
//import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
//import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
//import com.github.wallev.maidsoulkitchen.client.tooltip.RecipeDataTooltip;
//import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
//import com.github.wallev.maidsoulkitchen.entity.passive.IMaidsoulKitchenMaid;
//import com.github.wallev.maidsoulkitchen.init.MkItems;
//import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
//import com.github.wallev.maidsoulkitchen.task.TaskInfo;
//import com.github.wallev.maidsoulkitchen.task.cook.common.TaskBaseContainerCook;
//import com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor.IAbstractFurnaceAccessor;
//import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager;
//import com.google.common.collect.Lists;
//import com.mojang.datafixers.util.Pair;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.Container;
//import net.minecraft.world.inventory.tooltip.TooltipComponent;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.crafting.AbstractCookingRecipe;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.common.ForgeHooks;
//import net.minecraftforge.items.IItemHandlerModifiable;
//import net.minecraftforge.items.ItemHandlerHelper;
//import net.minecraftforge.items.wrapper.CombinedInvWrapper;
//import net.minecraftforge.registries.ForgeRegistries;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//
//
///**
// * todo
// * 临时解决，等待版本重构
// */
//public class TaskFurnace extends TaskBaseContainerCook<AbstractFurnaceBlockEntity, AbstractCookingRecipe> {
//    public List<ItemStack> fuels = new ArrayList<>();
//
//    @Override
//    public boolean isHeated(AbstractFurnaceBlockEntity be) {
//        return true;
//    }
//
//    @Override
//    public boolean beInnerCanCook(Container inventory, AbstractFurnaceBlockEntity be) {
//        return false;
//    }
//
//    @Override
//    public int getOutputSlot() {
//        return 2;
//    }
//
//    @Override
//    public int getInputSize() {
//        return 1;
//    }
//
//    @Override
//    public Container getContainer(AbstractFurnaceBlockEntity be) {
//        return be;
//    }
//
//    @Override
//    public boolean isCookBE(BlockEntity blockEntity) {
//        return blockEntity instanceof AbstractFurnaceBlockEntity;
//    }
//
//    @Override
//    @SuppressWarnings("unchecked, rawtypes")
//    public RecipeType<AbstractCookingRecipe> getRecipeType() {
//        return (RecipeType) RecipeType.SMELTING;
//    }
//
//    @Override
//    public ResourceLocation getUid() {
//        return TaskInfo.FURNACE.uid;
//    }
//
//    @Override
//    public ItemStack getIcon() {
//        return Items.FURNACE.getDefaultInstance();
//    }
//
//    @Override
//    public List<AbstractCookingRecipe> getRecipes(Level level) {
//        if (fuels.isEmpty()) {
//            for (Item value : ForgeRegistries.ITEMS.getValues()) {
//                ItemStack itemStack = new ItemStack(value);
//                if (ForgeHooks.getBurnTime(itemStack, null) > 0) {
//                    fuels.add(itemStack);
//                }
//            }
//        }
//
//        HashSet<RecipeType<? extends AbstractCookingRecipe>> recipeTypes = new HashSet<>();
//        recipeTypes.add(RecipeType.SMOKING);
//        recipeTypes.add(RecipeType.SMELTING);
//        recipeTypes.add(RecipeType.BLASTING);
//
//        List<AbstractCookingRecipe> recs = new ArrayList<>();
//        for (RecipeType<? extends AbstractCookingRecipe> canRecipeType : recipeTypes) {
//            List<? extends AbstractCookingRecipe> recipesFor = level.getRecipeManager().getAllRecipesFor(canRecipeType);
//            recs.addAll(recipesFor);
//        }
//        return recs;
//    }
//
//    @Override
//    public boolean maidShouldMoveTo(ServerLevel serverLevel, EntityMaid maid, AbstractFurnaceBlockEntity furnace, MaidRecipesManager<AbstractCookingRecipe> maidRecManager) {
//        IAbstractFurnaceAccessor furnaceAccessor = (IAbstractFurnaceAccessor) furnace;
//        AbstractCookingRecipesManager recManager = (AbstractCookingRecipesManager) maidRecManager;
//
//        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
//        boolean cooking = furnaceAccessor.tlmk$isLit() && furnaceAccessor.tlmk$innerCanCook();
//        boolean b = furnaceRecMatch(serverLevel, furnace);
//        boolean findFuel = ItemsUtil.findStackSlot(availableInv, itemStack -> ForgeHooks.getBurnTime(itemStack, null) > 0) > -1;
//
//        boolean canBurn = furnaceAccessor.tlmk$isLit() || !furnace.getItem(1).isEmpty() || findFuel;
//
//        if (!furnace.getItem(getOutputSlot()).isEmpty()) {
//            return true;
//        }
//
//        if (!cooking && !b && canBurn && recManager.hasRecipeIngredientsWithTemp(furnaceAccessor.tlmk$getRecipeType())) {
//            return true;
//        }
//
//        if (!cooking && !b && hasInput(furnace)) {
//            return true;
//        }
//
//        if (cooking && furnace.getItem(1).isEmpty() && findFuel) {
//            return true;
//        }
//
//        ItemStack fuel = furnace.getItem(1);
//        if (!furnaceAccessor.tlmk$innerCanCook() && !b && !furnace.getItem(1).isEmpty()) {
//            return ItemHandlerHelper.insertItemStacked(availableInv, fuel, true).isEmpty();
//        }
//
//        return false;
//    }
//
//    private boolean furnaceRecMatch(ServerLevel serverLevel, AbstractFurnaceBlockEntity blockEntity) {
//        return false;
//    }
//
//    @Override
//    public void maidCookMake(ServerLevel serverLevel, EntityMaid maid, AbstractFurnaceBlockEntity blockEntity, MaidRecipesManager<AbstractCookingRecipe> maidRecipesManager) {
//        AbstractCookingRecipesManager recManager = (AbstractCookingRecipesManager) maidRecipesManager;
//
//        extract(serverLevel, maid, blockEntity, recManager);
//        insert(serverLevel, maid, blockEntity, recManager);
//
//        recManager.syncInv();
//    }
//
//    private void extract(ServerLevel serverLevel, EntityMaid maid, AbstractFurnaceBlockEntity furnace, AbstractCookingRecipesManager recManager) {
//        IAbstractFurnaceAccessor furnaceAccessor = (IAbstractFurnaceAccessor) furnace;
//
//        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
//        AbstractFurnaceBlockEntity beInv = furnace;
//
//        IItemHandlerModifiable ingreInv = recManager.getInputInv();
//        IItemHandlerModifiable outputInv = recManager.getOutputInv();
//        boolean cooking = furnaceAccessor.tlmk$isLit() && furnaceAccessor.tlmk$innerCanCook();
//        boolean b = furnaceRecMatch(serverLevel, furnace);
//
//        boolean findFuel = ItemsUtil.findStackSlot(availableInv, itemStack -> ForgeHooks.getBurnTime(itemStack, null) > 0) > -1;
//
//        boolean canBurn = furnaceAccessor.tlmk$isLit() || !beInv.getItem(1).isEmpty() || findFuel;
//
//        if (!beInv.getItem(getOutputSlot()).isEmpty()) {
//            extractOutputStack(beInv, outputInv, furnace);
//            furnace.setChanged();
//        }
//
//        if (!cooking && !b && hasInput(beInv)) {
//            extractInputStack(beInv, ingreInv, furnace);
//            furnace.setChanged();
//        }
//        IMaidsoulKitchenMaid.pickupAction(maid);
//
//    }
//
//    private void insert(ServerLevel serverLevel, EntityMaid maid, AbstractFurnaceBlockEntity furnace, AbstractCookingRecipesManager recManager) {
//        IAbstractFurnaceAccessor furnaceAccessor = (IAbstractFurnaceAccessor) furnace;
//
//        CombinedInvWrapper availableInv = maid.getAvailableInv(true);
//        AbstractFurnaceBlockEntity beInv = furnace;
//
//        int stackSlot = ItemsUtil.findStackSlot(availableInv, itemStack -> ForgeHooks.getBurnTime(itemStack, null) > 0);
//        boolean findFuel = stackSlot > -1;
//
//
//        if (beInv.getItem(1).isEmpty() && findFuel) {
//            ItemStack stackInSlot = availableInv.getStackInSlot(stackSlot);
//            insertAndShrink(furnace, stackInSlot.getCount(), List.of(List.of(stackInSlot)), 0, 1);
//            furnace.setChanged();
//        }
//
//        boolean canBurn = furnaceAccessor.tlmk$isLit() || !beInv.getItem(1).isEmpty();
//        boolean cooking = furnaceAccessor.tlmk$isLit() && furnaceAccessor.tlmk$innerCanCook();
//        boolean b = furnaceRecMatch(serverLevel, furnace);
//        if (!cooking && !b && canBurn && recManager.hasRecipeIngredientsWithTemp(furnaceAccessor.tlmk$getRecipeType())) {
//            Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = recManager.getRecipeIngredient(furnaceAccessor.tlmk$getRecipeType());
//            if (recipeIngredient.getFirst().isEmpty()) return;
//            insertInputStack(beInv, availableInv, furnace, recipeIngredient);
//            furnace.setChanged();
//        }
//
//        ItemStack fuel = furnace.getItem(1);
//        if (!furnaceAccessor.tlmk$innerCanCook() && !b && !fuel.isEmpty()) {
//            if (ItemHandlerHelper.insertItemStacked(availableInv, fuel, true).isEmpty()) {
//                ItemHandlerHelper.insertItemStacked(availableInv, furnace.removeItem(1, fuel.getCount()), false);
//                furnace.setChanged();
//            }
//        }
//        IMaidsoulKitchenMaid.pickupAction(maid);
//
//    }
//
//    @Override
//    public TaskDataKey<CookData> getCookDataKey() {
//        return DataRegister.MC_FURNACE;
//    }
//
//    @Override
//    public AbstractCookingRecipesManager createRecipesManager(EntityMaid maid) {
//        return new AbstractCookingRecipesManager(maid, this);
//    }
//
//    @Override
//    public boolean hasEnoughFavor(EntityMaid maid) {
//        return true;
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public Optional<TooltipComponent> getRecClientAmountTooltip(Recipe<?> recipe, boolean modeIsBlacklist, boolean overSize, CookData cookData, EntityMaid maid) {
//        List<Ingredient> ingres = this.getIngredients(recipe);
//
//        List<List<RecipeDataTooltip.IngredientSourceType>> source = new ArrayList<>();
//        source.add(Lists.newArrayList(RecipeDataTooltip.IngredientSourceType.MAID_BACKPACK));
//        source.add(Lists.newArrayList(RecipeDataTooltip.IngredientSourceType.HUB_INGREDIENT));
//        int ruleMatchIndex = maid.getMaidInv().getStackInSlot(4).is(MkItems.CULINARY_HUB.get()) ? 1 : 0;
//        RecipeDataTooltip.TooltipRecIngredient tooltipRecIngredient = new RecipeDataTooltip.TooltipRecIngredient(ingres, source, RecipeDataTooltip.IngredientType.MANDATORY, ruleMatchIndex);
//
//        List<List<RecipeDataTooltip.IngredientSourceType>> fuelsSourceTypes = new ArrayList<>();
//        fuelsSourceTypes.add(Lists.newArrayList(RecipeDataTooltip.IngredientSourceType.MAIN_HAND, RecipeDataTooltip.IngredientSourceType.OFF_HAND, RecipeDataTooltip.IngredientSourceType.MAID_BACKPACK));
//        int containerRuleMatchIndex = 0;
//        List<Ingredient> fuels = List.of(Ingredient.of(this.fuels.stream()));
//        RecipeDataTooltip.TooltipRecIngredient tooltipRecContainerSources = new RecipeDataTooltip.TooltipRecIngredient(fuels, fuelsSourceTypes, RecipeDataTooltip.IngredientType.MAYBE, containerRuleMatchIndex);
//
//        RecipeDataTooltip.TooltipRecIngredient tooltipRecResultIngredient = getTooltipRecResultIngredient(recipe, maid);
//        RecipeDataTooltip.TooltipRecipeData tooltipRecipeData = new RecipeDataTooltip.TooltipRecipeData(cookData, recipe.getId().toString(), List.of(tooltipRecIngredient, tooltipRecContainerSources), tooltipRecResultIngredient, modeIsBlacklist, overSize);
//        return Optional.of(tooltipRecipeData);
//    }
//}
