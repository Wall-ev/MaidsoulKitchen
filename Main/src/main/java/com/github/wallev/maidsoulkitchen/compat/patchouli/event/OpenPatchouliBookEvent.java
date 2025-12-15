package com.github.wallev.maidsoulkitchen.compat.patchouli.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.lib.auto.event.AutoEventSubscriber;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.patchouli.api.PatchouliAPI;

@AutoEventSubscriber(mods = Mods.PATCHOULI, value = Dist.CLIENT)
public class OpenPatchouliBookEvent {

    @SubscribeEvent
    public void openBook(com.github.tartaricacid.touhoulittlemaid.api.event.client.OpenPatchouliBookEvent event) {
        ResourceLocation uid = event.getTask().getUid();

        if (uid.getNamespace().equals(MaidsoulKitchen.MOD_ID) && event.getTask() instanceof IMaidsoulKitchenTask task) {
            ResourceLocation location = VResourceLocation.of(TouhouLittleMaid.MOD_ID, "memorizable_gensokyo");
            PatchouliAPI.get().openBookEntry(location, VResourceLocation.of(TouhouLittleMaid.MOD_ID, "maidsoulkitchen/" + task.getBookEntry()), 1);
        }
    }

}
