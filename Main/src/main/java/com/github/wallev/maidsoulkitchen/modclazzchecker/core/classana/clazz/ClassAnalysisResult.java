package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz;


import java.util.*;

public class ClassAnalysisResult {
    final String uid;
    final String modId;
    final String version;
    final Set<String> classes = new TreeSet<>();
    final Set<String> methods = new TreeSet<>();
    final Set<String> fields = new TreeSet<>();
    final Set<String> mixins = new TreeSet<>();
    final Map<String, Boolean> classExistence = new HashMap<>();
    final Map<String, Boolean> methodExistence = new HashMap<>();
    final Map<String, Boolean> fieldExistence = new HashMap<>();
    final Map<String, Boolean> mixinExistence = new HashMap<>();
    final List<LogEntry> logs = new ArrayList<>();

    public ClassAnalysisResult(String uid, String modId, String version) {
        this.uid = uid;
        this.modId = modId;
        this.version = version;
    }

    public void addLog(LogEntry entry) {
        logs.add(entry);
    }
}
