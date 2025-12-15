package com.github.wallev.maidsoulkitchen.modclazzchecker.manager.taskinfo;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;

import java.util.Arrays;

public interface IModuleConfig<T> {

    boolean canLoad();

    T get();

    static boolean of(ILoadType loadType, IModuleConfig<?>... configLoad) {
        return loadType.checkCanLoad(configLoad);
    }

    record ModModuleConfig(Mods mods) implements IModuleConfig<Mods> {

        @Override
        public boolean canLoad() {
            return mods.versionLoad();
        }

        @Override
        public Mods get() {
            return mods;
        }
    }

    record TaskInfoModuleConfig(TaskInfo0 taskInfo0) implements IModuleConfig<TaskInfo0> {

        @Override
        public boolean canLoad() {
            return taskInfo0.canLoad();
        }

        @Override
        public TaskInfo0 get() {
            return taskInfo0;
        }
    }

    interface ILoadType {
        boolean checkCanLoad(IModuleConfig<?>... configLoad);
    }

    enum LoadType implements ILoadType {
        AND {
            @Override
            public boolean checkCanLoad(IModuleConfig<?>... configLoad) {
                return Arrays.stream(configLoad).allMatch(IModuleConfig::canLoad);
            }
        },
        OR {
            @Override
            public boolean checkCanLoad(IModuleConfig<?>... configLoad) {
                return Arrays.stream(configLoad).anyMatch(IModuleConfig::canLoad);
            }
        },
        ;

        public abstract boolean checkCanLoad(IModuleConfig<?>... config0);
    }

}
