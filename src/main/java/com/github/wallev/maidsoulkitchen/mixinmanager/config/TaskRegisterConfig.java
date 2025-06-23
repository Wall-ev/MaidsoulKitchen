package com.github.wallev.maidsoulkitchen.mixinmanager.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;

public class TaskRegisterConfig {
    public static final Codec<TaskRegisterConfig> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            Codec.STRING.fieldOf("__version").forGetter(o -> o.version),
            IssueCommentConfig.CODEC.fieldOf("__comment").forGetter(o -> o.issueCommentConfig),
            TaskMapConfig.MAP_CODEC.fieldOf("task").forGetter(o -> o.map)
    ).apply(ins, TaskRegisterConfig::new));
    private String version;
    private IssueCommentConfig issueCommentConfig;
    private Map<String, TaskConfigConfig> map;

    public TaskRegisterConfig(String version, IssueCommentConfig issueCommentConfig, Map<String, TaskConfigConfig> map) {
        this.version = version;
        this.issueCommentConfig = issueCommentConfig;
        this.map = map;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public IssueCommentConfig getIssueCommentConfig() {
        return issueCommentConfig;
    }

    public void setIssueCommentConfig(IssueCommentConfig issueCommentConfig) {
        this.issueCommentConfig = issueCommentConfig;
    }

    public Map<String, TaskConfigConfig> getTaskMapConfig() {
        return map;
    }

    public void setTaskMapConfig(Map<String, TaskConfigConfig> taskMapConfig) {
        this.map = taskMapConfig;
    }

    public boolean canLoad(String task) {
        TaskConfigConfig taskConfigConfig = this.map.get(task);
        if (taskConfigConfig == null) {
            return true;
        } else {
            return taskConfigConfig.isEnableConfig() || taskConfigConfig.getMixinConfigConfig().values().stream().filter(Boolean::booleanValue).findFirst().orElse(true);
        }
    }
}
