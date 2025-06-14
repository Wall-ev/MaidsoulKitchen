//package com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight;
//
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
//import com.github.wallev.maidsoulkitchen.util.MemoryUtil;
//import com.github.wallev.verhelper.server.ai.VBehaviorControl;
//import com.github.wallev.verhelper.server.item.VItemStack;
//import com.github.wallev.maidsoulkitchen.init.MkEntities;
//import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager;
//import com.google.common.collect.ImmutableMap;
//import com.mao.barbequesdelight.content.block.GrillBlockEntity;
//import com.mao.barbequesdelight.content.recipe.GrillingRecipe;
//import com.mao.barbequesdelight.init.registrate.BBQDItems;
//import com.mojang.datafixers.util.Pair;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.entity.ai.Brain;
//import net.minecraft.world.entity.ai.behavior.Behavior;
//import net.minecraft.world.entity.ai.memory.MemoryStatus;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.items.IItemHandlerModifiable;
//import net.minecraftforge.items.ItemHandlerHelper;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MaidGrillMakeTask extends Behavior<EntityMaid> implements VBehaviorControl {
//    private final TaskBdGrill task;
//    private final MaidRecipesManager<GrillingRecipe<?>> maidRecipesManager;
//    private final List<ItemStack> grillStacks = new ArrayList<>();
//
//    public MaidGrillMakeTask(TaskBdGrill task, MaidRecipesManager<GrillingRecipe<?>> maidRecipesManager) {
//        super(ImmutableMap.of(MkEntities.WORK_POS.get(), MemoryStatus.VALUE_PRESENT), 1200);
//        this.task = task;
//        this.maidRecipesManager = maidRecipesManager;
//    }
//
//    @Override
//    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
//        Brain<EntityMaid> brain = maid.getBrain();
//        return brain.getMemory(MkEntities.WORK_POS.get()).map(targetPos -> {
//            Vec3 targetV3d = targetPos.currentPosition();
//            return !(maid.distanceToSqr(targetV3d) > Math.pow(task.getCloseEnoughDist(), 2));
//        }).orElse(false);
//    }
//
//    @Override
//    protected boolean canStillUse(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
//        return maid.getBrain().hasMemoryValue(MkEntities.WORK_POS.get());
//    }
//
//    @Override
//    protected void start(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
//        super.start(worldIn, maid, pGameTime);
//        MemoryUtil.getWorkPos(maid).ifPresent(posWrapper -> {
//            BlockEntity blockEntity = worldIn.getBlockEntity(posWrapper.currentBlockPosition());
//            if (blockEntity instanceof GrillBlockEntity grillBlockEntity) {
//                if (!maidRecipesManager.getRecipesIngredients().isEmpty()) {
//                    Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = maidRecipesManager.getRecipeIngredient();
//                    grillStacks.addAll(recipeIngredient.getSecond().get(0));
//                }
//
//                this.maidRecipesManager.tranOutput2Chest();
//                this.maidRecipesManager.syncInv();
//            }
//        });
//    }
//
//    @Override
//    protected void tick(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
//        MemoryUtil.getWorkPos(maid).ifPresent(posWrapper -> {
//            BlockEntity blockEntity = worldIn.getBlockEntity(posWrapper.currentBlockPosition());
//            if (blockEntity instanceof GrillBlockEntity grillBlockEntity) {
//                IItemHandlerModifiable outputInv = maidRecipesManager.getOutputInv();
//
//                boolean nothing = true;
//                GrillBlockEntity.ItemEntry[] itemEntries = grillBlockEntity.entries;
//                for (GrillBlockEntity.ItemEntry itemEntry : itemEntries) {
//                    ItemStack stack = itemEntry.stack;
//                    if (stack.is(BBQDItems.BURNT_FOOD.asItem())) {
//                        ItemStack leftStack = ItemHandlerHelper.insertItemStacked(outputInv, stack.copy(), false);
//                        stack.shrink(stack.getCount() - leftStack.getCount());
//                        grillBlockEntity.inventoryChanged();
//
//                        nothing = false;
//                    } else if (!stack.isEmpty()) {
//                        // 要翻转了
//                        if (itemEntry.canFlip()) {
//                            itemEntry.flip(grillBlockEntity);
//                            maid.swing(InteractionHand.MAIN_HAND);
//
//                        }
//
//                        // 熟了，可以取出来了
//                        if (itemEntry.flipped && itemEntry.time >= itemEntry.duration) {
//                            ItemStack leftStack = ItemHandlerHelper.insertItemStacked(outputInv, stack.copy(), false);
//                            stack.shrink(stack.getCount() - leftStack.getCount());
//                            grillBlockEntity.inventoryChanged();
//                        }
//
//                        nothing = false;
//                    } else {
//                        if (!grillStacks.isEmpty()) {
//                            ItemStack grillStack = grillStacks.get(0);
//
//                            if (!grillStack.isEmpty() && itemEntry.addItem(grillBlockEntity, VItemStack.copyWithCount(grillStack, 1))) {
//                                maid.swing(InteractionHand.MAIN_HAND);
//                                grillBlockEntity.inventoryChanged();
//                                grillStack.shrink(1);
//                                nothing = false;
//                            }
//
//                        }
//
//                    }
//                }
//
//                if (nothing) {
//                    this.stop(worldIn, maid, pGameTime);
//                    return;
//                }
//
//            }
//        });
//    }
//
//    @Override
//    protected void stop(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
//        super.stop(worldIn, maid, pGameTime);
//        this.maidRecipesManager.tranOutput2Chest();
//        this.maidRecipesManager.syncInv();
//        MemoryUtil.eraseWorkPos(maid);
//        grillStacks.clear();
//    }
//}
