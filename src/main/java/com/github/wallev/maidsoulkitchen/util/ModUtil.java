package com.github.wallev.maidsoulkitchen.util;

import com.github.wallev.maidsoulkitchen.util.modutility.Mods;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

public class ModUtil {
    public static boolean isInstalled(String modId) {
        ModList modList = ModList.get();
        if (modList != null) {
            return modList.isLoaded(modId);
        } else {
            return LoadingModList.get().getModFileById(modId) != null;
        }
    }

    // [x.x.x,)
    public static boolean isInstalled(String modId, String spec) {
        try {
            String modVersion = getModVersion(modId);
            if (modVersion.isEmpty()) {
                return false;
            }
            ArtifactVersion version = new DefaultArtifactVersion(modVersion);

            VersionRange versionRange = VersionRange.createFromVersionSpec(spec);
            if (versionRange.containsVersion(version)) {
                return true;
            } else {
                // 开发环境下，version 是空的，所以需要额外判断
//                return !FMLEnvironment.production;
                return false;
            }

        } catch (InvalidVersionSpecificationException e) {
            throw new RuntimeException(e);
        }
    }

    // [x.x.x,)
    public static String getModVersion(String modId) {
        ArtifactVersion version = null;

        ModList modList = ModList.get();
        if (modList != null) {
            ModContainer modContainer = modList.getModContainerById(modId).orElse(null);
            if (modContainer == null) {
                return "";
            }
            version = modContainer.getModInfo().getVersion();
        } else {
            ModFileInfo modFileById = LoadingModList.get().getModFileById(modId);
            if (modFileById != null) {
                version = modFileById.getMods().get(0).getVersion();
            }
        }

        if (version == null) {
            return "";
        }

        if (version.getQualifier() != null) {
            version = new DefaultArtifactVersion(version.getQualifier());
        }
        return version.toString();

    }

    public static boolean allLoaded(String... modIds) {
        ModList modList = ModList.get();
        for (String modId : modIds) {
            if (!modList.isLoaded(modId)) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasLoaded(String... modIds) {
        ModList modList = ModList.get();
        for (String modId : modIds) {
            if (modList.isLoaded(modId)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasLoaded(Mods... mods) {
        ModList modList = ModList.get();
        for (Mods mod : mods) {
            if (mod.isInstalled()) {
                return true;
            }
        }
        return false;
    }

}
