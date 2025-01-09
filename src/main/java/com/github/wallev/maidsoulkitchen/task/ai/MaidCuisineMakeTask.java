package com.github.wallev.maidsoulkitchen.task.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.entity.passive.IAddonMaid;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.wallev.maidsoulkitchen.task.cook.v1.cuisine.TaskCuisineSkillet;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.cuisinedelight.content.block.CuisineSkilletBlockEntity;
import dev.xkmc.cuisinedelight.content.logic.CookedFoodData;
import dev.xkmc.cuisinedelight.content.logic.CookingData;
import dev.xkmc.cuisinedelight.content.logic.IngredientConfig;
import dev.xkmc.cuisinedelight.content.recipe.BaseCuisineRecipe;
import dev.xkmc.cuisinedelight.init.registrate.CDItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import vectorwing.farmersdelight.common.registry.ModSounds;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MaidCuisineMakeTask extends Behavior<EntityMaid> {
    private final TaskCuisineSkillet task;
    private final MaidRecipesManager<BaseCuisineRecipe<?>> maidRecipesManager;
    private int tickAll = 0;
    private int tickMax = 0;
    private int tickSpace = Integer.MAX_VALUE;
    private final List<Pair<Integer, ItemStack>> processTickStacks = new ArrayList<>();
    private ItemStack plateItem = ItemStack.EMPTY;
    private boolean end = false;

    public MaidCuisineMakeTask(TaskCuisineSkillet task, MaidRecipesManager<BaseCuisineRecipe<?>> maidRecipesManager) {
        super(ImmutableMap.of(InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_PRESENT), 1200);
        this.task = task;
        this.maidRecipesManager = maidRecipesManager;
    }

    private static int getReduction(ItemStack stack) {
        return stack.getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0 ? 20 : 0;
    }

    public static void playSound(EntityMaid maid, Level level, SoundEvent event) {
        Vec3 pos = maid.position();
        double x = pos.x() + 0.5;
        double y = pos.y();
        double z = pos.z() + 0.5;
        level.playLocalSound(x, y, z, event, SoundSource.BLOCKS, 0.4F, level.random.nextFloat() * 0.2F + 0.9F, false);
    }

    private static void processV1(ServerLevel worldIn, EntityMaid maid, CuisineSkilletBlockEntity cuisineSkilletBlockEntity) {
        if (!cuisineSkilletBlockEntity.cookingData.contents.isEmpty()) {
            if (!worldIn.isClientSide()) {
                cuisineSkilletBlockEntity.stir(worldIn.getGameTime(), getReduction(maid.getMainHandItem()));
            } else {
                playSound(maid, worldIn, ModSounds.BLOCK_SKILLET_SIZZLE.get());
            }
            maid.swing(InteractionHand.MAIN_HAND);
        }
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
        Brain<EntityMaid> brain = maid.getBrain();
        return brain.getMemory(InitEntities.TARGET_POS.get()).map(targetPos -> {
            Vec3 targetV3d = targetPos.currentPosition();
            if (maid.distanceToSqr(targetV3d) > Math.pow(task.getCloseEnoughDist(), 2)) {
                return false;
            }
            return true;
        }).orElse(false);
    }

    @Override
    protected boolean canStillUse(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        return maid.getBrain().hasMemoryValue(InitEntities.TARGET_POS.get()) && !end;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        super.start(worldIn, maid, pGameTime);
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            BlockEntity blockEntity = worldIn.getBlockEntity(posWrapper.currentBlockPosition());
            if (blockEntity instanceof CuisineSkilletBlockEntity cuisineSkilletBlockEntity) {
                CombinedInvWrapper maidAvailableInv = maid.getAvailableInv(true);
                ItemStack mainHandItem = maid.getMainHandItem();
                if (!mainHandItem.is(CDItems.SPATULA.get())) {
                    int stackSlot = ItemsUtil.findStackSlot(maidAvailableInv, itemStack -> itemStack.is(CDItems.SPATULA.get()));
                    if (stackSlot == -1) return;
                    ItemStack leftStack = ItemHandlerHelper.insertItemStacked(maidAvailableInv, mainHandItem, false);
                    if (!leftStack.isEmpty()) return;
                    maid.setItemInHand(InteractionHand.MAIN_HAND, maidAvailableInv.getStackInSlot(stackSlot));
                }

                int plateSlot = ItemsUtil.findStackSlot(maidAvailableInv, itemStack -> itemStack.is(CDItems.PLATE.get()));
                if (plateSlot > -1) {
                    plateItem = maidAvailableInv.getStackInSlot(plateSlot);
                } else {
                    return;
                }


                Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = this.maidRecipesManager.getRecipeIngredient();
                for (List<ItemStack> itemStacks : recipeIngredient.getSecond()) {
                    for (ItemStack itemStack : itemStacks) {
                        if (!itemStack.isEmpty()) {
                            IngredientConfig.IngredientEntry entry = IngredientConfig.get().getEntry(itemStack);
                            if (entry != null) {
                                tickMax = Math.max(tickMax, entry.min_time);
                                tickSpace = Math.min(tickSpace, entry.stir_time);
                                processTickStacks.add(Pair.of(entry.min_time, itemStack));
                            }
                            break;
                        }
                    }
                }
                processTickStacks.sort((a, b) -> a.getFirst() == 0 ? -1 : Integer.compare(b.getFirst(), a.getFirst()));

                WeakReference<FakePlayer> fakePlayer$tlma = ((IAddonMaid) maid).getFakePlayer$tlma();
                FakePlayer fakePlayer = fakePlayer$tlma.get();
                if (fakePlayer != null) {
                    Integer time = processTickStacks.get(0).getFirst();
                    List<Pair<Integer, ItemStack>> list = processTickStacks.stream().filter(pair -> pair.getFirst() == time || pair.getFirst() == tickMax).toList();
                    processTickStacks.removeAll(list);
                    for (Pair<Integer, ItemStack> integerItemStackPair : list) {
                        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, integerItemStackPair.getSecond().split(1));
                        this.interactUseOnBlock(maid, blockEntity.getBlockPos(), InteractionHand.MAIN_HAND, null);
                        maid.swing(InteractionHand.MAIN_HAND);
                    }

//                    processV1(worldIn, maid, cuisineSkilletBlockEntity);
                }

                this.maidRecipesManager.getCookInv().syncInv();
            }
        });
    }

    @Override
    protected void tick(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        tickAll++;
        super.tick(worldIn, maid, pGameTime);
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            BlockEntity blockEntity = worldIn.getBlockEntity(posWrapper.currentBlockPosition());
            if (blockEntity instanceof CuisineSkilletBlockEntity cuisineSkilletBlockEntity) {

                if ((tickAll + 10) % tickSpace == 0) {
                    processV1(worldIn, maid, cuisineSkilletBlockEntity);
                }


                if (!processTickStacks.isEmpty()) {
                    Pair<Integer, ItemStack> pair = processTickStacks.get(0);
                    if (tickAll + 10 == tickMax - pair.getFirst()) {
                        WeakReference<FakePlayer> fakePlayer$tlma = ((IAddonMaid) maid).getFakePlayer$tlma();
                        FakePlayer fakePlayer = fakePlayer$tlma.get();
                        if (fakePlayer != null) {
                            Integer time = pair.getFirst();
                            List<Pair<Integer, ItemStack>> list = processTickStacks.stream().filter(pair1 -> pair1.getFirst() == time || pair1.getFirst() == tickMax).toList();
                            processTickStacks.removeAll(list);
                            for (Pair<Integer, ItemStack> integerItemStackPair : list) {
                                fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, integerItemStackPair.getSecond().split(1));
                                this.interactUseOnBlock(maid, blockEntity.getBlockPos(), InteractionHand.MAIN_HAND, null);
                                maid.swing(InteractionHand.MAIN_HAND);
                            }
                        }
                    }
                }


                if (tickAll - 10 >= tickMax) {
                    CombinedInvWrapper maidAvailableInv = maid.getAvailableInv(true);

                    CookingData data = cuisineSkilletBlockEntity.cookingData;
                    data.stir(worldIn.getGameTime(), 0);
                    CookedFoodData food = new CookedFoodData(data);
                    ItemStack foodStack = BaseCuisineRecipe.findBestMatch(worldIn, food);
                    plateItem.shrink(1);
                    ItemHandlerHelper.insertItemStacked(maidAvailableInv, foodStack, false);

                    cuisineSkilletBlockEntity.cookingData = new CookingData();
                    cuisineSkilletBlockEntity.sync();

                    maid.swing(InteractionHand.MAIN_HAND);

                    this.end = true;
                }

            }
        });
    }

    @Override
    protected void stop(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        super.stop(worldIn, maid, pGameTime);
        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        this.tickAll = 0;
        this.tickMax = 0;
        this.tickSpace = Integer.MAX_VALUE;
        this.processTickStacks.clear();
        this.plateItem = ItemStack.EMPTY;
        this.end = false;
    }


    private InteractionResult interactUseOnBlock(EntityMaid maid, BlockPos targetPos, InteractionHand hand, @Nullable Direction facing) {
        FakePlayer fakePlayer = ((IAddonMaid) maid).getFakePlayer$tlma().get();
        Direction placementOn = (facing == null) ? fakePlayer.getMotionDirection() : facing;
        BlockHitResult blockraytraceresult = new BlockHitResult(
                fakePlayer.getLookAngle(), placementOn,
                targetPos, true);
        //processRightClick
        ItemStack itemInHand = fakePlayer.getItemInHand(hand);
        return fakePlayer.gameMode.useItemOn(fakePlayer, maid.level(), itemInHand, hand, blockraytraceresult);
    }
}
