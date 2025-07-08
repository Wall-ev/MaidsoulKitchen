package com.github.wallev.maidsoulkitchen.util.classana.clazz;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.farm.ICompatHandler;
import com.github.wallev.maidsoulkitchen.task.CookTask;
import com.github.wallev.maidsoulkitchen.task.MaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.farm.handler.IFarmHandlerManager;
import com.github.wallev.maidsoulkitchen.util.ModUtil;
import com.github.wallev.maidsoulkitchen.util.TimeUtil;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.LoadingModList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class MultiClassAnalysisResult {
    public static final Path ROOT_FOLDER = FMLPaths.GAMEDIR.get().resolve("logs");
    public static final String FILE_NAME = "maidsoul_kitchen_task_class_analysis.txt";
    public static final Path LOG_FILE_PATH = ROOT_FOLDER.resolve(FILE_NAME);

    final List<ClassAnalysisResult> classResults = new ArrayList<>();
    final List<LogEntry> globalLogs = new ArrayList<>();
    final Set<String> allClasses = new TreeSet<>();
    final Set<String> allMethods = new TreeSet<>();
    final Set<String> allFields = new TreeSet<>();
    final Set<String> allMixins = new TreeSet<>();

    public void addClassResult(ClassAnalysisResult result) {
        classResults.add(result);
        allClasses.addAll(result.classes);
        allMethods.addAll(result.methods);
        allFields.addAll(result.fields);
        allMixins.addAll(result.mixins);
        globalLogs.addAll(result.logs);
    }

    // 导出报告到文件
    public Path exportToFile(Map<String, VerifyExistence.ClazzInfo> allClazzInfo) throws IOException {
        File file = LOG_FILE_PATH.toFile();
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(generateReport(allClazzInfo));
        fileWriter.close();
        globalLogs.add(new LogEntry(LogLevel.INFO, "The task analysis report has been exported to: " + file.getPath()));
        return file.toPath();
    }

    // 生成文本格式报告
    private String generateReport(Map<String, VerifyExistence.ClazzInfo> allClazzInfo) {
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
                    a.mixinExistence.forEach((name, val) -> {
                        if (!val) {
                            arrayList.add(name);
                        }
                    });
                    return arrayList.stream();
                }).toList();

        StringBuilder report = new StringBuilder();
        report.append("========= TaskAnalysisReport =========\n");
        report.append("Time: ").append(TimeUtil.getCurrentTimeWithFormat()).append("\n");
        report.append("GameVersion: ").append(FMLLoader.versionInfo().mcVersion()).append("\n");
        report.append("ModLoaderVersion: ").append(FMLLoader.versionInfo().forgeVersion()).append("\n");
        report.append("TouhouLittleMainVersion: ").append(ModUtil.getModVersion(TouhouLittleMaid.MOD_ID)).append("\n");
        report.append("MaidsoulKitchenVersion: ").append(ModUtil.getModVersion(MaidsoulKitchen.MOD_ID)).append("\n");
        report.append("ModState: ").append(list.isEmpty() ? "[Success] Congratulations, there were no problems with this run, and we wish you a great time!" : "[Error] oops，There are currently some mod compatibility failures, you can report to the author and bring this file with you to speed up the problem!").append("\n");
        if (!list.isEmpty()) {
            report.append("BugIssueUrl: ").append(MaidsoulKitchen.ISSUE_URL).append("\n");
        }
        report.append("ActualVersionWithCurrentCompatibleMod：").append("\n");
        for (String modId : compatModIds()) {
            String modVersion = ModUtil.getModVersion(modId);
            if (!modVersion.isEmpty()) {
                report.append("- ").append(modId).append(": ").append(modVersion).append("\n");
            }
        }
        report.append("\n");

        // 全局统计信息
        report.append("===== GlobalStatistics =====\n");
        report.append("ClassesCount: ").append(allClasses.size()).append("\n");
        report.append("MethodCount: ").append(allMethods.size()).append("\n");
        report.append("FieldCount: ").append(allFields.size()).append("\n");
        report.append("MixinCount: ").append(allMixins.size()).append("\n");
        report.append("Errors: ").append(list.size()).append("\n");
        for (String s : list) {
            report.append("- ").append(s).append("\n");
        }
        report.append("\n");

        report.append("===== TaskDetails =====\n\n");

        // 各类分析详情
        for (ClassAnalysisResult result : classResults) {
            // 类信息
            report.append("## TaskId： ").append(result.uid).append("、")
                    .append("ModId： ").append(result.modId).append("、")
                    .append("ModVersion： ").append(result.version)
                    .append("\n");
            // 类统计信息
            report.append("### ClassStatistics\n");
            report.append("ClassesCount: ").append(result.classes.size()).append("\n");
            report.append("MethodCount: ").append(result.methods.size()).append("\n");
            report.append("FieldCount: ").append(result.fields.size()).append("\n");
            report.append("MixinCount: ").append(result.mixins.size()).append("\n");
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
            result.mixinExistence.forEach((name, val) -> {
                if (!val) {
                    list0.add(name);
                }
            });
            report.append("Errors: ").append(list0.size()).append("\n");
            for (String s : list0) {
                report.append("- ").append(s).append("\n");
            }
            report.append("\n");

            // 类列表
            report.append("#### ClassesList\n");
            if (result.classes.isEmpty()) {
                report.append("NoClassesFound\n");
            } else {
                result.classes.forEach(m -> {
                    boolean exists = result.classExistence.getOrDefault(m, false);
                    report.append(exists ? "[Success] " : "[Error] ").append(m).append("\n");
                });
            }
            report.append("\n");

            // 方法列表
            report.append("#### MethodsList \n");
            if (result.methods.isEmpty()) {
                report.append("NoMethodFound\n");
            } else {
                result.methods.forEach(m -> {
                    boolean exists = result.methodExistence.getOrDefault(m, false);
                    report.append(exists ? "[Success] " : "[Error] ").append(m).append("\n");
                });
            }
            report.append("\n");

            // 字段列表
            report.append("#### FieldList\n");
            if (result.fields.isEmpty()) {
                report.append("NoFieldsFound\n");
            } else {
                result.fields.forEach(f -> {
                    boolean exists = result.fieldExistence.getOrDefault(f, false);
                    report.append(exists ? "[Success] " : "[Error] ").append(f).append("\n");
                });
            }
            report.append("\n");

            // Mixin列表
            report.append("#### MixinList\n");
            if (result.mixins.isEmpty()) {
                report.append("NoMixinsFound\n");
            } else {
                result.mixins.forEach(f -> {
                    boolean exists = result.mixinExistence.getOrDefault(f, false);
                    report.append(exists ? "[Success] " : "[Error] ").append(f).append("\n");
                });
            }
            report.append("\n");

            // 类分析日志
            report.append("#### ClassAnalysisLogs\n");
            result.logs.forEach(log -> report.append(log).append("\n"));
            report.append("\n");
        }

        // 全局分析日志
        report.append("### AnalyzeLogsGlobally\n");
        globalLogs.forEach(log -> report.append(log).append("\n"));

        report.append("\n##ModList").append("\n");
        LoadingModList.get().getMods().forEach(modInfo -> {
            String modId = modInfo.getModId();
            String version = modInfo.getVersion().toString();
            report.append("- ").append(modId).append(": ").append(version).append("\n");
        });

        /**
         *         report.append("\n##ClassInfo\n");
         *         allClazzInfo.forEach((clazz, info) -> {
         *             List<String> methods = info.methods();
         *             List<String> fields = info.fields();
         *
         *             report.append("Class：").append(clazz).append("\n");
         *             report.append("Method: ").append("\n");
         *             for (String method : methods) {
         *                 report.append("- ").append(method).append("\n");
         *             }
         *             report.append("Field: ").append("\n");
         *             for (String field : fields) {
         *                 report.append("- ").append(field).append("\n");
         *             }
         *         });
         */

        return report.toString();
    }

    private static Set<String> compatModIds() {
        Set<String> modIds = new HashSet<>();
        modIds.addAll(Arrays.stream(MaidsoulKitchenTask.values()).map(t -> t.modId).toList());
        modIds.addAll(Arrays.stream(CookTask.values()).map(t -> t.modId).toList());
        IFarmHandlerManager.HANDLER_MAP.values().forEach(fm -> {
            fm.forEach(m -> {
                ResourceLocation uid = m.getFarmHandler().getUid();
                TaskInfo by = TaskInfo.by(uid);
                if (by != null) {
                    modIds.add(by.getBindMod().modId);
                }
            });
        });
        return modIds;
    }
}
