package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;

public record ModTaskMixin(String taskUid, IMods compatMod, List<String> mixinList) {

    public static ModTaskMixin create(String taskUid, IMods compatMod, Set<String> mixinList) {
        return new ModTaskMixin(taskUid, compatMod, Lists.newArrayList(mixinList));
    }
}
