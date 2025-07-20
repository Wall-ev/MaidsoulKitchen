package com.github.wallev.maidsoulkitchen.modclazzchecker.manager;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;


public class MKClazzCheckManager extends MKClazzCheck2MixinManager<TaskInfo> {
    public MKClazzCheckManager() {
        super();
    }

    @Override
    protected Codec<TaskInfo> createTaskInfoCodec() {
        return TaskInfo.CODEC;
    }

    @Override
    public TaskInfo taskInfoByKey(String key) {
        return TaskInfo.by(key);
    }

    @Override
    public TaskInfo taskInfoByUid(String uid) {
        ResourceLocation location = ResourceLocation.tryParse(uid);
        if (location == null) {
            return null;
        }
        return TaskInfo.by(location);
    }

    @Override
    public Set<String> getExtractMod() {
        return Sets.newHashSet(TouhouLittleMaid.MOD_ID);
    }

    @Override
    public Set<String> getCompatMods() {
        Set<String> compatMods = Sets.newHashSet();
        for (TaskInfo task : TaskInfo.VALUES) {
            boolean canLoad = task.canLoadWithoutCheckClazz();
            if (!canLoad) {
                continue;
            }
            compatMods.add(task.getBindMod().modId());
        }

        return compatMods;
    }
}
