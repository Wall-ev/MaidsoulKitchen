package com.github.catbert.tlma.task.cook.v1.yhc;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.api.ILittleMaidTask;
import com.github.catbert.tlma.entity.data.inner.task.CookData;
import com.github.catbert.tlma.entity.passive.IAddonMaid;
import com.github.catbert.tlma.init.registry.tlm.RegisterData;
import com.github.catbert.tlma.mixin.yhc.KettleBlockAccessor;
import com.github.catbert.tlma.task.cook.handler.v2.MaidRecipesManager;
import com.github.catbert.tlma.task.cook.v1.common.TaskFdPot;
import com.github.catbert.tlma.util.FakePlayerUtil;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
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

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class TaskYhcTeaKettle extends TaskFdPot<KettleBlockEntity, KettleRecipe> {
    public static final TaskYhcTeaKettle INSTANCE = new TaskYhcTeaKettle();
    public static final ResourceLocation UID = new ResourceLocation(TLMAddon.MOD_ID, "yhc_tea_kettle");

    private TaskYhcTeaKettle() {
    }

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
        return UID;
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
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, KettleBlockEntity blockEntity, MaidRecipesManager<KettleRecipe> maidRecipesManager) {
        return super.shouldMoveTo(serverLevel, entityMaid, blockEntity, maidRecipesManager) || (needWater(blockEntity) && findMaidHasWaterResource(entityMaid, blockEntity) != -1);
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid entityMaid, KettleBlockEntity blockEntity, MaidRecipesManager<KettleRecipe> maidRecipesManager) {
        super.processCookMake(serverLevel, entityMaid, blockEntity, maidRecipesManager);
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

    public static TaskYhcTeaKettle getInstance() {
        return INSTANCE;
    }
}
