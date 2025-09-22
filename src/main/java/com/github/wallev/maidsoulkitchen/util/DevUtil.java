package com.github.wallev.maidsoulkitchen.util;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.List;
import java.util.UUID;

public class DevUtil {
    private static final boolean DEBUG = !FMLEnvironment.production;

    public static boolean isDev() {
        return isDevEnv() || User.IS_DEV_USER;
    }

    public static boolean isDevEnv() {
        return DEBUG;
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
