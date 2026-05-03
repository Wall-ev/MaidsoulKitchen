package com.github.wallev.maidsoulkitchen.vhelper.client.resources;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMods;
import com.github.wallev.maidsoulkitchen.vhelper.IModInfo;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class VResourceLocation{

    public static ResourceLocation create(String location) {
        return new ResourceLocation(location);
    }

    @Nullable
    public static ResourceLocation tryParse(String location) {
        return ResourceLocation.tryParse(location);
    }

    public static ResourceLocation createMod(String extension) {
        return new ResourceLocation(IModInfo.MOD_ID, extension);
    }

    public static ResourceLocation createTypeMod(ResourceLocation recipeType) {
        if (recipeType.getNamespace().isEmpty()) {
            return new ResourceLocation(IModInfo.MOD_ID, recipeType.getPath());
        } else {
            return new ResourceLocation(IModInfo.MOD_ID + "_" + recipeType.getNamespace(), recipeType.getPath());
        }
    }

    public static ResourceLocation createTypeMod(String modId, String type) {
        return createTypeMod(new ResourceLocation(modId, type));
    }

    public static ResourceLocation createTypeMod(IMods modId, String type) {
        return createTypeMod(new ResourceLocation(modId.modId(), type));
    }

    public static ResourceLocation createTypeMod(String recipeType) {
        return createTypeMod(VResourceLocation.create(recipeType));
    }
    
    public static ResourceLocation create(String nameSpace, String path) {
        return new ResourceLocation(nameSpace, path);
    }
}
