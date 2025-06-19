package com.github.wallev.maidsoulkitchen.util.debug;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

public class AspectDebug {
    private static final String MOD_ID = MaidsoulKitchen.MOD_ID;
    // 此版本号[,0.0]为开发环境版本号,由 build.gradle#tasks#processResources 控制
    private static final String VERSION = "[,0.0]";

    public static void init() {
        try {
            if (MaidsoulKitchen.DEBUG && isMaidsoulKitchenDev()) {
                AspectDebugInner.init();
            }
        } catch (InvalidVersionSpecificationException e) {
            throw new RuntimeException();
        }
    }

    /**
     * 判断环境是否为 Maidsoul Kitchen 的开发环境
     */
    private static boolean isMaidsoulKitchenDev() throws InvalidVersionSpecificationException {
        VersionRange versionRange = VersionRange.createFromVersionSpec(VERSION);
        IModFileInfo modFileById = ModList.get().getModFileById(MOD_ID);
        ArtifactVersion version = modFileById.getMods().get(0).getVersion();
        return versionRange.containsVersion(version);
    }
}
