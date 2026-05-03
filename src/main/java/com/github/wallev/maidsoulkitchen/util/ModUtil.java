package com.github.wallev.maidsoulkitchen.util;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     *     public static String getModIssueUrl(String modId) {
     *         String issueUrl = "";
     *
     *         ModList modList = ModList.get();
     *         if (modList != null) {
     *             ModContainer modContainer = modList.getModContainerById(modId).orElse(null);
     *             if (modContainer == null) {
     *                 return issueUrl;
     *             }
     *             IModFileInfo owningFile = modContainer.getModInfo().getOwningFile();
     *             IConfigurable config = owningFile.getConfig();
     *             Optional<String> issueTrackerURL = config.<String>getConfigElement("issueTrackerURL");
     *             issueUrl = issueTrackerURL.orElse("");
     *         } else {
     *             ModFileInfo modFileById = LoadingModList.get().getModFileById(modId);
     *             if (modFileById != null) {
     *                 issueUrl = modFileById.getMods().get(0).getConfig().<String>getConfigElement("issueTrackerURL").orElse("");
     *             }
     *         }
     *         return issueUrl;
     *     }
     *
     */

    public static String getModName(String modId) {
        IModInfo modInfo = getModInfo(modId);
        return modInfo != null ? modInfo.getDisplayName() : StringUtils.EMPTY;
    }

    @Nullable
    public static IModInfo getModInfo(String modId) {
        ModList modList = ModList.get();
        if (modList != null) {
            ModContainer modContainer = modList.getModContainerById(modId).orElse(null);
            if (modContainer != null) {
                return modContainer.getModInfo();
            }
        } else {
            ModFileInfo modFileById = LoadingModList.get().getModFileById(modId);
            if (modFileById != null) {
                return modFileById.getMods().get(0);
            }
        }
        return null;
    }

    // [x.x.x,)
    public static String getModVersion(String modId) {

        IModInfo modInfo = getModInfo(modId);
        if (modInfo == null) {
            return "";
        }

        ArtifactVersion version = modInfo.getVersion();

//        if (version.getQualifier() != null) {
//            version = new DefaultArtifactVersion(version.getQualifier());
//        }
        return version.toString();

    }

    // [x.x.x,)
    public static String getAutualMaidsoulKitchenVersion() {
        // 正则表达式匹配括号中的内容
        Pattern pattern = Pattern.compile("\\((.*?)\\)");

        IModInfo modInfo = getModInfo(Mods.MSK.getModId());
        if (modInfo == null) {
            return "";
        }

        String displayName = modInfo.getDisplayName();

        Matcher matcher = pattern.matcher(displayName);
        // 如果找到匹配项，提取括号中的内容
        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
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
