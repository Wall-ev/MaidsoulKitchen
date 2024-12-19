package com.github.wallev.maidsoulkitchen.compat.patchouli.event;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.ILittleMaidTask;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.patchouli.api.PatchouliAPI;

public class OpenPatchouliBookEvent {

    @SubscribeEvent
    public void openBook(com.github.tartaricacid.touhoulittlemaid.api.event.client.OpenPatchouliBookEvent event) {
        ResourceLocation uid = event.getTask().getUid();

        if (uid.getNamespace().equals(MaidsoulKitchen.MOD_ID) && event.getTask() instanceof ILittleMaidTask task) {
            ResourceLocation location = new ResourceLocation(TouhouLittleMaid.MOD_ID, "memorizable_gensokyo");
            PatchouliAPI.get().openBookEntry(location, new ResourceLocation(TouhouLittleMaid.MOD_ID, "maidsoulkitchen/" + task.getBookEntry()), 1);
        }
    }

}
