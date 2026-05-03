package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide;

import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.ModLang;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.lang.MsmLangUtil;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;

public class Lang {

    @ModLang(
            value = Mods.MSK,
            en_us = "Maidsoul Kitchen",
            zh_cn = "女仆厨房",
            custom = true
    )
    public static final MsmLangUtil.LangProvider LANG_PROVIDER = () -> null;

}
