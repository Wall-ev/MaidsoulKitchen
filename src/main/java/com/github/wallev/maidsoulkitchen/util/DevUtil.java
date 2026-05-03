package com.github.wallev.maidsoulkitchen.util;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.language.IModFileInfo;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

import java.util.List;
import java.util.UUID;

public class DevUtil {
    private static final String MOD_ID = MaidsoulKitchen.MOD_ID;
    // 此版本号[,0.0]为开发环境版本号,由 build.gradle#tasks#processResources 控制
    private static final String VERSION = "[,0.0]";

    private static final boolean DEBUG = !FMLEnvironment.production;

    private static final boolean MAIDSOUL_KITCHEN_DEV;

    static {
        try {
            MAIDSOUL_KITCHEN_DEV = isMaidsoulKitchenDev();
        } catch (InvalidVersionSpecificationException e) {
            throw new RuntimeException(e);
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

    private static boolean isDev() {
        return isDevEnv() || User.IS_DEV_USER;
    }

    public static boolean isDevEnv() {
        return DEBUG && MAIDSOUL_KITCHEN_DEV;
    }

    private static class User {
        private static final UUID DEV_USER = UUID.fromString("81d2e1b0-1c89-48ec-8d68-98f7995aaff7");
        private static final List<String> DEV_USER_NAME = List.of("seven_lifet", "Abert_Cat", "Albert_Cat", "Catbert");
        private static final boolean IS_DEV_USER = isDevUser();

        private static boolean isDevUser() {
            if (FMLEnvironment.dist.isDedicatedServer()) {
                return false;
            }

            Minecraft minecraft = Minecraft.getInstance();
            String userId = minecraft.getUser().getUuid();
            if (DEV_USER.toString().equals(userId)) {
                return true;
            }

//            String userName = minecraft.getUser().getName();
//            if (DEV_USER_NAME.contains(userName)) {
//                return true;
//            }

            return false;
        }
    }
}
