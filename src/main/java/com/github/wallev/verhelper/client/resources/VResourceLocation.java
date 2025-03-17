package com.github.wallev.verhelper.client.resources;

import com.github.wallev.verhelper.IModInfo;
import net.minecraft.resources.ResourceLocation;

public class VResourceLocation{

    public static ResourceLocation create(String location) {
        return new ResourceLocation(location);
    }

    public static ResourceLocation createMod(String extension) {
        return new ResourceLocation(IModInfo.MOD_ID, extension);
    }
    
    public static ResourceLocation create(String nameSpace, String path) {
        return new ResourceLocation(nameSpace, path);
    }
}
