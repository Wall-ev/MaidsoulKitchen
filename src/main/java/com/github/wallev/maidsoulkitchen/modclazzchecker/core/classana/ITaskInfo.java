package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana;


public interface ITaskInfo<M extends IMods> {

    String getUidStr();

    M getBindMod();

    boolean canLoad();

    boolean canLoadWithoutCheckClazz();
}
