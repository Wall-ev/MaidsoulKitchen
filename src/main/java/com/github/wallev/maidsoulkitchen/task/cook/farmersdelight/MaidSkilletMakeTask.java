package com.github.wallev.maidsoulkitchen.task.cook.farmersdelight;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.wallev.maidsoulkitchen.entity.passive.IAddonMaid;
import com.github.wallev.maidsoulkitchen.init.MkMemories;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor.IRecipeExperinceAward;
import com.github.wallev.maidsoulkitchen.task.cook.common.inventory.MaidRecipesManager;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Collections;

/**
 * </ul>
 */
@TaskClassAnalyzer(TaskInfo.FD_SKILLET)
public class MaidSkilletMakeTask extends Behavior<EntityMaid> {
    private final TaskFdSkillet task;
    private int cookCount = 0;
    private final MaidRecipesManager<CampfireCookingRecipe> maidRecipesManager;
    private int tickCounter = 0;

    public MaidSkilletMakeTask(TaskFdSkillet task, MaidRecipesManager<CampfireCookingRecipe> maidRecipesManager) {
        super(ImmutableMap.of(InitEntities.TARGET_POS.get(), MemoryStatus.VALUE_PRESENT), 1200);
        this.task = task;
        this.maidRecipesManager = maidRecipesManager;
    }

    public static void playSound(EntityMaid maid, Level level, SoundEvent event) {
        Vec3 pos = maid.position();
        double x = pos.x() + 0.5;
        double y = pos.y();
        double z = pos.z() + 0.5;
        level.playLocalSound(x, y, z, event, SoundSource.BLOCKS, 0.4F, level.random.nextFloat() * 0.2F + 0.9F, false);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid maid) {
        if (maid != this.maidRecipesManager.getMaid()) {
            return false;
        }
        
        Brain<EntityMaid> brain = maid.getBrain();
        return brain.getMemory(InitEntities.TARGET_POS.get()).map(targetPos -> {
            Vec3 targetV3d = targetPos.currentPosition();
            return maid.distanceToSqr(targetV3d) <= Math.pow(task.getCloseEnoughDist(), 2);
        }).orElse(false);
    }

    @Override
    protected boolean canStillUse(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        return maid.getBrain().hasMemoryValue(InitEntities.TARGET_POS.get());
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        super.start(worldIn, maid, pGameTime);
        // 确保配方管理器已更新并准备好
        if (!this.maidRecipesManager.checkAndCreateRecipesIngredients()) {
            this.stop(worldIn, maid, pGameTime);
            return;
        }
        
        this.maidRecipesManager.getCookInv().syncInv();
        
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            BlockEntity blockEntity = worldIn.getBlockEntity(posWrapper.currentBlockPosition());
            if (blockEntity instanceof SkilletBlockEntity skilletBlockEntity) {

                if (skilletBlockEntity.hasStoredStack() || !skilletBlockEntity.isHeated()) {
                    return;
                }
                
                // 寻找配方中的原料进行烹饪
                attemptCooking(maid, skilletBlockEntity);
            }
        });
    }

    private void attemptCooking(EntityMaid maid, SkilletBlockEntity skilletBlockEntity) {
        // 确保配方管理器已经更新
        if (!this.maidRecipesManager.checkAndCreateRecipesIngredients()) {
            return;
        }


        CombinedInvWrapper maidInventory = maid.getAvailableInv(true);
        
        // 首先尝试使用女仆的配方管理器中的配方
        if (!maidRecipesManager.getRecipesIngredients().isEmpty()) {
            var recipeIngredient = maidRecipesManager.getRecipeIngredient();
            if (!recipeIngredient.getFirst().isEmpty() && !recipeIngredient.getSecond().isEmpty()) {
                var itemStacks = recipeIngredient.getSecond().get(0);
                if (!itemStacks.isEmpty()) {
                    ItemStack cookingItem = itemStacks.get(0);
                    if (!cookingItem.isEmpty()) {

                        WeakReference<FakePlayer> fakePlayer$tlma = ((IAddonMaid) maid).tlmk$getFakePlayer();
                        FakePlayer fakePlayer = fakePlayer$tlma.get();
                        if (fakePlayer != null) {
                            // 计算烹饪数量
                            cookCount = Math.max(cookingItem.getCount(),cookingItem.getMaxStackSize());
                            cookingItem = cookingItem.split(cookCount);
                            skilletBlockEntity.addItemToCook(cookingItem, fakePlayer);
                            maid.swing(InteractionHand.MAIN_HAND);
                            playSound(maid, maid.level, ModSounds.BLOCK_SKILLET_ADD_FOOD.get());
                            // 同步物品库存
                            this.maidRecipesManager.getCookInv().syncInv();
                            return;
                        }
                    }
                }
            }
        }
        
        // 如果配方管理器中没有合适的配方，则尝试直接使用女仆背包中的物品
        for (CampfireCookingRecipe recipe : getRecipes()) {
            var ingredient = recipe.getIngredients().get(0);
            
            for (int i = 0; i < maidInventory.getSlots(); i++) {
                ItemStack stackInSlot = maidInventory.getStackInSlot(i);
                if (!stackInSlot.isEmpty() && ingredient.test(stackInSlot)) {
                    // 找到匹配的物品
                    WeakReference<FakePlayer> fakePlayer$tlma = ((IAddonMaid) maid).tlmk$getFakePlayer();
                    FakePlayer fakePlayer = fakePlayer$tlma.get();
                    if (fakePlayer != null) {
                        cookCount = Math.max(stackInSlot.getCount(),stackInSlot.getMaxStackSize());
                        ItemStack cookingItem = stackInSlot.split(cookCount);
                        // 添加到煎锅中
                        skilletBlockEntity.addItemToCook(cookingItem, fakePlayer);
                    }
                    maid.swing(InteractionHand.MAIN_HAND);
                    playSound(maid, maid.level, ModSounds.BLOCK_SKILLET_ADD_FOOD.get());

                    // 同步物品库存
                    this.maidRecipesManager.getCookInv().syncInv();
                    return;
                }
            }
        }
    }
    
    /**
     * 获取所有可用的篝火烹饪配方
     * @return 篝火烹饪配方列表
     */
    private List<CampfireCookingRecipe> getRecipes() {
        if (maidRecipesManager.getMaid().level instanceof ServerLevel serverLevel) {
            return task.getRecipes(serverLevel);
        }
        return Collections.emptyList();
    }

    @Override
    protected void tick(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        tickCounter++;
        // 每秒检查一次
        if (tickCounter % 20 != 0) return;
        
        maid.getBrain().getMemory(InitEntities.TARGET_POS.get()).ifPresent(posWrapper -> {
            BlockPos blockPos = posWrapper.currentBlockPosition();
            BlockEntity blockEntity = worldIn.getBlockEntity(blockPos);
            
            if (blockEntity instanceof SkilletBlockEntity skilletBlockEntity) {
                // 如果正在烹饪中
                if (skilletBlockEntity.hasStoredStack() || cookCount > 0) {
                    // 如果有食物烹饪完毕
                    if (skilletBlockEntity.getStoredStack().getCount() < cookCount) {
                        playSound(maid, worldIn, ModSounds.BLOCK_SKILLET_ADD_FOOD.get());

                        // 给予经验奖励
                        if (blockEntity instanceof IRecipeExperinceAward) {
                            ((IRecipeExperinceAward) blockEntity).tlmk$awardExperience(maid);
                        }

                        // 同步物品库存并转移到箱子
                        this.maidRecipesManager.getCookInv().syncInv();
                        this.maidRecipesManager.tranOutput2Chest();
                        this.maidRecipesManager.getCookInv().syncInv();
                        cookCount = skilletBlockEntity.getStoredStack().getCount();
                    } else {
                        // 烹饪中，可以添加动画或者声音效果
                        if (tickCounter % 60 == 0) {  // 每3秒播放一次声音
                            playSound(maid, worldIn, ModSounds.BLOCK_SKILLET_SIZZLE.get());
                            maid.swing(InteractionHand.MAIN_HAND);
                        }
                    }
                } else if (!((SkilletBlockEntity) blockEntity).hasStoredStack()) {
                    cookCount = 0;
                    // 如果没有在烹饪，尝试开始新的烹饪
                    if (skilletBlockEntity.isHeated()) {
                        attemptCooking(maid, skilletBlockEntity);
                    }
                }
            }
        });
    }

    @Override
    protected void stop(ServerLevel worldIn, EntityMaid maid, long pGameTime) {
        super.stop(worldIn, maid, pGameTime);
        maid.getBrain().eraseMemory(MkMemories.DESTROY_POS.get());
        maid.getBrain().eraseMemory(InitEntities.TARGET_POS.get());
        maid.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        cookCount = 0;
        this.tickCounter = 0;
        
        // 确保在停止任务时同步物品库存
        this.maidRecipesManager.getCookInv().syncInv();
    }
}
