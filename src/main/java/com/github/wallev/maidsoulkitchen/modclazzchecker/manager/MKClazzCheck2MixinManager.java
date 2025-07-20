package com.github.wallev.maidsoulkitchen.modclazzchecker.manager;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ITaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager.BaseClazzCheckManager;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import org.objectweb.asm.Type;

import java.util.Set;

public class MKClazzCheck2MixinManager<T extends ITaskInfo<Mods>> extends BaseClazzCheckManager<T, Mods> {
    public static final String MOD_PACKAGE = "com.github.wallev.maidsoulkitchen";
    public static final String MIXIN_PACKAGE = MOD_PACKAGE + ".mixin.compat";

    public MKClazzCheck2MixinManager() {
        super(MaidsoulKitchen.MOD_ID, MaidsoulKitchen.ISSUE_URL, MOD_PACKAGE, MIXIN_PACKAGE);
    }

    @Override
    protected Codec<T> createTaskInfoCodec() {
        return null;
    }

    @Override
    protected Codec<Mods> createModsCodec() {
        return Mods.CODEC;
    }

    @Override
    public T taskInfoByKey(String key) {
        return null;
    }

    @Override
    public T taskInfoByUid(String uid) {
        return null;
    }

    @Override
    public Mods modsByKey(String mod) {
        return Mods.by(mod);
    }

    @Override
    public Type getTaskClazzAnnotationType() {
        return Type.getType(TaskClassAnalyzer.class);
    }

    @Override
    public Type getTaskClazzMixinAnnotationType() {
        return Type.getType(TaskMixin.class);
    }

    @Override
    protected Set<String> getExtractBlackGroups() {
        return Sets.newHashSet("org.joml",
                "com.github.tartaricacid.simplebedrockmodel",
                "com.github.tartaricacid.touhoulittlemaid");
    }
}
