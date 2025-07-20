package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana;

public interface IMods {
    String modId();
    String versionRange();
    boolean versionLoad();
    boolean load();
    String name();
}
