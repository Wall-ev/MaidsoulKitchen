package com.github.wallev.maidsoulkitchen.vhelper.client.resources;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMods;
import com.github.wallev.maidsoulkitchen.vhelper.IModInfo;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class VResourceLocation{

    public static ResourceLocation of(String location) {
        return new ResourceLocation(location);
    }

    public static ResourceLocation of(String nameSpace, String... location) {
        return new ResourceLocation(nameSpace, String.join("/", location));
    }

    public static ResourceLocation ofMod(String... location) {
        return new ResourceLocation(IModInfo.MOD_ID, String.join("/", location));
    }

    public static ResourceLocation of(IMods mods, String... location) {
        return new ResourceLocation(mods.modId(), String.join("/", location));
    }


    @Nullable
    public static ResourceLocation tryParse(String location) {
        return ResourceLocation.tryParse(location);
    }

    public static ResourceLocation ofMod(String extension) {
        return new ResourceLocation(IModInfo.MOD_ID, extension);
    }

    public static ResourceLocation ofTypeMod(ResourceLocation recipeType) {
        if (recipeType.getNamespace().isEmpty()) {
            return new ResourceLocation(IModInfo.MOD_ID, recipeType.getPath());
        } else {
            return new ResourceLocation(IModInfo.MOD_ID + "_" + recipeType.getNamespace(), recipeType.getPath());
        }
    }

    public static ResourceLocation ofTypeMod(String modId, String type) {
        return ofTypeMod(new ResourceLocation(modId, type));
    }

    public static ResourceLocation ofTypeMod(IMods modId, String type) {
        return ofTypeMod(new ResourceLocation(modId.modId(), type));
    }

    public static ResourceLocation ofTypeMod(String recipeType) {
        return ofTypeMod(VResourceLocation.of(recipeType));
    }
    
    public static ResourceLocation of(String nameSpace, String path) {
        return new ResourceLocation(nameSpace, path);
    }
}
