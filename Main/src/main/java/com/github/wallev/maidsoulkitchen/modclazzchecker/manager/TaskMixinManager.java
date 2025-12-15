package com.github.wallev.maidsoulkitchen.modclazzchecker.manager;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ModTaskMixin;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ModTaskMixinMap;
import com.github.wallev.maidsoulkitchen.vhelper.IModInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.fml.loading.LoadingModList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskMixinManager {
    private static final String FILE_NAME = "mod_task_clazz.json";

    public static ModTaskMixinMap readModTaskMixinClazzFromFile() {
        try {
            Map<String, List<ModTaskMixin>> map = new HashMap<>();

            Path resource = LoadingModList.get().getModFileById(IModInfo.MOD_ID)
                    .getFile()
                    .findResource(FILE_NAME);
            String json = Files.readString(resource);
            JsonObject jsonData = JsonParser.parseString(json).getAsJsonObject().getAsJsonObject("mixinInfo").getAsJsonObject("list");
            for (Map.Entry<String, JsonElement> entry : jsonData.entrySet()) {
                String taskId = entry.getKey();
                JsonArray vals = entry.getValue().getAsJsonArray();
                for (JsonElement val : vals) {
                    JsonObject asJsonObject = val.getAsJsonObject();
                    String taskUid = asJsonObject.get("taskUid").getAsString();
                    String compatModKey = asJsonObject.get("compatMod").getAsString();
                    Mods compatMod = Mods.by(compatModKey);
                    JsonArray mixinList = asJsonObject.getAsJsonArray("mixinList");
                    List<String> mixins = new ArrayList<>();
                    for (JsonElement mixin : mixinList) {
                        String mixinClazz = mixin.getAsString();
                        mixins.add(mixinClazz);
                    }
                    ModTaskMixin modTaskMixin = new ModTaskMixin(taskUid, compatMod, mixins);
                    map.computeIfAbsent(taskId, (uid) -> {
                        return new ArrayList<>();
                    }).add(modTaskMixin);

                }
            }
            return new ModTaskMixinMap(map);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
