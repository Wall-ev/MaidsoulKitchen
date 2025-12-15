package com.github.wallev.maidsoulkitchen.modclazzchecker.manager.taskinfo;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.config.subconfig.TaskRegisterConfig;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskModClazzManager;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.taskinfo.condition.LoadCondition;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.type.DevModule;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.type.VersionModule;
import com.github.wallev.maidsoulkitchen.util.DevUtil;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;

public enum TaskInfo0 {
    @DevModule
    @VersionModule(legacy0 = MaidsoulKitchen.MOD_ID + "_chopping_board")
    KC_CHOPPING_BOARD(
            VResourceLocation.of(Mods.KC, "chopping_board", "chopping_board"),
            LoadCondition.of(Mods.KC)
    ),

    YHC_TEA_KETTLE(
            VResourceLocation.of(Mods.YHCD_250_, "tea_kettle", "tea_kettle"),
            LoadCondition.or(Mods.YHCD_250_, Mods.YHCF)
    ),
    YHC_TEA_KETTLE_LEGACY(
            YHC_TEA_KETTLE.uid(),
            LoadCondition.of(Mods.YHCD_LEGACY)
    ),

    MSM_CORE(
            VResourceLocation.ofMod("msm_core"),
            LoadCondition.of(Mods.MAID_STORAGE_MANAGER)
    ),
    MSM_YHC_TEA_KETTLE(
            VResourceLocation.ofMod(Mods.YHCD_250_.getModId(), "tea_kettle", "tea_kettle"),
            LoadCondition.and(
                    LoadCondition.of(MSM_CORE),
                    LoadCondition.or(
                            LoadCondition.of(Mods.YHCD_250_),
                            LoadCondition.of(Mods.YHCF)
                    )
            )
    ),
    MSM_YHC_TEA_KETTLE_LEGACY(
            MSM_YHC_TEA_KETTLE.uid(),
            LoadCondition.and(
                    LoadCondition.of(MSM_CORE),
                    LoadCondition.of(Mods.YHCD_LEGACY)
            )
    ),
    MSM_FD_HAND_SKILLET(
            VResourceLocation.ofMod("msm_fd_hand_skillet"),
            LoadCondition.and(
                    LoadCondition.of(MSM_CORE),
                    LoadCondition.of(Mods.FD)
            )
    ),



    ;

    public static final ResourceLocation EMPTY_UID = VResourceLocation.of(MaidsoulKitchen.MOD_ID, "empty");

    private final ResourceLocation uid;
    private final LoadCondition<?> loadCondition;

    private final ResourceLocation oldUid;

    TaskInfo0(ResourceLocation uid, LoadCondition<?> loadCondition) {
        this.uid = uid;
        this.loadCondition = loadCondition;

        this.oldUid = getOldUid();
    }

    private ResourceLocation getOldUid() {
        if (this.getDeclaringClass().isAnnotationPresent(VersionModule.class)) {
            VersionModule declaredAnnotation = this.getDeclaringClass().getDeclaredAnnotation(VersionModule.class);
            return VResourceLocation.of(declaredAnnotation.legacy0());
        }
        return EMPTY_UID;
    }

    public ResourceLocation uid() {
        return uid;
    }

    public ResourceLocation oldUid() {
        return oldUid;
    }

    public LoadCondition<?> loadCondition() {
        return loadCondition;
    }

    public Mods bindMod() {
        return null;
    }

    public boolean canLoad() {
        return canLoadWithoutCheckClazz() && TaskModClazzManager.clazzLoad(uid.toString());
    }

    public boolean canLoadWithoutCheckClazz() {
        return canLoadByCondition() && canLoadByConfig() && canLoadByDevModuleIfPresent();
    }

    public boolean canLoadByDevModuleIfPresent() {
        if (isDevModule())
            return DevUtil.isDevEnv();
        return true;
    }

    public boolean isDevModule() {
        return this.getDeclaringClass().isAnnotationPresent(DevModule.class);
    }

    public boolean canLoadByCondition() {
        return loadCondition.canLoad();
    }

    public boolean canLoadByConfig() {
        return TaskRegisterConfig.enabled(uid);
    }

    public String getSerializedName() {
        return "";
    }
}
