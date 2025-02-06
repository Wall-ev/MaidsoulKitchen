package com.github.wallev.maidsoulkitchen.task.cook.v1.barbequesdelight;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.wallev.maidsoulkitchen.init.MkEntities;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.google.common.collect.ImmutableMap;
import com.mao.barbequesdelight.content.block.BasinBlockEntity;
import com.mao.barbequesdelight.content.recipe.SkeweringRecipe;
import com.mao.barbequesdelight.init.registrate.BBQDRecipes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public class MaidBasinMakeTask extends Behavior<EntityMaid> {
    private final TaskBdBasin task;
    private final MaidRecipesManager<SkeweringRecipe<?>> maidRecipesManager;
    private int tick;

    private int time;
    private ItemStack container = ItemStack.EMPTY;
    private ItemStack tool = ItemStack.EMPTY;
    private ItemStack side = ItemStack.EMPTY;

    public MaidBasinMakeTask(TaskBdBasin task, MaidRecipesManager<SkeweringRecipe<?>> maidRecipesManager) {
        super(ImmutableMap.of(InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_PRESENT), 1200);
        this.task = task;
        this.maidRecipesManager = maidRecipesManager;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
        Brain<EntityMaid> brain = maid.getBrain();
        return brain.getMemory(InitEntities.TARGET_POS.get()).map(targetPos -> {
            Vec3 targetV3d = targetPos.currentPosition();
            return !(maid.distanceToSqr(targetV3d) > Math.pow(task.getCloseEnoughDist(), 2));
        }).orElse(false);
    }

    @Override
    protected boolean canStillUse(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        return maid.getBrain().hasMemoryValue(InitEntities.TARGET_POS.get());
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        super.start(worldIn, maid, pGameTime);
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            BlockEntity blockEntity = worldIn.getBlockEntity(posWrapper.currentBlockPosition());
            if (blockEntity instanceof BasinBlockEntity basinBlockEntity) {
                IItemHandlerModifiable inputInv = maidRecipesManager.getInputInv();

                if (!basinBlockEntity.items.isEmpty()) {
                    for (ItemStack itemStack : basinBlockEntity.items.getAsList()) {
                        ItemStack leftStack = ItemHandlerHelper.insertItemStacked(inputInv, itemStack.copy(), false);
                        itemStack.shrink(itemStack.getCount() - leftStack.getCount());
                    }
                }

                if (!basinBlockEntity.items.isEmpty()) {
                    return;
                }

                if (!maidRecipesManager.getRecipesIngredients().isEmpty()) {
                    Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = maidRecipesManager.getRecipeIngredient();


                    time = recipeIngredient.getFirst().get(0);
                    List<List<ItemStack>> second = recipeIngredient.getSecond();
                    ItemStack containerStack = second.get(0).get(0);
                    basinBlockEntity.items.addItem(containerStack);
                    containerStack.setCount(0);

                    container = basinBlockEntity.items.getItem(0);
                    tool = second.get(1).get(0);
                    if (second.size() > 2) {
                        side = second.get(2).get(0);
                    }

                }

                this.maidRecipesManager.getCookInv().syncInv();
            }
        });
    }

    @Override
    protected void tick(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        if (tick++ % 5 != 0) return;
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            BlockEntity blockEntity = worldIn.getBlockEntity(posWrapper.currentBlockPosition());
            if (blockEntity instanceof BasinBlockEntity basinBlockEntity) {
                IItemHandlerModifiable outputInv = maidRecipesManager.getOutputInv();

                var cont = new SimpleContainer(tool, container, side);
                var optional = worldIn.getRecipeManager().getRecipeFor(BBQDRecipes.RT_SKR.get(), cont, worldIn);
                if (optional.isEmpty()) {
                    this.stop(worldIn, maid, pGameTime);
                    basinBlockEntity.notifyTile();
                    this.time = 0;
                    this.tool = ItemStack.EMPTY;
                    this.container = ItemStack.EMPTY;
                    this.side = ItemStack.EMPTY;
                    return;
                }
                SkeweringRecipe<?> recipe = optional.get();
                ItemStack ret = recipe.assemble(cont, worldIn.registryAccess());
                ItemHandlerHelper.insertItemStacked(outputInv, ret, false);
                maid.swing(InteractionHand.MAIN_HAND);

                basinBlockEntity.notifyTile();

            }
        });
    }


    @Override
    protected void stop(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        super.stop(worldIn, maid, pGameTime);
        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
        maid.getBrain().eraseMemory(MkEntities.WORK_POS.get());
    }
}
