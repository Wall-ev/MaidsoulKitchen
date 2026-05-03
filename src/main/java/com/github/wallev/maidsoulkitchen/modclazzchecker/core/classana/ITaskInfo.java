package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.util.IEnum;

public interface ITaskInfo<M extends IMods> extends IEnum {

    String getUidStr();

    M getBindMod();

    boolean canLoad();

    boolean canLoadWithoutCheckClazz();
}
