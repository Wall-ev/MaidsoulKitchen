package com.github.wallev.maidsoulkitchen.task;

import com.google.common.collect.Lists;

import java.util.List;

public enum ModGroup {
    NONE(),

    BLACK("com.github.wallev",
            "com.github.tartaricacid",
            "java.util",
            "java.lang",
            "java.io",
            "java.net",
            "java.nio",
            "com.google",
            "org.apache",
            "org.objectweb",
            "com.mojang",
            "net.minecraft",
            "net.minecraftforge",
            "net.neoforged"),

    XKMC("dev.xkmc"),
    SAMMY("com.sammy"),
    VECTORWING("vectorwing"),
    TEAM_TEA("com.teamtea"),
    SERENESEASONS("sereneseasons"),
    MAO("com.mao"),
    UMPAZ("umpaz"),
    SIHENZHANG("com.sihenzhang"),
    LEKAVAR_LMA("lekavar.lma"),
    YSBBBBBB("com.github.ysbbbbbb"),
    TT_432("io.github.tt432"),

    ;
    public final List<String> groups;

    ModGroup(String... groups) {
        this.groups = Lists.newArrayList(groups);
    }

    public static ModGroup by(String key) {
        return ModGroup.valueOf(key);
    }
}
