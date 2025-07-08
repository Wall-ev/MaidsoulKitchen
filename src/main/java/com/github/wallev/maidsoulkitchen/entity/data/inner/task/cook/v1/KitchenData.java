package com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v1;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class KitchenData {
    public static final Codec<Map<ResourceLocation, CookDataV1>> MAP_CODEC = Codec.unboundedMap(ResourceLocation.CODEC, CookDataV1.CODEC)
            .xmap(HashMap::new, Function.identity());
    public static final Codec<KitchenData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MAP_CODEC.fieldOf("type_data").forGetter((kitchenData) -> kitchenData.cookData),
            ResourceLocation.CODEC.fieldOf("kitchen_name").forGetter((kitchenData) -> kitchenData.cookName)
    ).apply(instance, KitchenData::new));

    private final Map<ResourceLocation, CookDataV1> cookData;
    private ResourceLocation cookName;

    public KitchenData(Map<ResourceLocation, CookDataV1> cookData, ResourceLocation cookName) {
        this.cookData = cookData;
        this.cookName = cookName;
    }

    public KitchenData() {
        this(new HashMap<>(), TaskInfo.IDLE.getUid());
    }

    public ResourceLocation getCookName() {
        return cookName;
    }

    public void setCookName(ResourceLocation cookName) {
        this.cookName = cookName;
    }

    public Map<ResourceLocation, CookDataV1> getCookDatas() {
        return cookData;
    }

    public CookDataV1 getCookData(ResourceLocation name) {
        if (!cookData.containsKey(name)) {
            cookData.put(name, new CookDataV1());
        }

        return cookData.get(name);
    }

    public CookDataV1 getCookData() {
        return cookData.getOrDefault(cookName, new CookDataV1());
    }

    public void setCookData(ResourceLocation name, CookDataV1 cookData) {
        this.cookData.put(name, cookData);
    }
}
