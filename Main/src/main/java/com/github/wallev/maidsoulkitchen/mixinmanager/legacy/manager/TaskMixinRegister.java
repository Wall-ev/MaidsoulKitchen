package com.github.wallev.maidsoulkitchen.mixinmanager.legacy.manager;

import com.github.wallev.maidsoulkitchen.mixinmanager.legacy.config.*;

import java.util.HashMap;

public class TaskMixinRegister {
    public static TaskRegisterConfig create() {
        String version = "V1";
        IssueCommentConfig issueCommentConfig = new IssueCommentConfig("",
                "如果你发现某一个任务崩溃了，或者你在下方找到了对应的mixin问题，那就找到对应的任务直接禁用即可，可在下方进行禁用，然后重启游戏即可，并向作者反馈！",
        "");
        HashMap<String, TaskConfigConfig> taskConfigConfig = new HashMap<>();
        taskConfigConfig.put("yhc_fermentation_tank", new TaskConfigConfig(issueCommentConfig, true, new HashMap<>()));
        return new TaskRegisterConfig(version, issueCommentConfig, taskConfigConfig);
    }
}
