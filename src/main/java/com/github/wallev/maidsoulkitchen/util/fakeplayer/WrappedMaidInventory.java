package com.github.wallev.maidsoulkitchen.util.fakeplayer;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.github.wallev.maidsoulkitchen.api.IMaidsoulKitchenTask;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.ApiStatus;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@ApiStatus.Experimental
public class WrappedMaidInventory extends Inventory {
    private final EntityMaid maid;
    private Function<EntityMaid, IItemHandlerModifiable> invSupplier = maid0 -> defaultTaskInv();
    public WrappedMaidInventory(EntityMaid maid, WrappedMaidFakePlayer fakePlayer) {
        super(fakePlayer);
        this.maid = maid;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public IItemHandlerModifiable getInv() {
        return invSupplier.apply(maid);
    }

    protected IItemHandlerModifiable defaultTaskInv() {
        IMaidTask task = maid.getTask();
        if (task instanceof IMaidsoulKitchenTask maidsoulKitchenTask) {
            return maidsoulKitchenTask.getInventory(maid);
        } else {
            return maid.getAvailableInv(true);
        }
    }

    public void setInvSupplier(Function<EntityMaid, IItemHandlerModifiable> invSupplier) {
        this.invSupplier = invSupplier;
    }

    public void resetInv() {
        this.invSupplier = (maid0 -> defaultTaskInv());
    }

    public void stacks(Consumer<ItemStack> stack) {
        IItemHandler inv = this.getInv();
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack0 = inv.getStackInSlot(i);
            if (!stack0.isEmpty()) {
                stack.accept(stack0);
            }
        }
    }

    public ItemStack getItemStack(Predicate<ItemStack> predicate) {
        return ItemsUtil.getStack(this.getInv(), predicate);
    }

    public ItemStack getItemStack(ItemStack itemStack) {
        return this.getItemStack(stack -> stack == itemStack);
    }

    public ItemStack getItemStack(Item item) {
        return this.getItemStack(stack -> stack.is(item));
    }

    public boolean hasItemStack(Predicate<ItemStack> predicate) {
        return ItemsUtil.findStackSlot(this.getInv(), predicate) > -1;
    }

    public boolean hasItemStack(ItemStack itemStack) {
        return this.hasItemStack(stack -> stack == itemStack);
    }

    public boolean hasItemStack(Item item) {
        return this.hasItemStack(stack -> stack.is(item));
    }

    public ItemStack insert(ItemStack itemStack, boolean simulate) {
       return ItemHandlerHelper.insertItemStacked(this.getInv(), itemStack, simulate);
    }

    public void insertWithDrop(ItemStack itemStack) {
        ItemStack leftStack = this.insert(itemStack, false);
        if (!leftStack.isEmpty()) {
            this.maid.level.addFreshEntity(new ItemEntity(maid.level, maid.getX(), maid.getY(), maid.getZ(), leftStack));
        }
    }

    public boolean extract(ItemStack stack) {
        ItemStack itemStack = this.getItemStack(stack0 -> stack0 == stack);
        if (!itemStack.isEmpty()) {
            itemStack.setCount(0);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int countItem(Item item) {
        int count = 0;

        IItemHandler inv = this.getInv();
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.is(item)) {
                count += stack.getCount();
            }
        }

        return count;
    }

    @Override
    public boolean hasAnyOf(Set<Item> itemSet) {
        return this.hasItemStack(stack -> itemSet.contains(stack.getItem()));
    }

    @Override
    public boolean hasAnyMatching(Predicate<ItemStack> predicate) {
        return this.hasItemStack(predicate);
    }

    @Override
    public ItemStack getSelected() {
        return maid.getMainHandItem();
    }

    @Override
    public void setPickedItem(ItemStack stack) {
        this.insertWithDrop(stack);
    }
    
    @Override
    public boolean add(ItemStack stack) {
        ItemStack itemStack = this.insert(stack, true);
        if (!itemStack.isEmpty()) {
            return false;
        } else {
            this.insert(stack, false);
            return true;
        }
    }

    @Override
    public void placeItemBackInInventory(ItemStack stack) {
        this.placeItemBackInInventory(stack, true);
    }

    @Override
    public void placeItemBackInInventory(ItemStack stack, boolean sendPacket) {
        this.insertWithDrop(stack);
    }

    @Override
    public void removeItem(ItemStack stack) {
        this.extract(stack);
    }

    @Override
    public float getDestroySpeed(BlockState state) {
        return this.getSelected().getDestroySpeed(state);
    }


    /******************************* toProcess *************************************/
    @Override
    public void tick() {
    }

    @Override
    public ListTag save(ListTag listTag) {
        return listTag;
    }

    @Override
    public void load(ListTag listTag) {
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public Component getName() {
        return super.getName();
    }

    @Override
    public ItemStack getArmor(int pSlot) {
        return super.getArmor(pSlot);
    }

    @Override
    public void hurtArmor(DamageSource pSource, float pDamage, int[] pArmorPieces) {
        super.hurtArmor(pSource, pDamage, pArmorPieces);
    }

    @Override
    public void dropAll() {
        super.dropAll();
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public int getTimesChanged() {
        return super.getTimesChanged();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return super.stillValid(pPlayer);
    }

    @Override
    public boolean contains(ItemStack stack) {
        return super.contains(stack);
    }

    @Override
    public boolean contains(TagKey<Item> pTag) {
        return super.contains(pTag);
    }

    @Override
    public void replaceWith(Inventory pPlayerInventory) {
        super.replaceWith(pPlayerInventory);
    }

    @Override
    public void clearContent() {
        super.clearContent();
    }

    @Override
    public void fillStackedContents(StackedContents pStackedContent) {
        super.fillStackedContents(pStackedContent);
    }

    @Override
    public ItemStack removeFromSelected(boolean pRemoveStack) {
        return super.removeFromSelected(pRemoveStack);
    }

    @Override
    public int getMaxStackSize() {
        return super.getMaxStackSize();
    }

    @Override
    public void startOpen(Player pPlayer) {
        super.startOpen(pPlayer);
    }

    @Override
    public void stopOpen(Player pPlayer) {
        super.stopOpen(pPlayer);
    }

    @Override
    public boolean hasCustomName() {
        return super.hasCustomName();
    }

    @Override
    public Component getDisplayName() {
        return super.getDisplayName();
    }

    @Override
    public Component getCustomName() {
        return super.getCustomName();
    }
    /*******************************************************************************/



    /******************************* UnsupportedOperation *************************************/
    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public int findSlotMatchingItem(ItemStack stack) {
        return 0;
    }

    @Override
    public int findSlotMatchingUnusedItem(ItemStack stack) {
        return 0;
    }

    @Override
    public int getSuitableHotbarSlot() {
        return 0;
    }

    @Override
    public void swapPaint(double pDirection) {
    }

    @Override
    public int clearOrCountMatchingItems(Predicate<ItemStack> pStackPredicate, int pMaxCount, Container pInventory) {
        return 0;
    }

    @Override
    public int getSlotWithRemainingSpace(ItemStack stack) {
        return 0;
    }
    
    @Override
    public boolean add(int pSlot, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canTakeItem(Container pTarget, int index, ItemStack stack) {
        return false;
    }

    @Override
    public void pickSlot(int index) {
    }

    @Override
    public ItemStack getItem(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
    }

    @Override
    public ItemStack removeItem(int index, int pCount) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getFreeSlot() {
        return 0;
    }
    /******************************************************************************************/
}
