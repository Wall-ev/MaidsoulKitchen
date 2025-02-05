package com.github.wallev.maidsoulkitchen.task.cook.v1.minecraft;

import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.TaskBaseContainerCook;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.IAbstractFurnaceAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;


public class TaskFurnace extends TaskBaseContainerCook<AbstractFurnaceBlockEntity, AbstractCookingRecipe> {
    @Override
    public boolean isEnable(EntityMaid maid) {
        return true;
    }

    @Override
    public boolean isHeated(AbstractFurnaceBlockEntity be) {
        return true;
    }

    @Override
    public boolean beInnerCanCook(Container inventory, AbstractFurnaceBlockEntity be) {
        return false;
    }

    @Override
    public int getOutputSlot() {
        return 2;
    }

    @Override
    public int getInputSize() {
        return 1;
    }

    @Override
    public Container getContainer(AbstractFurnaceBlockEntity be) {
        return be;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof AbstractFurnaceBlockEntity;
    }

    @Override
    @SuppressWarnings("unchecked, rawtypes")
    public RecipeType<AbstractCookingRecipe> getRecipeType() {
        return (RecipeType) RecipeType.SMELTING;
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.FURNACE.uid;
    }

    @Override
    public ItemStack getIcon() {
        return Items.FURNACE.getDefaultInstance();
    }

    // 为了兼容熔炉类的（熔炉、烟熏炉、高炉）
    // 就直接实时获取配方原料了，而且也因为就一个输入口（燃料不算），运算起来也还行
    // 之前写的，先这样把，找个时间再看看...
    @Override
    public boolean maidShouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, AbstractFurnaceBlockEntity blockEntity, MaidRecipesManager<AbstractCookingRecipe> maidRecipesManager) {
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);

        int[] resultSlots = blockEntity.getSlotsForFace(Direction.DOWN);

        for (int resultSlot : resultSlots) {
            ItemStack resultStack = blockEntity.getItem(resultSlot);
            if (resultStack.isEmpty()) {
                continue;
            }
            if (!blockEntity.canTakeItemThroughFace(resultSlot, resultStack, Direction.DOWN)) {
                continue;
            }
            return true;
        }

        boolean hasFuel = hasFuel(blockEntity) || getFuel(availableInv).isPresent();
        if (!hasFuel) {
            return false;
        }

        RecipeType<? extends AbstractCookingRecipe> recipeType = ((IAbstractFurnaceAccessor) blockEntity).tlmk$getRecipeType();
        for (int slot : blockEntity.getSlotsForFace(Direction.UP)) {
            ItemStack stack = blockEntity.getItem(slot);
            if (!stack.isEmpty()) continue;
            if (getAnyCookableItem(entityMaid, entityMaid.getMaidInv(), recipeType,
                    cookable -> blockEntity.canPlaceItemThroughFace(slot, cookable, Direction.UP))
                    .isPresent()) {
                return true;
            }

        }


        return false;
    }

    private boolean hasFuel(AbstractFurnaceBlockEntity furnace) {
        int[] fuelSlots = furnace.getSlotsForFace(Direction.NORTH);
        for (int fuelSlot : fuelSlots) {
            ItemStack fuelSlotStack = furnace.getItem(fuelSlot);
            if (!fuelSlotStack.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private Optional<ItemStack> getAnyCookableItem(EntityMaid maid, IItemHandlerModifiable availableInv, RecipeType<? extends AbstractCookingRecipe> recipeType,
                                                  Predicate<ItemStack> predicate) {
        for (int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack slotStack = availableInv.getStackInSlot(i);
            if (!slotStack.isEmpty()
                    && getRecipe(maid, slotStack, recipeType).isPresent()
                    && predicate.test(slotStack)) {
                return Optional.of(slotStack);
            }
        }
        return Optional.empty();
    }

    private Optional<? extends AbstractCookingRecipe> getRecipe(EntityMaid maid, ItemStack stack, RecipeType<? extends AbstractCookingRecipe> recipeType) {
        return maid.level.getRecipeManager().getRecipeFor(recipeType, new SimpleContainer(stack), maid.level());
    }

    @Override
    public void maidCookMake(ServerLevel serverLevel, EntityMaid entityMaid, AbstractFurnaceBlockEntity blockEntity, MaidRecipesManager<AbstractCookingRecipe> maidRecipesManager) {
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);
        tryExtractItem(blockEntity, entityMaid, maidRecipesManager.getOutputInv());

        if (!tryInsertFuel(availableInv, blockEntity)){
            return;
        }

        tryInsertCookable(entityMaid, availableInv, blockEntity);

    }

    private void tryExtractItem(AbstractFurnaceBlockEntity furnace, EntityMaid maid, IItemHandlerModifiable availableInv ) {
        int[] resultSlots = furnace.getSlotsForFace(Direction.DOWN);

        for (int resultSlot : resultSlots) {
            ItemStack resultStack = furnace.getItem(resultSlot);
            if (resultStack.isEmpty()) {
                continue;
            }
            if (!furnace.canTakeItemThroughFace(resultSlot, resultStack, Direction.DOWN)) {
                continue;
            }
            ItemStack copy = resultStack.copy();
            furnace.setItem(resultSlot, ItemStack.EMPTY);
            ItemHandlerHelper.insertItemStacked(availableInv, copy, false);

            // to-do
            // 给女仆经验
        }

        pickupAction(maid);
    }

    private boolean tryInsertFuel(CombinedInvWrapper availableInv, AbstractFurnaceBlockEntity furnace) {
        int[] fuelSlots = furnace.getSlotsForFace(Direction.NORTH);
        for (int fuelSlot : fuelSlots) {
            ItemStack fuelSlotStack = furnace.getItem(fuelSlot);
            if (!fuelSlotStack.isEmpty()) {
                continue;
            }
            Optional<ItemStack> fuel = getFuel(availableInv);
            if (fuel.isEmpty()) {
                return false;
            }
            if (!furnace.canPlaceItemThroughFace(fuelSlot, fuel.get(), Direction.NORTH)) {
                continue;
            }
            furnace.setItem(fuelSlot, fuel.get().copy());
            fuel.get().setCount(0);
            furnace.setChanged();
            break;
        }
        return true;
    }

    private void tryInsertCookable(EntityMaid maid, CombinedInvWrapper availableInv, AbstractFurnaceBlockEntity furnace) {
        int[] materialSlots = furnace.getSlotsForFace(Direction.UP);
        for (int materialSlot : materialSlots) {
            ItemStack materialSlotStack = furnace.getItem(materialSlot);
            if (!materialSlotStack.isEmpty()) {
                continue;
            }
            Optional<ItemStack> material = getCookable(maid, maid.getMaidInv(), ((IAbstractFurnaceAccessor)furnace).tlmk$getRecipeType());
            if (material.isEmpty()) {
                continue;
            }
            if (!furnace.canPlaceItemThroughFace(materialSlot, material.get(), Direction.UP)) {
                continue;
            }
            furnace.setItem(materialSlot, material.get().copy());
            material.get().setCount(0);
            furnace.setChanged();
            break;
        }

        pickupAction(maid);
    }

    private Optional<ItemStack> getCookable(EntityMaid maid, ItemStackHandler availableInv, RecipeType<? extends AbstractCookingRecipe> recipeType) {
        for (int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack slotStack = availableInv.getStackInSlot(i);
            if (getRecipe(maid, slotStack, recipeType).isPresent()) {
                return Optional.of(slotStack);
            }
        }
        return Optional.empty();
    }

    private Optional<ItemStack> getFuel(CombinedInvWrapper availableInv ) {
        for(int i = 0; i < availableInv.getSlots(); ++i) {
            ItemStack stack = availableInv.getStackInSlot(i);
            if (stack.isEmpty()) continue;
            if (isFuel(stack)) {
                return Optional.of(stack);
            }
        }
        return Optional.empty();
    }

    private boolean isFuel(ItemStack stack) {
        return AbstractFurnaceBlockEntity.isFuel(stack);
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.MC_FURNACE;
    }

    @Override
    public MaidRecipesManager<AbstractCookingRecipe> getRecipesManager(EntityMaid maid) {
        return new MaidRecipesManager<>(maid, this, false) {

            @Override
            protected boolean enableHub() {
                return false;
            }
        };
    }

    @Override
    public List<Component> getWarnComponent() {
        return List.of(Component.translatable("gui.maidsoulkitchen.btn.cook_guide.info.warn").withStyle(ChatFormatting.YELLOW),
                Component.translatable("gui.maidsoulkitchen.btn.cook_guide.info.warn.furnace"),
                Component.translatable("gui.maidsoulkitchen.btn.cook_guide.info.warn.furnace.1"));
    }
}
