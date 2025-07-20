package com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ITaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ModGroup;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz.ClassAnalyzerManager;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz.TaskClazzInfo;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class BaseClazzCheckManager<T extends ITaskInfo<M>, M extends IMods> {
    private final String modId;
    private final String issueUrl;
    private final String modPackage;
    private final String mixinPackage;
    private final Set<String> mcGroups = Sets.newHashSet(ModGroup.MC.groups);
    private final Set<String> blackGroups = Sets.newHashSet(ModGroup.BLACK.groups);
    private final Codec<T> taskCodec;
    private final Codec<M> modsCodec;

    private final Set<String> errorTasks = new HashSet<>();
    private boolean reported = false;

    private String fileName = "mod_task_clazz.json";

    public BaseClazzCheckManager(String modId, String issueUrl, String modPackage, String mixinPackage) {
        this.modId = modId;
        this.issueUrl = issueUrl;
        this.modPackage = modPackage;
        this.mixinPackage = mixinPackage;
        this.taskCodec = this.createTaskInfoCodec();
        this.modsCodec = this.createModsCodec();
        this.mcGroups.addAll(this.getExtractMcGroups());
        this.blackGroups.add(modPackage);
        this.blackGroups.addAll(this.getExtractBlackGroups());
    }

    public void writeModTaskClazz(Path rootOutputFolder) throws Exception {
        ClassAnalyzerManager.writeModTaskClazz(rootOutputFolder, this);
    }

    public TaskClazzInfo readModTaskClazzFromFile() {
        return ClassAnalyzerManager.readModTaskClazzFromFile(this);
    }

    public Map<String, Boolean> readModTaskClazz(TaskClazzInfo taskClazzInfo) throws IOException {
        return ClassAnalyzerManager.readModTaskClazz(taskClazzInfo, this);
    }

    public Map<String, Boolean> readModTaskClazz() throws IOException {
        return ClassAnalyzerManager.readModTaskClazz(this);
    }

    public String getFileName() {
        return fileName;
    }

    protected void setFileName(String fileName) {
        this.fileName = fileName;
    }

    protected Codec<T> getTaskCodec() {
        return taskCodec;
    }

    public final Codec<ITaskInfo<?>> getTaskCodecO() {
        return (Codec<ITaskInfo<?>>) getTaskCodec();
    }

    protected abstract Codec<T> createTaskInfoCodec();

    protected Codec<M> getModsCodec() {
        return modsCodec;
    }

    public final Codec<IMods> getModsCodecO() {
        return (Codec<IMods>) getModsCodec();
    }

    protected abstract Codec<M> createModsCodec();

    public String getModId() {
        return modId;
    }

    public String getMixinPackage() {
        return mixinPackage;
    }

    public abstract T taskInfoByKey(String key);

    public abstract T taskInfoByUid(String uid);

    public abstract M modsByKey(String mod);

    public abstract Type getTaskClazzAnnotationType();

    public abstract Type getTaskClazzMixinAnnotationType();

    public String getModPackage() {
        return modPackage;
    }

    public Set<String> getMcGroups() {
        return mcGroups;
    }

    public Set<String> getBlackGroups() {
        return blackGroups;
    }

    protected Set<String> getExtractMcGroups() {
        return new HashSet<>();
    }

    protected Set<String> getExtractBlackGroups() {
        return Sets.newHashSet(modPackage);
    }

    public String getIssueUrl() {
        return issueUrl;
    }

    public Set<String> getErrorTasks() {
        return errorTasks;
    }

    public void addErrorTask(String task) {
        errorTasks.add(task);
    }

    public boolean needReportErrorTasks() {
        return !reported && !errorTasks.isEmpty();
    }

    public void markReported() {
        reported = true;
    }

    public Set<String> getExtractMod() {
        return new HashSet<>();
    }

    public Set<String> getCompatMods() {
        return new HashSet<>();
    }
}
