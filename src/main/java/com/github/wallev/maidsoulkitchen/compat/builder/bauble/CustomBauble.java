package com.github.wallev.maidsoulkitchen.compat.builder.bauble;

import com.github.wallev.maidsoulkitchen.compat.builder.CustomBaseLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;


public class CustomBauble extends CustomBaseLittleMaid<CustomBaubleBuilder> implements IMaidBauble {
    private Item item;
    private BiConsumer<EntityMaid, ItemStack> onTick;

    public CustomBauble(CustomBaubleBuilder builder) {
        super(builder);
    }

    @Override
    public void bindMaidBauble(BaubleManager manager) {
        manager.bind(this.item, this);
    }

    @Override
    public void onTick(EntityMaid maid, ItemStack baubleItem) {
        this.onTick.accept(maid, baubleItem);
    }

    @Override
    public void builderDefaultInit(@NotNull CustomBaubleBuilder builder) {
        this.item = builder.item;
        this.onTick = builder.onTick;
    }
}
