package com.github.wallev.maidsoulkitchen.util.classana.clazz;

import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class ClassAnalysisResult {
    final ResourceLocation uid;
    final String modId;
    final String version;
    final Set<String> classes = new TreeSet<>();
    final Set<String> methods = new TreeSet<>();
    final Set<String> fields = new TreeSet<>();
    final Map<String, Boolean> classExistence = new HashMap<>();
    final Map<String, Boolean> methodExistence = new HashMap<>();
    final Map<String, Boolean> fieldExistence = new HashMap<>();
    final List<LogEntry> logs = new ArrayList<>();

    public ClassAnalysisResult(ResourceLocation uid, String modId, String version) {
        this.uid = uid;
        this.modId = modId;
        this.version = version;
    }

    public void addLog(LogEntry entry) {
        logs.add(entry);
    }
}
