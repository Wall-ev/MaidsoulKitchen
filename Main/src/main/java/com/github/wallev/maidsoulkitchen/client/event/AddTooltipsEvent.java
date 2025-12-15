package com.github.wallev.maidsoulkitchen.client.event;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.init.ModItems;
import com.github.wallev.maidsoulkitchen.vhelper.client.chat.VComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = MaidsoulKitchen.MOD_ID, value = Dist.CLIENT)
public class AddTooltipsEvent {

    @SubscribeEvent
    public static void addTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        List<Component> components = event.getToolTip();

        if (itemStack.is(ModItems.BURN_PROTECT_BAUBLE.get())) {
            components.add(VComponent.empty());
            components.add(VComponent.translatable("tooltips.maidsoulkitchen.burn_protect_bauble.desc.function").withStyle(ChatFormatting.GREEN));
            components.add(VComponent.translatable("tooltips.maidsoulkitchen.burn_protect_bauble.desc.function.1").withStyle(ChatFormatting.GRAY));
        }
    }

}
