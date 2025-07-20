package com.github.wallev.maidsoulkitchen.item;

import com.github.tartaricacid.touhoulittlemaid.item.ItemDamageableBauble;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemBurnProtectBauble extends ItemDamageableBauble {
    public ItemBurnProtectBauble() {
        super(128);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag isAdvanced) {
        super.appendHoverText(itemStack, level, components, isAdvanced);
//        components.add(VComponent.empty());
//        components.add(VComponent.translatable("tooltips.maidsoulkitchen.burn_protect_bauble.desc.function").withStyle(ChatFormatting.GREEN));
//        components.add(VComponent.translatable("tooltips.maidsoulkitchen.burn_protect_bauble.desc.function.1").withStyle(ChatFormatting.GRAY));
    }
}
