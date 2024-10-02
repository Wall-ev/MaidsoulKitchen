package com.github.catbert.tlma.compat.patchouli.event;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.api.ILittleMaidTask;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Locale;

public class OpenPatchouliBookEvent {

    @SubscribeEvent
    public void openBook(com.github.tartaricacid.touhoulittlemaid.api.event.client.OpenPatchouliBookEvent event) {
        ResourceLocation uid = event.getTask().getUid();

        if (uid.getNamespace().equals(TLMAddon.MOD_ID) && event.getTask() instanceof ILittleMaidTask task) {
            ResourceLocation location = new ResourceLocation(TouhouLittleMaid.MOD_ID, "memorizable_gensokyo");
            String lowerCase = task.getBookEntryType().name;
            PatchouliAPI.get().openBookEntry(location, new ResourceLocation(TouhouLittleMaid.MOD_ID, "addon/" + lowerCase), 1);
        }
    }

}
