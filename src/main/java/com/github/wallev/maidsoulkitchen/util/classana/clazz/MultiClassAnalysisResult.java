package com.github.wallev.maidsoulkitchen.util.classana.clazz;

import com.google.common.collect.Lists;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MultiClassAnalysisResult {
    private static final FMLPaths ROOT_FOLDER = FMLPaths.GAMEDIR;
    private static final String FILE_NAME = "task_class_analysis.txt";
    private static final Path LOG_FILE_PATH = ROOT_FOLDER.get().resolve("logs\\" + FILE_NAME);

    final List<ClassAnalysisResult> classResults = new ArrayList<>();
    final List<LogEntry> globalLogs = new ArrayList<>();
    final Set<String> allClasses = new TreeSet<>();
    final Set<String> allMethods = new TreeSet<>();
    final Set<String> allFields = new TreeSet<>();

    public void addClassResult(ClassAnalysisResult result) {
        classResults.add(result);
        allClasses.addAll(result.classes);
        allMethods.addAll(result.methods);
        allFields.addAll(result.fields);
        globalLogs.addAll(result.logs);
    }

    // 导出报告到文件
    public Path exportToFile() throws IOException {
        File file = LOG_FILE_PATH.toFile();
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(generateReport());
        fileWriter.close();
        globalLogs.add(new LogEntry(LogLevel.INFO, "任务分析报告已导出到: " + file.getPath()));
        return file.toPath();
    }

    // 生成文本格式报告
    private String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("========= 任务分析报告 =========\n\n");

        // 全局统计信息
        report.append("===== 全局统计信息 =====\n");
        report.append("总类数量: ").append(allClasses.size()).append("\n");
        report.append("总方法数量: ").append(allMethods.size()).append("\n");
        report.append("总字段数量: ").append(allFields.size()).append("\n\n");

        List<String> list = classResults.stream()
                .flatMap(a -> {
                    List<String> arrayList = Lists.newArrayList();
                    a.classExistence.forEach((name, val) -> {
                        if (!val) {
                            arrayList.add(name);
                        }
                    });
                    a.fieldExistence.forEach((name, val) -> {
                        if (!val) {
                            arrayList.add(name);
                        }
                    });
                    a.methodExistence.forEach((name, val) -> {
                        if (!val) {
                            arrayList.add(name);
                        }
                    });
                    return arrayList.stream();
                }).toList();
        report.append("存在问题的成员: ").append(list.size()).append("\n");
        for (String s : list) {
            report.append("- ").append(s).append("\n");
        }

        report.append("\n===== 各任务分析详情 =====\n\n");

        // 各类分析详情
        for (ClassAnalysisResult result : classResults) {
            // 类信息
            report.append("## 任务ID： ").append(result.uid).append("、")
                    .append("模组ID： ").append(result.modId).append("、")
                    .append("模组版本： ").append(result.version)
                    .append("\n");
            // 类统计信息
            report.append("### 类统计信息\n");
            report.append("分析的类数量: ").append(result.classes.size()).append("\n");
            report.append("方法数量: ").append(result.methods.size()).append("\n");
            report.append("字段数量: ").append(result.fields.size()).append("\n\n");
            List<String> list0 = Lists.newArrayList();
            result.classExistence.forEach((name, val) -> {
                if (!val) {
                    list0.add(name);
                }
            });
            result.fieldExistence.forEach((name, val) -> {
                if (!val) {
                    list0.add(name);
                }
            });
            result.methodExistence.forEach((name, val) -> {
                if (!val) {
                    list0.add(name);
                }
            });
            report.append("存在问题的成员: ").append(list0.size()).append("\n");
            for (String s : list0) {
                report.append("- ").append(s).append("\n");
            }
            report.append("\n");

            // 类列表
            report.append("#### 类列表\n");
            if (result.classes.isEmpty()) {
                report.append("未发现类\n");
            } else {
                result.classes.forEach(m -> {
                    boolean exists = result.classExistence.getOrDefault(m, false);
                    report.append(exists ? "[✓] " : "[✗] ").append(m).append("\n");
                });
            }
            report.append("\n");

            // 方法列表
            report.append("#### 方法列表\n");
            if (result.methods.isEmpty()) {
                report.append("未发现方法\n");
            } else {
                result.methods.forEach(m -> {
                    boolean exists = result.methodExistence.getOrDefault(m, false);
                    report.append(exists ? "[✓] " : "[✗] ").append(m).append("\n");
                });
            }
            report.append("\n");

            // 字段列表
            report.append("#### 字段列表\n");
            if (result.fields.isEmpty()) {
                report.append("未发现字段\n");
            } else {
                result.fields.forEach(f -> {
                    boolean exists = result.fieldExistence.getOrDefault(f, false);
                    report.append(exists ? "[✓] " : "[✗] ").append(f).append("\n");
                });
            }
            report.append("\n");

            // 类分析日志
            report.append("#### 类分析日志\n");
            result.logs.forEach(log -> report.append(log).append("\n"));
            report.append("\n");
        }

        // 全局分析日志
        report.append("### 全局分析日志\n");
        globalLogs.forEach(log -> report.append(log).append("\n"));

        return report.toString();
    }
}
