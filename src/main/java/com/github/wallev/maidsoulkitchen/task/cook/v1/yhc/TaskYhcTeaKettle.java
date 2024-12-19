package com.github.wallev.maidsoulkitchen.task.cook.v1.yhc;

import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.entity.passive.IAddonMaid;
import com.github.wallev.maidsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.maidsoulkitchen.mixin.yhc.KettleBlockAccessor;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.TaskFdPot;
import com.github.wallev.maidsoulkitchen.util.FakePlayerUtil;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class TaskYhcTeaKettle extends TaskFdPot<KettleBlockEntity, KettleRecipe> {
    @Override
    public ItemStackHandler getItemStackHandler(KettleBlockEntity be) {
        return be.getInventory();
    }

    @Override
    public int getOutputSlot() {
        return KettleBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getInputSize() {
        return 4;
    }

    @Override
    public ItemStackHandler getBeInv(KettleBlockEntity kettleBlockEntity) {
        return kettleBlockEntity.getInventory();
    }

    @Override
    public int getMealStackSlot() {
        return KettleBlockEntity.MEAL_DISPLAY_SLOT;
    }

    @Override
    public int getContainerStackSlot() {
        return KettleBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public ItemStack getFoodContainer(KettleBlockEntity blockEntity) {
        return blockEntity.getContainer();
    }

    @Override
    public boolean isHeated(KettleBlockEntity be) {
        return be.isHeated();
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof KettleBlockEntity;
    }

    @Override
    public RecipeType<KettleRecipe> getRecipeType() {
        return YHBlocks.KETTLE_RT.get();
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.YHC_TEA_KETTLE.uid;
    }

    @Override
    public ItemStack getIcon() {
        return YHBlocks.KETTLE.asStack();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.YHC_TEA_KETTLE;
    }

    @Override
    public boolean canCook(KettleBlockEntity be, KettleRecipe recipe) {
        return super.canCook(be, recipe) && be.getWater() > 0;
    }

    @Override
    public boolean shouldMoveTo(ServerLevel level, EntityMaid maid, KettleBlockEntity be, MaidRecipesManager<KettleRecipe> manager) {
        return super.shouldMoveTo(level, maid, be, manager);
    }

    @Override
    public void processCookMake(ServerLevel level, EntityMaid maid, KettleBlockEntity be, MaidRecipesManager<KettleRecipe> manager) {
        super.processCookMake(level, maid, be, manager);
    }

    @Override
    public void tryInsertItem(ServerLevel serverLevel, EntityMaid entityMaid, KettleBlockEntity blockEntity, MaidRecipesManager<KettleRecipe> maidRecipesManager) {
        if(this.needWater(blockEntity)) return;
        super.tryInsertItem(serverLevel, entityMaid, blockEntity, maidRecipesManager);
    }

    @Override
    public boolean maidShouldMoveTo(ServerLevel level, EntityMaid maid, KettleBlockEntity kettleBlockEntity, MaidRecipesManager<KettleRecipe> manager) {
        ItemStackHandler inventory = getItemStackHandler(kettleBlockEntity);
        ItemStack outputStack = inventory.getStackInSlot(getOutputSlot());
        // 有最终物品
        if (!outputStack.isEmpty()) {
            return true;
        }

        ItemStack mealStack = getBeInvMealStack(kettleBlockEntity, inventory);
        ItemStack container = getFoodContainer(kettleBlockEntity);
        boolean hasOutputAdditionItem = manager.hasOutputAdditionItem(container);
        // 有待取出物品和对应的容器
        if (!mealStack.isEmpty() && hasOutputAdditionItem) {
            return true;
        }

        boolean heated = isHeated(kettleBlockEntity);
        Optional<KettleRecipe> recipe = getMatchingRecipe(kettleBlockEntity, new RecipeWrapper(inventory));
        // 现在是否可以做饭（厨锅有没有正在做饭）
        boolean b = recipe.isPresent() && canCook(kettleBlockEntity, recipe.get());
        List<Pair<List<Integer>, List<List<ItemStack>>>> recipesIngredients = manager.getRecipesIngredients();
        if (!b && !recipesIngredients.isEmpty() && heated && (!needWater(kettleBlockEntity) || findMaidHasWaterResource(maid, kettleBlockEntity) != -1) && mealStack.isEmpty()) {
            return true;
        }

        // 能做饭现在和有输入（也就是厨锅现在有物品再里面但是不符合配方
        if (!b && hasInput(inventory)) {
            return true;
        }

        ItemStack containerInputStack = inventory.getStackInSlot(getContainerStackSlot());
        //当厨锅没有物品，又有杯具在时，就取出杯具
        if (!hasInput(inventory) && !containerInputStack.isEmpty()) {
            return true;
        }

        return false;
    }

    public boolean needWater(KettleBlockEntity kettleBlockEntity) {
        return kettleBlockEntity.getWater() <= 200;
    }

    @Override
    public void tryExtractItem(ServerLevel serverLevel, EntityMaid entityMaid, KettleBlockEntity blockEntity, MaidRecipesManager<KettleRecipe> maidRecipesManager) {
        this.replenishWater(entityMaid, blockEntity);
        super.tryExtractItem(serverLevel, entityMaid, blockEntity, maidRecipesManager);
    }

    private boolean replenishWater(EntityMaid entityMaid, KettleBlockEntity blockEntity) {
        if (this.needWater(blockEntity)) {
            int stackSlot = findMaidHasWaterResource(entityMaid, blockEntity);
            if (stackSlot != -1) {
                ItemStack waterStack = entityMaid.getAvailableInv(true).getStackInSlot(stackSlot);
                WeakReference<FakePlayer> fakePlayer$tlma = ((IAddonMaid) entityMaid).getFakePlayer$tlma();
                FakePlayer fakePlayer = fakePlayer$tlma.get();
                if (fakePlayer != null) {
                    fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, waterStack.split(1));
                    try {
                        InteractionResult interactionResult = FakePlayerUtil.interactUseOnBlock(fakePlayer$tlma, entityMaid.level(), blockEntity.getBlockPos(), InteractionHand.MAIN_HAND, null);

                        if (interactionResult != InteractionResult.PASS) {
                            ItemStack itemInHand = fakePlayer.getItemInHand(InteractionHand.MAIN_HAND);
                            ItemHandlerHelper.insertItemStacked(entityMaid.getAvailableInv(true), itemInHand.copy(), false);
                            fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                        } else {
                            fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                        }

                        if (interactionResult == InteractionResult.PASS) {
                            BlockState blockState = entityMaid.level.getBlockState(blockEntity.getBlockPos());
                            Block block = blockState.getBlock();
                        }
                    } catch (Exception e) {
                        return false;
                    }
                }
            }

        }
        return false;
    }

    public List<ItemStack> getWaterSourceList(KettleBlockEntity kettleBlockEntity) {
        Block block = kettleBlockEntity.getBlockState().getBlock();

        Lazy<Map<Ingredient, Integer>> waterMAP = ((KettleBlockAccessor) block).getMAP();
        Set<Ingredient> ingredients = waterMAP.get().keySet();
        List<ItemStack> list = ingredients.stream().map(Ingredient::getItems).flatMap(Stream::of).toList();
//        LOGGER.info("getWaterSourceList: " + list);
        return list;
    }

    public int findMaidHasWaterResource(EntityMaid entityMaid, KettleBlockEntity kettleBlockEntity) {
        List<ItemStack> waterSourceList = getWaterSourceList(kettleBlockEntity);

        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);
        int stackSlot = ItemsUtil.findStackSlot(availableInv, itemStack -> {
            return waterSourceList.stream().anyMatch(ingredient -> ingredient.is(itemStack.getItem()));
        });
//        LOGGER.info("findMaidHasWaterResource.WATER_BUCKET: " + stackSlot);

        return stackSlot;
    }
}
