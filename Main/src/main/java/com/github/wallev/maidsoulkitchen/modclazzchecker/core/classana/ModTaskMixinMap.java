package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.util.MapUtil;
import com.google.common.collect.Lists;

import java.util.*;

public class ModTaskMixinMap {

    private final Map<String, List<ModTaskMixin>> map;
    private final Map<String, List<IMods>> sourceMixinMap;

    public ModTaskMixinMap(Map<String, List<ModTaskMixin>> map) {
        this.map = map;

        Map<String, List<IMods>> sourceMixinMap = new HashMap<>();
        map.values().stream().flatMap((v) -> {
            return v.stream();
        }).forEach(modTaskMixin -> {
            modTaskMixin.mixinList().forEach((mixin) -> {
                sourceMixinMap.computeIfAbsent(mixin, (uid) -> {
                    return new ArrayList<>();
                }).add(modTaskMixin.compatMod());
            });
        });
        this.sourceMixinMap = sourceMixinMap;
    }

    public static ModTaskMixinMap create(Map<String, Set<ModTaskMixin>> map) {
        Map<String, List<ModTaskMixin>> map0 = new HashMap<>();
        map.forEach((key, val) -> {
            map0.put(key, Lists.newArrayList(val));
        });
        return new ModTaskMixinMap(map0);
    }

    public Map<String, List<String>> getMixinList() {
        Map<String, Set<String>> map = new HashMap<>();
        this.map.forEach((taskUid, list) -> {
            List<String> mixinList0 = list.stream()
                    .flatMap(l -> {
                        return l.mixinList().stream();
                    })
                    .toList();
            map.computeIfAbsent(taskUid, (t) -> {
                return new HashSet<>();
            }).addAll(mixinList0);
        });
        return MapUtil.set2List(map);
    }

    public Map<String, List<ModTaskMixin>> map() {
        return map;
    }

    public boolean canMixin(String sourceMixinClazz) {
        return Optional.ofNullable(sourceMixinMap.get(sourceMixinClazz))
                .map(info -> {
                    for (IMods mod : info) {
                        if (!mod.versionLoad()) {
                            return false;
                        }
                    }
                    return true;
                })
                .orElse(true);
    }
}
