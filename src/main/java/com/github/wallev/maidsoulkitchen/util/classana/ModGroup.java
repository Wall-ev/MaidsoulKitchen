package com.github.wallev.maidsoulkitchen.util.classana;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

public enum ModGroup {
    NONE(),

    MC("com.mojang",
            "net.minecraft",
            "cpw.mods",
            "net.minecraftforge",
            "net.neoforged"),

    BLACK(MC,
            "com.github.wallev",
            "com.github.tartaricacid",
            "java.util",
            "java.lang",
            "java.io",
            "java.net",
            "java.nio",
            "it.unimi",
            "com.google",
            "org.spongepowered",
            "org.apache",
            "org.objectweb"),

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

    ModGroup(ModGroup modGroup, String... groups) {
        Set<String> list = Sets.newHashSet(modGroup.groups);
        list.addAll(Lists.newArrayList(groups));
        this.groups = Lists.newArrayList(list);
    }

    ModGroup(String... groups) {
        this.groups = Lists.newArrayList(groups);
    }

    public static ModGroup by(String key) {
        return ModGroup.valueOf(key);
    }
}
