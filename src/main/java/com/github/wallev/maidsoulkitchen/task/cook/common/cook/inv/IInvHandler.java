package com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public interface IInvHandler {
    IInvHandler EMPTY = new EmptyInvHandler();

    /**
     * 返回可用槽数
     *
     * @return 可用插槽数
     **/
    int kl$getSlots();

    /**
     * 返回给定槽位中的 ItemStack。
     *
     * 结果的堆栈大小可能大于 itemstack 的最大大小。
     *
     * 如果结果为空，则槽为空。
     *
     * <p>
     * <strong>重要提示：</strong>此 ItemStack <em>不得</em>修改。此方法不适用于
     * 更改库存的内容。任何能够检测
     * 通过此方法进行修改应引发异常。
     * </p>
     * <p>
     * <strong><em>认真地说：不要修改返回的 ITEMSTACK</em></strong>
     * </p>
     *
     * @param slot 查询
     * 在给定的槽位中@return ItemStack。如果槽位为空，则为空物品堆栈。
     **/
    ItemStack kl$getStackInSlot(int slot);

    /**
     * <p>
     * 将 ItemStack 插入给定的槽位并返回余数。
     * <em>不应</em>在此功能中修改 ItemStack！
     * </p>
     * 注意：此行为与 {@link IFluidHandler#fill（FluidStack， IFluidHandler.FluidAction）} 略有不同
     *
     * @param 插槽 插入插槽。
     * @param 堆叠 ItemStack 进行插入。项处理程序不得修改此项。
     * @param 模拟 如果为 true，则仅模拟插入
     * @return 未插入的剩余 ItemStack（如果接受整个堆栈，则返回一个空的 ItemStack）。
     * 如果保持不变，则可能与输入 ItemStack 相同，否则为新的 ItemStack。
     * 返回的 ItemStack 之后可以安全地修改。
     **/
    ItemStack kl$insertItem(int slot, ItemStack stack, boolean simulate);

    /**
     * 从给定的槽位中提取一个 ItemStack。
     * <p>
     * 如果未提取任何内容，则返回值必须为空，
     * 否则，其堆栈大小必须小于或等于 {@code amount} 和 {@link ItemStack#getMaxStackSize（）}。
     * </p>
     *
     * @param 插槽 要从中提取。
     * @param 数量 要提取的数量（可能大于当前堆栈的最大限制）
     * @param 模拟 如果为 true，则仅模拟提取
     * @return 从插槽中提取的 ItemStack，如果无法提取任何内容，则必须为空。
     * 返回的 ItemStack 可以在之后安全地修改，因此物品处理程序应返回新的或复制的堆栈。
     **/
    ItemStack kl$extractItem(int slot, int amount, boolean simulate);

    @SuppressWarnings("unchecked")
    default <IC0> IC0 kl$castAny() {
        return (IC0) this;
    }

    default IItemHandler kl$itemHandler() {
        return this.kl$castAny();
    }

    default Container kl$container() {
        return this.kl$castAny();
    }
}
