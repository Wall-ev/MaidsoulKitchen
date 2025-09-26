package com.github.wallev.maidsoulkitchen.entity.data.inner.task.inv.v0;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SwappedInvData {
    public static final ResourceLocation KEY = VResourceLocation.createMod("swapped_inv_data");
    public static Codec<SwappedInvData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("maidHandItem").forGetter(SwappedInvData::getMaidHandItem),
            ItemStack.CODEC.fieldOf("offHandItem").forGetter(SwappedInvData::getOffHandItem)
    ).apply(instance, SwappedInvData::new));

    private ItemStack maidHandItem = ItemStack.EMPTY;
    private ItemStack offHandItem = ItemStack.EMPTY;

    public SwappedInvData(ItemStack maidHandItem, ItemStack offHandItem) {
        this.maidHandItem = maidHandItem;
        this.offHandItem = offHandItem;
    }

    public SwappedInvData() {
    }

    public ItemStack getMaidHandItem() {
        return maidHandItem;
    }

    public void setMaidHandItem(ItemStack maidHandItem) {
        this.maidHandItem = maidHandItem;
    }

    public ItemStack getOffHandItem() {
        return offHandItem;
    }

    public void setOffHandItem(ItemStack offHandItem) {
        this.offHandItem = offHandItem;
    }

    public static SwappedInvData get(EntityMaid maid) {
        return maid.getOrCreateData(DataRegister.SWAPPED_INV, new SwappedInvData());
    }
}
