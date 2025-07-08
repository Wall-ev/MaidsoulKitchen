package com.github.wallev.maidsoulkitchenlegacy.task.cook;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.modutility.Mods;
import com.github.wallev.verhelper.client.resources.VResourceLocation;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.function.Supplier;

public enum LegacyTaskInfo {
    YHC_FERMENTITAION(TaskInfo.YHC_FERMENTATION_TANK, Mods.YHCD_LEGACY),
    KK_BREWING(TaskInfo.KK_BREW_BARREL, Mods.KK_LEGACY, "io.github.tt432.kitchenkarrot.blockentity.BrewingBarrelBlockEntity"),
    ;
    public final ResourceLocation uid;
    public final Mods bindMod;
    public final Supplier<ForgeConfigSpec.BooleanValue> bindConfig;
    public final List<String> mixinList;

    LegacyTaskInfo(ResourceLocation uid, Mods bindMod, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, String... mixinList) {
        this.uid = uid;
        this.bindMod = bindMod;
        this.bindConfig = bindConfig;
        this.mixinList = Lists.newArrayList(mixinList);

        for (String target : mixinList) {
            LegacyMixinInfo.putMixin(target, bindMod, uid);
        }
    }

    LegacyTaskInfo(TaskInfo taskInfo, Mods bindMod, String... mixinList) {
        this(taskInfo.getUid(), bindMod, taskInfo.bindConfig, mixinList);
    }

    LegacyTaskInfo(String uid, Mods bindMod, Supplier<ForgeConfigSpec.BooleanValue> bindConfig, String... mixinList) {
        this(VResourceLocation.createMod(uid), bindMod, bindConfig, mixinList);
    }

    public static void init(){
    }
}
