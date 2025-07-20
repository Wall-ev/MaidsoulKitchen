package com.github.wallev.maidsoulkitchen.vhelper.client.resources;

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
    
    public static ResourceLocation create(String nameSpace, String path) {
        return new ResourceLocation(nameSpace, path);
    }
}
