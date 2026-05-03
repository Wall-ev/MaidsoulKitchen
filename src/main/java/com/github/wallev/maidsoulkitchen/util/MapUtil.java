package com.github.wallev.maidsoulkitchen.util;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapUtil {

    public static <K, V> Map<K, List<V>> set2List(Map<K, Set<V>> map) {
        Map<K, List<V>> map0 = new HashMap<>();
        map.forEach((key, valList) -> {
            map0.put(key, Lists.newArrayList(valList));
        });
        return map0;
    }

}
