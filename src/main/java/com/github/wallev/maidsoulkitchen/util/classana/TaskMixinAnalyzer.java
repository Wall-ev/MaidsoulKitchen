package com.github.wallev.maidsoulkitchen.util.classana;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.mixin.IMaidsoulKitchenInterface;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.MapUtil;
import com.github.wallev.maidsoulkitchen.util.modutility.Mods;
import com.github.wallev.verhelper.client.resources.VResourceLocation;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
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

public final class TaskMixinAnalyzer {
    private static final String FILE_NAME = "mod_task_mixin_clazz.json";
    private static final String MIXIN_PACKAGE = "com.github.wallev.maidsoulkitchen.mixin.compat";

    public static void writeModTaskClazz(Path rootOutputFolder) {
        ModTaskMixinMap modTaskClazz = collectModTaskClazz();
        ModTaskMixinMap.CODEC.encodeStart(JsonOps.INSTANCE, modTaskClazz)
                .resultOrPartial(error -> {
                    MaidsoulKitchen.LOGGER.error("生成失败：{}", error);
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
                        MaidsoulKitchen.LOGGER.info("生成成功：{}", file.getPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public static ModTaskMixinMap readModTaskClazz() {
        try {
            Path resource = LoadingModList.get().getModFileById(MaidsoulKitchen.MOD_ID)
                    .getFile()
                    .findResource(FILE_NAME);
            String json = Files.readString(resource);
            JsonObject jsonData = JsonParser.parseString(json).getAsJsonObject();
            return ModTaskMixinMap.CODEC.parse(JsonOps.INSTANCE, jsonData)
                    .result()
                    .orElseThrow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"SameParameterValue", "unchecked"})
    public static ModTaskMixinMap collectModTaskClazz() {
        Map<String, Mods> targetBindInfo = new HashMap<>();
        Map<ResourceLocation, Set<String>> taskMixinList = new HashMap<>();

        Map<String, Set<ResourceLocation>> classInfo = new HashMap<>();
        Map<String, Set<String>> classInfo0 = new HashMap<>();

        Map<String, Set<String>> mixinMap = new HashMap<>();
        Map<String, Set<ResourceLocation>> mixinResourceMap = new HashMap<>();

        Type taskMixinType = Type.getType(TaskMixin.class);
        Type mixinType = Type.getType(Mixin.class);
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        for (ModFileScanData scanData : allScanData) {
            for (ModFileScanData.AnnotationData data : scanData.getAnnotations()) {
                Type annotationedType = data.annotationType();
                if (Objects.equals(annotationedType, taskMixinType)) {
                    List<ModAnnotation.EnumHolder> taskEnum = getEnumHolderValue(data, "task");
                    for (ModAnnotation.EnumHolder enumHolder : taskEnum) {
                        TaskInfo task = TaskInfo.by(enumHolder.getValue());
                        targetBindInfo.put(task.uid.toString(), task.bindMod);
                        classInfo.computeIfAbsent(data.memberName(), (name) -> {
                            return new HashSet<>();
                        }).add(task.uid);
                        mixinResourceMap.computeIfAbsent(data.memberName(), (name) -> {
                            return new HashSet<>();
                        }).add(task.uid);
                    }
                }
                if (Objects.equals(annotationedType, mixinType) && data.memberName().startsWith(MIXIN_PACKAGE)) {
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
            Set<ResourceLocation> set = classInfo.get(key);
            for (ResourceLocation resourceLocation : set) {
                taskMixinList.computeIfAbsent(resourceLocation, (uid) -> {
                    return new HashSet<>();
                }).addAll(val);
            }
        });

        Map<String, Set<ModTaskMixin>> map = new HashMap<>();
        mixinMap.forEach((key, value) -> {
            Set<ResourceLocation> set = mixinResourceMap.get(key);
            for (ResourceLocation taskUid : set) {
                Mods mod = targetBindInfo.get(taskUid.toString());
                ModTaskMixin modTaskMixin = ModTaskMixin.create(taskUid, mod, value);
                map.computeIfAbsent(taskUid.toString(), (uid) -> {
                    return new HashSet<>();
                }).add(modTaskMixin);
            }
        });
        return ModTaskMixinMap.create(map);
    }

    private static List<ModAnnotation.EnumHolder> getEnumHolderValue(ModFileScanData.AnnotationData data, String name) {
        return ((List<ModAnnotation.EnumHolder>) data.annotationData().get(name));
    }

    public record ModTaskMixin(ResourceLocation taskUid, Mods compatMod, List<String> mixinList) {

        public static final Codec<ModTaskMixin> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                ResourceLocation.CODEC.fieldOf("taskUid").forGetter(ModTaskMixin::taskUid),
                Mods.CODEC.fieldOf("compatMod").forGetter(ModTaskMixin::compatMod),
                Codec.STRING.listOf().fieldOf("mixinList").forGetter(ModTaskMixin::mixinList)
        ).apply(ins, ModTaskMixin::new));

        public static ModTaskMixin create(ResourceLocation taskUid, Mods compatMod, Set<String> mixinList) {
            return new ModTaskMixin(taskUid, compatMod, Lists.newArrayList(mixinList));
        }
    }

    public static class ModTaskMixinMap {
        public static final Codec<ModTaskMixinMap> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                Codec.unboundedMap(Codec.STRING, ModTaskMixin.CODEC.listOf()).fieldOf("list").forGetter(ModTaskMixinMap::map)
        ).apply(ins, ModTaskMixinMap::new));
        private final Map<String, List<ModTaskMixin>> map;
        private final Map<ResourceLocation, List<String>> taskMixinList;

        public ModTaskMixinMap(Map<String, List<ModTaskMixin>> map) {
            this.map = map;

            Map<ResourceLocation, List<String>> taskMixinList0 = new HashMap<>();
            map.values().stream().flatMap((v) -> {
                return v.stream();
            }).forEach(modTaskMixin -> {
                taskMixinList0.computeIfAbsent(modTaskMixin.taskUid, (uid) -> {
                    return new ArrayList<>();
                }).addAll(modTaskMixin.mixinList());
            });
            this.taskMixinList = taskMixinList0;
        }

        public static ModTaskMixinMap create(Map<String, Set<ModTaskMixin>> map) {
            Map<String, List<ModTaskMixin>> map0 = new HashMap<>();
            map.forEach((key, val) -> {
                map0.put(key, Lists.newArrayList(val));
            });
            return new ModTaskMixinMap(map0);
        }

        public Map<ResourceLocation, List<String>> getMixinList() {
            Map<ResourceLocation, Set<String>> map = new HashMap<>();
            this.map.forEach((taskUid, list) -> {
                List<String> mixinList0 = list.stream()
                        .flatMap(l -> {
                            return l.mixinList.stream();
                        })
                        .toList();
                map.computeIfAbsent(VResourceLocation.create(taskUid), (t) -> {
                    return new HashSet<>();
                }).addAll(mixinList0);
            });
            return MapUtil.set2List(map);
        }

        private Map<String, List<ModTaskMixin>> map() {
            return map;
        }

        public boolean canMixin(String sourceMixinClazz) {
            return Optional.ofNullable(map.get(sourceMixinClazz))
                    .map(info -> {
                        for (ModTaskMixin modTaskMixin : info) {
                            if (modTaskMixin.compatMod.versionLoaded) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .orElse(true);
        }

        public boolean isApplyMixin(ResourceLocation task) {
            boolean apply = true;
            for (String targetClass : taskMixinList.getOrDefault(task, List.of())) {
                if (!IMaidsoulKitchenInterface.applyInterfaceMixin(targetClass)) {
                    Mods compatMod = map.get(task.toString()).get(0).compatMod;
                    TaskLoadError.putError(task);
                    MaidsoulKitchen.LOGGER.error("ModTaskMixinMap.MixinError: task: {}, class: {}", task, targetClass);
                    apply = false;
                }
            }
            return apply;
        }
    }

}
