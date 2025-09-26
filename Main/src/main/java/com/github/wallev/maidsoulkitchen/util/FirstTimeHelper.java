package com.github.wallev.maidsoulkitchen.util;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class FirstTimeHelper {
    // 临时这么写，这块后面还得重写
    private static final Path ROOT_FOLDER = FMLPaths.GAMEDIR.get().resolve("config");
    private static final String FILE_NAME = MaidsoulKitchen.MOD_ID + "__cache.json";
    private static final Path LOG_FILE_PATH = ROOT_FOLDER.resolve(FILE_NAME);

    public static boolean isFirstTime() {
        boolean exists = LOG_FILE_PATH.toFile().exists();
        if (exists) {
            return false;
        } else {
            try {
                LOG_FILE_PATH.toFile().createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }

}
