package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.ModClazzChecker;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager.BaseClazzCheckManager;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.util.MapUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.Mixin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

public final class TaskMixinAnalyzer {

    private static final String FILE_NAME = "mod_task_mixin_clazz.json";

    public static void writeModTaskClazz(Path rootOutputFolder, BaseClazzCheckManager<?, ?> checkManager) {
        ModTaskMixinMap modTaskClazz = collectModTaskClazz(checkManager);
        ModTaskMixinMap.CODEC.apply(checkManager.getModsCodecO()).encodeStart(JsonOps.INSTANCE, modTaskClazz)
                .resultOrPartial(error -> {
                    ModClazzChecker.LOGGER.error("生成失败：{}", error);
                })
                .ifPresent(data -> {
                    File file = new File(rootOutputFolder.toString().replace("generated", "main") + "\\" + FILE_NAME);
                    Gson gson = new GsonBuilder()
                            .setPrettyPrinting()  // 保留缩进
                            .create();
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdir();
                    }
                    try {
                        FileWriter fileWriter = new FileWriter(file);
                        fileWriter.write(gson.toJson(data));
                        fileWriter.close();
                        ModClazzChecker.LOGGER.info("生成成功：{}", file.getPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public static ModTaskMixinMap readModTaskClazz(BaseClazzCheckManager<?, ?> checkManager) {
        try {
            Path resource = LoadingModList.get().getModFileById(checkManager.getModId())
                    .getFile()
                    .findResource(FILE_NAME);
            String json = Files.readString(resource);
            JsonObject jsonData = JsonParser.parseString(json).getAsJsonObject();
            return ModTaskMixinMap.CODEC.apply(checkManager.getModsCodecO()).parse(JsonOps.INSTANCE, jsonData)
                    .result()
                    .orElseThrow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"SameParameterValue", "unchecked"})
    public static ModTaskMixinMap collectModTaskClazz(BaseClazzCheckManager<?, ?> checkManager) {
        Map<String, IMods> targetBindInfo = new HashMap<>();
        Map<String, Set<String>> taskMixinList = new HashMap<>();

        Map<String, Set<String>> classInfo = new HashMap<>();
        Map<String, Set<String>> classInfo0 = new HashMap<>();

        Map<String, Set<String>> mixinMap = new HashMap<>();
        Map<String, Set<String>> mixinResourceMap = new HashMap<>();

        Type taskMixinType = checkManager.getTaskClazzMixinAnnotationType();
        Type mixinType = Type.getType(Mixin.class);
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        for (ModFileScanData scanData : allScanData) {
            for (ModFileScanData.AnnotationData data : scanData.getAnnotations()) {
                Type annotationedType = data.annotationType();
                if (Objects.equals(annotationedType, taskMixinType)) {
                    List<ModAnnotation.EnumHolder> taskEnum = getEnumHolderValue(data, "value");
                    for (ModAnnotation.EnumHolder enumHolder : taskEnum) {
                        ITaskInfo<?> task = checkManager.taskInfoByKey(enumHolder.getValue());
                        targetBindInfo.put(task.getUidStr(), task.getBindMod());
                        classInfo.computeIfAbsent(data.memberName(), (name) -> {
                            return new HashSet<>();
                        }).add(task.getUidStr());
                        mixinResourceMap.computeIfAbsent(data.memberName(), (name) -> {
                            return new HashSet<>();
                        }).add(task.getUidStr());
                    }
                }
                if (Objects.equals(annotationedType, mixinType) && data.memberName().startsWith(checkManager.getMixinPackage())) {
                    List<String> list = new ArrayList<>();
                    if (data.annotationData().get("value") != null) {
                        String string = ((List<?>) data.annotationData().get("value")).get(0).toString();
                        string = string.substring(1, string.length() - 1);
                        list.add(string.replace("/", "."));
                    }

                    if (data.annotationData().get("targets") != null) {
                        List<String> targets = (List<String>) data.annotationData().get("targets");
                        for (String target : targets) {
                            list.add(target.replace("/", "."));
                        }
                    }

                    mixinMap.computeIfAbsent(data.memberName(), (uid) -> {
                        return new HashSet<>();
                    }).addAll(list);
                    classInfo0.computeIfAbsent(data.memberName(), (uid) -> {
                        return new HashSet<>();
                    }).addAll(list);
                }
            }
        }
        classInfo0.forEach((key, val) -> {
            Set<String> set = classInfo.getOrDefault(key, new HashSet<>());
            for (String resourceLocation : set) {
                taskMixinList.computeIfAbsent(resourceLocation, (uid) -> {
                    return new HashSet<>();
                }).addAll(val);
            }
        });

        Map<String, Set<ModTaskMixin>> map = new HashMap<>();
        mixinMap.forEach((key, value) -> {
            Set<String> set = mixinResourceMap.getOrDefault(key, new HashSet<>());
            for (String taskUid : set) {
                IMods mod = targetBindInfo.get(taskUid);
                ModTaskMixin modTaskMixin = ModTaskMixin.create(taskUid, mod, value);
                map.computeIfAbsent(taskUid, (uid) -> {
                    return new HashSet<>();
                }).add(modTaskMixin);
            }
        });
        return ModTaskMixinMap.create(map);
    }

    private static List<ModAnnotation.EnumHolder> getEnumHolderValue(ModFileScanData.AnnotationData data, String name) {
        return ((List<ModAnnotation.EnumHolder>) data.annotationData().get(name));
    }

    public record ModTaskMixin(String taskUid, IMods compatMod, List<String> mixinList) {
        public static final Function<Codec<IMods>, Codec<ModTaskMixin>> CODEC = (mc) -> {
             return RecordCodecBuilder.create(ins -> ins.group(
                    Codec.STRING.fieldOf("taskUid").forGetter(o -> {
                        return o.taskUid;
                    }),
                    mc.fieldOf("compatMod").forGetter(o -> {
                        return o.compatMod;
                    }),
                    Codec.STRING.listOf().fieldOf("mixinList").forGetter(ModTaskMixin::mixinList)
            ).apply(ins, ModTaskMixin::new));
        };

        public static ModTaskMixin create(String taskUid, IMods compatMod, Set<String> mixinList) {
            return new ModTaskMixin(taskUid, compatMod, Lists.newArrayList(mixinList));
        }
    }

    public static class ModTaskMixinMap {
        public static final Function<Codec<IMods>, Codec<ModTaskMixinMap>> CODEC = (mc) -> {
            return RecordCodecBuilder.create(ins -> ins.group(
                    Codec.unboundedMap(Codec.STRING, ModTaskMixin.CODEC.apply(mc).listOf()).fieldOf("list").forGetter(ModTaskMixinMap::map)
            ).apply(ins, ModTaskMixinMap::new));
        };
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
                            return l.mixinList.stream();
                        })
                        .toList();
                map.computeIfAbsent(taskUid, (t) -> {
                    return new HashSet<>();
                }).addAll(mixinList0);
            });
            return MapUtil.set2List(map);
        }

        private Map<String, List<ModTaskMixin>> map() {
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
}
