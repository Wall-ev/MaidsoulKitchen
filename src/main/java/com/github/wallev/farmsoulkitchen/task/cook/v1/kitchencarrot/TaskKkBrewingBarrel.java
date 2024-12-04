package com.github.wallev.farmsoulkitchen.task.cook.v1.kitchencarrot;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.api.task.v1.cook.ICookTask;
import com.github.wallev.farmsoulkitchen.api.task.v1.cook.IHandlerCookBe;
import com.github.wallev.farmsoulkitchen.api.task.v1.cook.IItemHandlerCook;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.farmsoulkitchen.entity.passive.IAddonMaid;
import com.github.wallev.farmsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.farmsoulkitchen.task.cook.handler.v2.MaidRecipesManager;
import com.github.wallev.farmsoulkitchen.util.FakePlayerUtil;
import io.github.tt432.kitchenkarrot.blockentity.BrewingBarrelBlockEntity;
import io.github.tt432.kitchenkarrot.capability.KKItemStackHandler;
import io.github.tt432.kitchenkarrot.recipes.recipe.BrewingBarrelRecipe;
import io.github.tt432.kitchenkarrot.registries.ModBlocks;
import io.github.tt432.kitchenkarrot.registries.ModItems;
import io.github.tt432.kitchenkarrot.registries.RecipeTypes;
import io.github.tt432.kitchenkarrot.util.ItemHandlerUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class TaskKkBrewingBarrel implements ICookTask<BrewingBarrelBlockEntity, BrewingBarrelRecipe>, IHandlerCookBe<BrewingBarrelBlockEntity>, IItemHandlerCook {
    public static final ResourceLocation UID = new ResourceLocation(FarmsoulKitchen.MOD_ID, "kk_brew_barrel");

    public static final Map<Item, Integer> FLUID_WATER = Map.of(Items.WATER_BUCKET, 1, ModItems.WATER.get(), 4);
    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof BrewingBarrelBlockEntity;
    }

    @Override
    public RecipeType<BrewingBarrelRecipe> getRecipeType() {
        return RecipeTypes.BREWING_BARREL.get();
    }

    @Override
    public boolean shouldMoveTo(ServerLevel serverLevel, EntityMaid maid, BrewingBarrelBlockEntity brewBe, MaidRecipesManager<BrewingBarrelRecipe> recManager) {
        CombinedInvWrapper maidInv = maid.getAvailableInv(true);

        if (!this.getResultHandler(brewBe).getStackInSlot(getOutputSlot()).isEmpty()) {
            return true;
        }

        boolean findFluidItem = ItemsUtil.findStackSlot(maidInv, stack -> FLUID_WATER.containsKey(stack.getItem()) && stack.getCount() >= FLUID_WATER.get(stack.getItem())) > -1;
        if (!brewBe.isStarted() && !recManager.getRecipesIngredients().isEmpty()) {
            return brewBe.hasEnoughWater() || findFluidItem;
        }

        if (hasRecipe(brewBe) && !brewBe.hasEnoughWater() && findFluidItem) {
            return true;
        }

        if (!brewBe.isStarted() && hasInput(getInputHandler(brewBe))) {
            return true;
        }

        return false;
    }

    @SuppressWarnings("all")
    private static boolean hasRecipe(BrewingBarrelBlockEntity brewBe) {
        List<ItemStack> inputList = ItemHandlerUtils.toList(brewBe.input);
        return brewBe.getLevel().getRecipeManager().getAllRecipesFor(RecipeTypes.BREWING_BARREL.get()).stream().anyMatch((r) -> {
            return r.matches(inputList);
        });
    }

    @Override
    public void processCookMake(ServerLevel serverLevel, EntityMaid maid, BrewingBarrelBlockEntity brewBe, MaidRecipesManager<BrewingBarrelRecipe> recManager) {
        CombinedInvWrapper maidInv = maid.getAvailableInv(true);

        if (!brewBe.hasEnoughWater()) {
            replenishWater(maid, brewBe, maidInv);
        }

        if (!this.getResultHandler(brewBe).getStackInSlot(getOutputSlot()).isEmpty()) {
            extractOutputStack(getResultHandler(brewBe), recManager.getOutputInv(maid), brewBe);
        }
        pickupAction(maid);

        if (!brewBe.isStarted() && hasInput(getInputHandler(brewBe))) {
            extractInputStack(getInputHandler(brewBe), recManager.getIngreInv(maid), brewBe);
        }

        if (!brewBe.isStarted() && brewBe.hasEnoughWater() && !recManager.getRecipesIngredients().isEmpty()) {
            insertInputStack(getInputHandler(brewBe), maidInv, brewBe, recManager.getRecipeIngredient());
        }
        pickupAction(maid);
    }

    private static void replenishWater(EntityMaid maid, BrewingBarrelBlockEntity brewBe, CombinedInvWrapper maidInv) {
        int fluidItemSlot = ItemsUtil.findStackSlot(maidInv, stack -> FLUID_WATER.containsKey(stack.getItem()) && stack.getCount() >= FLUID_WATER.get(stack.getItem()));
        if (fluidItemSlot > -1) {

            ItemStack waterStack = maid.getAvailableInv(true).getStackInSlot(fluidItemSlot);
            WeakReference<FakePlayer> fakePlayer$tlma = ((IAddonMaid) maid).getFakePlayer$tlma();
            FakePlayer fakePlayer = fakePlayer$tlma.get();
            if (fakePlayer != null) {

                int time = FLUID_WATER.getOrDefault(waterStack.getItem(), 0);
                for (int i = 0; i < time; i++) {
                    fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, waterStack.split(1));
                    try {
                        InteractionResult interactionResult = FakePlayerUtil.interactUseOnBlock(fakePlayer$tlma, maid.level(), brewBe.getBlockPos(), InteractionHand.MAIN_HAND, null);

                        if (interactionResult != InteractionResult.PASS) {
                            ItemStack itemInHand = fakePlayer.getItemInHand(InteractionHand.MAIN_HAND);
                            ItemHandlerHelper.insertItemStacked(maid.getAvailableInv(true), itemInHand.copy(), false);
                            fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                        } else {
                            fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                        }

                        if (interactionResult == InteractionResult.PASS) {

                        }
                    } catch (Exception e) {

                    }
                }


            }

        }
    }

    public KKItemStackHandler getInputHandler(BrewingBarrelBlockEntity brewBe) {
        return brewBe.input;
    }

    public KKItemStackHandler getResultHandler(BrewingBarrelBlockEntity brewBe) {
        return (KKItemStackHandler) brewBe.result();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return ModBlocks.BREWING_BARREL.get().asItem().getDefaultInstance();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.KC_BREW_BARREL;
    }

    @Override
    public ItemStackHandler getItemStackHandler(BrewingBarrelBlockEntity brewingBarrelBlockEntity) {
        return brewingBarrelBlockEntity.input;
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getInputSize() {
        return 6;
    }

    @Override
    public NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
        return ((BrewingBarrelRecipe)recipe).getIngredient();
    }
}
