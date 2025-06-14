//package com.github.wallev.maidsoulkitchen.task.cook.farmersdelight;
//
//import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
//import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
//import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
//import com.github.wallev.maidsoulkitchen.client.tooltip.RecipeDataTooltip;
//import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
//import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
//import com.github.wallev.maidsoulkitchen.task.TaskInfo;
//import com.github.wallev.maidsoulkitchen.task.cook.common.ai.MaidCookMoveTask;
//import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager;
//import com.github.wallev.verhelper.client.chat.VComponent;
//import com.github.wallev.verhelper.server.ai.VBehaviorControl;
//import com.google.common.collect.Lists;
//import com.mojang.datafixers.util.Pair;
//import net.minecraft.ChatFormatting;
//import net.minecraft.core.NonNullList;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.items.IItemHandler;
//import net.minecraftforge.items.IItemHandlerModifiable;
//import net.minecraftforge.items.ItemHandlerHelper;
//import net.minecraftforge.items.ItemStackHandler;
//import net.minecraftforge.items.wrapper.CombinedInvWrapper;
//import net.minecraftforge.items.wrapper.RecipeWrapper;
//import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
//import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
//import vectorwing.farmersdelight.common.registry.ModBlocks;
//import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
//
//import java.util.*;
//import java.util.function.Consumer;
//
//public class TaskFdCuttingBoard implements ICookTask<CuttingBoardBlockEntity, CuttingBoardRecipe> {
//    @Override
//    public boolean isCookBE(BlockEntity blockEntity) {
//        return blockEntity instanceof CuttingBoardBlockEntity;
//    }
//
//    @Override
//    public RecipeType<CuttingBoardRecipe> getRecipeType() {
//        return ModRecipeTypes.CUTTING.get();
//    }
//
//    @Override
//    public List<Pair<Integer, VBehaviorControl>> vCreateBrainTasks(EntityMaid maid) {
//        if (maid.level.isClientSide) {
//            return Collections.emptyList();
//        }
//
//        MaidRecipesManager<CuttingBoardRecipe> cookingPotRecipeMaidRecipesManager = createRecipesManager(maid);
//        MaidCookMoveTask<CuttingBoardBlockEntity, CuttingBoardRecipe> maidCookMoveTask = new MaidCookMoveTask<>(this, cookingPotRecipeMaidRecipesManager);
//        MaidCuttingMakeTask maidCookMakeTask = new MaidCuttingMakeTask(this, cookingPotRecipeMaidRecipesManager);
//        return Lists.newArrayList(Pair.of(5, maidCookMoveTask), Pair.of(6, maidCookMakeTask));
//    }
//
//    @Override
//    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, CuttingBoardBlockEntity blockEntity, MaidRecipesManager<CuttingBoardRecipe> recManager) {
//        if (!blockEntity.isEmpty() && hasBoardStackTool(maid, blockEntity)) {
//            return true;
//        }
//
//        if (blockEntity.getStoredItem().isEmpty() && !recManager.getRecipesIngredients().isEmpty()) {
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, CuttingBoardBlockEntity blockEntity, MaidRecipesManager<CuttingBoardRecipe> recManager) {
//
//    }
//
//    private boolean hasBoardStackTool(EntityMaid maid, CuttingBoardBlockEntity blockEntity) {
//        return !this.getBoardStackTool(maid, blockEntity).isEmpty();
//    }
//
//    private ItemStack getBoardStackTool(EntityMaid maid, CuttingBoardBlockEntity blockEntity) {
//        Level level = maid.level;
//        CombinedInvWrapper maidInv = maid.getAvailableInv(true);
//
//        IItemHandler inventory = blockEntity.getInventory();
//        RecipeWrapper recipeWrapper = new RecipeWrapper((IItemHandlerModifiable) inventory);
//        Optional<CuttingBoardRecipe> recipe = level.getRecipeManager().getRecipeFor(ModRecipeTypes.CUTTING.get(), recipeWrapper, level);
//
//        if (recipe.isPresent()) {
//            Ingredient tool = recipe.get().getTool();
//            return ItemsUtil.getStack(maidInv, (itemStack) -> {
//                return tool.test(itemStack);
//            });
//        }
//
//        return ItemStack.EMPTY;
//    }
//
//    public ItemStack swapItem(InteractionHand hand, ItemStack itemStack, EntityMaid maid, IItemHandler inv) {
//        ItemStack swapItemCopy = itemStack.copyAndClear();
//
//        ItemStack handItem = maid.getItemInHand(hand);
//        ItemStack leftStack = ItemHandlerHelper.insertItemStacked(inv, handItem, false);
//        maid.setItemInHand(hand, swapItemCopy);
//        if (!leftStack.isEmpty()) {
//            maid.level.addFreshEntity(new ItemEntity(maid.level, maid.getX(), maid.getY(), maid.getZ(), leftStack));
//        }
//        return swapItemCopy;
//    }
//
//    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, CuttingBoardBlockEntity blockEntity, MaidRecipesManager<CuttingBoardRecipe> recManager, Consumer<Item> item) {
//        if (!blockEntity.isEmpty()) {
//            ItemStack boardStackTool = getBoardStackTool(maid, blockEntity);
//            if (!boardStackTool.isEmpty()) {
//                CombinedInvWrapper maidInv = maid.getAvailableInv(true);
//                ItemStack tool = this.swapItem(InteractionHand.MAIN_HAND, boardStackTool, maid, maidInv);
//                blockEntity.processStoredItemUsingTool(tool, null);
//                maid.swing(InteractionHand.MAIN_HAND);
//            }
//        }
//
//        if (blockEntity.getStoredItem().isEmpty() && !recManager.getRecipesIngredients().isEmpty()) {
//            Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = recManager.getRecipeIngredient();
//            if (recipeIngredient.getFirst().isEmpty()) return;
//
//            ItemStackHandler availableInv = maid.getMaidInv();
//
//            List<ItemStack> itemStacks = recipeIngredient.getSecond().get(0);
//            for (ItemStack itemStack : itemStacks) {
//                if (!itemStack.isEmpty()) {
//                    ItemStack swapItem = swapItem(InteractionHand.OFF_HAND, itemStack, maid, availableInv);
//                    item.accept(swapItem.getItem());
//                    break;
//                }
//            }
//
//            List<ItemStack> toolStacks = recipeIngredient.getSecond().get(1);
//            for (ItemStack itemStack : toolStacks) {
//                if (!itemStack.isEmpty()) {
//                    swapItem(InteractionHand.MAIN_HAND, itemStack, maid, availableInv);
//                    break;
//                }
//            }
//
//        }
//    }
//
//    @Override
//    public MaidRecipesManager<CuttingBoardRecipe> createRecipesManager(EntityMaid maid) {
//        return new MaidRecipesManager<>(maid, this) {
//            @Override
//            protected List<Pair<List<Integer>, List<Item>>> createIngres(Map<Item, Integer> available, boolean setRecipeIngres) {
////                ItemStackHandler availableInv = maid.getMaidInv();
////                boolean hasAvi = false;
////                for (int i = 0; i < availableInv.getSlots(); i++) {
////                    if (availableInv.getStackInSlot(i).isEmpty()) {
////                        hasAvi = true;
////                        break;
////                    }
////                }
////                if (!hasAvi) return Collections.emptyList();
//                return super.createIngres(available, setRecipeIngres);
//            }
//
//            @Override
//            protected boolean enableHub() {
//                return false;
//            }
//        };
//    }
//
//    @Override
//    public ResourceLocation getUid() {
//        return TaskInfo.FD_CUTTING_BOARD.uid;
//    }
//
//    @Override
//    public ItemStack getIcon() {
//        return ModBlocks.CUTTING_BOARD.get().asItem().getDefaultInstance();
//    }
//
//    @Override
//    public TaskDataKey<CookData> getCookDataKey() {
//        return DataRegister.FD_CUTTING_BOARD;
//    }
//
//    @Override
//    public NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
//        CuttingBoardRecipe cuttingBoardRecipe = (CuttingBoardRecipe) recipe;
//        NonNullList<Ingredient> ingredients = cuttingBoardRecipe.getIngredients();
//        ingredients.add(cuttingBoardRecipe.getTool());
//        return ingredients;
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public List<Component> getWarnComponent() {
//        return List.of(VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.info.warn").withStyle(ChatFormatting.YELLOW),
//                VComponent.translatable("gui.maidsoulkitchen.btn.cook_guide.info.warn.cuttingboard"));
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public RecipeDataTooltip.TooltipRecIngredient getTooltipRecResultIngredient(Recipe<?> recipe, EntityMaid maid) {
//        List<List<RecipeDataTooltip.IngredientSourceType>> result = new ArrayList<>();
//        result.add(List.of(RecipeDataTooltip.IngredientSourceType.PICKUP));
//        int resultRuleMatchIndex = 0;
//        RecipeDataTooltip.TooltipRecIngredient tooltipRecResultIngredient = new RecipeDataTooltip.TooltipRecIngredient(List.of(Ingredient.of(this.getResultItem(recipe, maid.level.registryAccess()))), result, RecipeDataTooltip.IngredientType.OUTPUT, resultRuleMatchIndex);
//        return tooltipRecResultIngredient;
//    }
//}
