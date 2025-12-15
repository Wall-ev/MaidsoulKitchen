package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.util.IEnum;

public interface IMods extends IEnum {
    String modId();
    String versionRange();
    boolean versionLoad();
    boolean load();
    String name();
}
