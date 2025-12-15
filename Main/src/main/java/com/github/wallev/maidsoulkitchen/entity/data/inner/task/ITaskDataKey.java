package com.github.wallev.maidsoulkitchen.entity.data.inner.task;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;

import java.util.Optional;

public abstract class ITaskDataKey<T> implements TaskDataKey<T> {
    private final IVersionSerializer<T, ?>[] versionSerializers;

    @SafeVarargs
    protected ITaskDataKey(IVersionSerializer<T, ?>... versionSerializers) {
        this.versionSerializers = versionSerializers;
    }

    @Override
    public CompoundTag writeSaveData(T data) {
        return codec().encodeStart(NbtOps.INSTANCE, data)
                .resultOrPartial(MaidsoulKitchen.LOGGER::error)
                .map(tag -> (CompoundTag) tag)
                .orElseGet(CompoundTag::new);
    }

    @Override
    public T readSaveData(CompoundTag compound) {
        return codec().parse(NbtOps.INSTANCE, compound)
                .resultOrPartial(MaidsoulKitchen.LOGGER::error)
                .orElseGet(() -> {
                    for (IVersionSerializer<T, ?> serializer : versionSerializers) {
                        Optional<?> result = serializer.getCodec().parse(NbtOps.INSTANCE, compound).result();
                        if (result.isPresent()) {
                            return serializer.toObjNew(result.get());
                        }
                    }

                    return defaultData();
                });
    }

    public abstract Codec<T> codec();

    public abstract T defaultData();

    /**
     * 数据迁移
     */
    public interface IVersionSerializer<NEW, SELF> {

        NEW toNew(SELF self);

        @SuppressWarnings("unchecked")
        default NEW toObjNew(Object self) {
            return toNew((SELF) self);
        }

        Codec<SELF> getCodec();

    }
}
