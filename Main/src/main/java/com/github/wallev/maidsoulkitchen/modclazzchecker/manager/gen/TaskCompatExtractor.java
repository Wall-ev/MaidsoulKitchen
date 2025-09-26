package com.github.wallev.maidsoulkitchen.modclazzchecker.manager.gen;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskErrorLang;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.type.IgnoreSolver;
import com.github.wallev.maidsoulkitchen.util.ModUtil;
import com.mojang.datafixers.util.Pair;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class TaskCompatExtractor {
    // 用于存储解析后的历史版本数据
    private static class VersionData {
        String version;
        Map<String, Map<String, List<String>>> content = new HashMap<>();
        List<String> originalContent = new ArrayList<>(); // 存储原始文件内容
    }

    public static void solver(Path rootOutputFolder) throws NoSuchFieldException {
        Map<String, Map<String, List<String>>> currentMap = new HashMap<>();

        Class<TaskInfo> enumClass = TaskInfo.class;
        TaskInfo[] tasks = TaskInfo.values();

        // 收集当前版本数据
        for (TaskInfo task : tasks) {
            Field taskField = enumClass.getField(task.name());
            if (taskField.getAnnotation(IgnoreSolver.class) != null)
                continue;

            TaskErrorLang errorLang = taskField.getAnnotation(TaskErrorLang.class);
            if (errorLang == null) {
                MaidsoulKitchen.LOGGER.warn("task {} no @TaskErrorLang annotation added, skipped", task.getUid());
                continue;
            }

            String compatibleBlock = errorLang.en_us();
            if (compatibleBlock == null) {
                MaidsoulKitchen.LOGGER.warn("task {} block name extraction failed and was skipped", task.getUid());
                continue;
            }

            String moduleType = findModuleType(taskField);
            currentMap.computeIfAbsent(moduleType, (k) -> new HashMap<>())
                    .computeIfAbsent(task.getBindMod().getModId(), (k) -> new ArrayList<>())
                    .add(compatibleBlock);
        }
        removeDuplicates(currentMap);

        String kitchenVersion = ModUtil.getAutualMaidsoulKitchenVersion();
        File file = new File(rootOutputFolder.toString().replace("generated", "main") + "\\" + "task_compat.txt");

        // 检查文件是否存在
        if (file.exists()) {
            // 读取并解析现有文件
            VersionData previousData = readExistingFile(file);
            if (previousData != null) {
                // 比较差异并生成变动说明
                String changes = generateChanges(previousData, currentMap);

                // 新内容写在前面，旧内容追加在后面
                writeNewContentBeforeOld(file, kitchenVersion, currentMap, changes, previousData.originalContent);
            } else {
                // 解析失败，创建新文件
                genTxt(file, kitchenVersion, currentMap, null);
            }
        } else {
            // 文件不存在，创建新文件
            genTxt(file, kitchenVersion, currentMap, null);
        }
    }

    private static VersionData readExistingFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            VersionData data = new VersionData();
            String line;
            String currentModule = null;
            String currentModId = null;

            // 读取所有原始内容
            while ((line = reader.readLine()) != null) {
                data.originalContent.add(line);
            }

            // 重置读取器，重新解析数据结构
            reader.close();
            BufferedReader parser = new BufferedReader(new FileReader(file));

            // 读取版本号
            if ((line = parser.readLine()) != null) {
                data.version = line.trim();
            } else {
                return null; // 空文件
            }

            boolean isVersionCompatModuleLine = false;
            int emptyLine = 0;
            while ((line = parser.readLine()) != null) {
                if (!line.contains("-"))
                    line = line.trim();
                if (line.contains("## Compat Modules")) {
                    isVersionCompatModuleLine = true;
                    continue;
                }
                if (!isVersionCompatModuleLine)
                    continue;

                if (line.isEmpty()) {
                    emptyLine++;
                } else {
                    emptyLine = 0;
                }
                if (emptyLine >= 2)
                    break;

                // 模块类型行
                if (!line.contains("-")) {
                    currentModule = line.trim();
                }
                // ModId行
                else if (line.startsWith("- ")) {
                    currentModId = line.replace("- ", "").trim();
                    data.content.computeIfAbsent(currentModule, k -> new HashMap<>())
                            .computeIfAbsent(currentModId, k -> new ArrayList<>());
                }
                // 内容行
                else if (line.startsWith("  - ") && currentModule != null && currentModId != null) {
                    String content = line.replace("  - ", "").trim();
                    data.content.get(currentModule).get(currentModId).add(content);
                }
            }
            parser.close();

            return data;
        } catch (IOException e) {
            MaidsoulKitchen.LOGGER.error("Error reading existing file: {}", e.getMessage());
            return null;
        }
    }

    private static String generateChanges(VersionData previous, Map<String, Map<String, List<String>>> current) {
        StringBuilder changes = new StringBuilder();
        changes.append("\n## Changes (").append(previous.version).append(" -> ").append(ModUtil.getAutualMaidsoulKitchenVersion()).append(")\n");
        for (Map.Entry<String, Map<String, List<String>>> entry : current.entrySet()) {
            String module = entry.getKey();

            Map<String, List<String>> currentModMap = current.get(module);
            if (currentModMap == null) {
                continue;
            }

            boolean hasChange = false;
            Map<String, List<String>> preContent = previous.content.getOrDefault(module, new HashMap<>());
            for (Map.Entry<String, List<String>> modEntry : currentModMap.entrySet()) {
                String modId = modEntry.getKey();
                List<String> currentContent = modEntry.getValue();
                List<String> previousContent = preContent.getOrDefault(modId, Collections.emptyList());

                // 新增的内容
                Set<String> added = currentContent.stream()
                        .filter(c -> !previousContent.contains(c))
                        .collect(Collectors.toSet());

                // 移除的内容
                Set<String> removed = previousContent.stream()
                        .filter(c -> !currentContent.contains(c))
                        .collect(Collectors.toSet());
                if (!added.isEmpty() || !removed.isEmpty()) {
                    changes.append(module).append("\n");
                    hasChange = true;
                    break;
                }
            }


            for (Map.Entry<String, List<String>> modEntry : currentModMap.entrySet()) {
                String modId = modEntry.getKey();
                List<String> currentContent = modEntry.getValue();
                List<String> previousContent = preContent.getOrDefault(modId, Collections.emptyList());

                // 新增的内容
                Set<String> added = currentContent.stream()
                        .filter(c -> !previousContent.contains(c))
                        .collect(Collectors.toSet());

                // 移除的内容
                Set<String> removed = previousContent.stream()
                        .filter(c -> !currentContent.contains(c))
                        .collect(Collectors.toSet());

                if (!added.isEmpty() || !removed.isEmpty()) {
                    changes.append("- ").append(modId).append("\n");

                    for (String add : added) {
                        changes.append("  + ").append(add).append("\n");
                    }

                    for (String rem : removed) {
                        changes.append("  - ").append(rem).append("\n");
                    }
                }
            }
        }

        return changes.toString();
    }

    /**
     * 将新内容写入文件前面，旧内容追加在后面
     */
    private static void writeNewContentBeforeOld(File file, String version,
                                                 Map<String, Map<String, List<String>>> map,
                                                 String changes, List<String> oldContent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // 先写入新版本内容
            writer.write(version);
            if (changes != null && !changes.isEmpty()) {
                writer.write(changes);
            }

            writer.write("\n## Compat Modules\n");
            for (Map.Entry<String, Map<String, List<String>>> entry : map.entrySet()) {
                String moduleName = entry.getKey();
                Map<String, List<String>> modCompatModules = entry.getValue();
                writer.write(moduleName + "\n");

                for (Map.Entry<String, List<String>> modCompatModule : modCompatModules.entrySet()) {
                    writer.write("- " + modCompatModule.getKey() + "\n");
                    for (String compatBlock : modCompatModule.getValue()) {
                        writer.write("  - " + compatBlock + "\n");
                    }
                }
                writer.write("\n");
            }
            writer.write("\n");


            // 再写入旧内容
            for (String line : oldContent) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            MaidsoulKitchen.LOGGER.error("Writing to file failed, error: {}", e.getMessage());
        }
    }

    private static void genTxt(File file, String version,
                               Map<String, Map<String, List<String>>> map,
                               String changes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(version);
            writer.write("\n## Compat Modules\n");

            if (changes != null && !changes.isEmpty()) {
                writer.write(changes);
                writer.write("\n");
            }

            for (Map.Entry<String, Map<String, List<String>>> entry : map.entrySet()) {
                String moduleName = entry.getKey();
                Map<String, List<String>> modCompatModules = entry.getValue();
                writer.write(moduleName + "\n");

                for (Map.Entry<String, List<String>> modCompatModule : modCompatModules.entrySet()) {
                    writer.write("- " + modCompatModule.getKey() + "\n");
                    for (String compatBlock : modCompatModule.getValue()) {
                        writer.write("  - " + compatBlock + "\n");
                    }
                }
                writer.write("\n");
            }

            writer.write("\n");
        } catch (IOException e) {
            MaidsoulKitchen.LOGGER.error("Write to file failed, error: {}", e.getMessage());
        }
    }

    private static void removeDuplicates(Map<String, Map<String, List<String>>> map) {
        HashMap<String, Map<String, List<String>>> copy = new HashMap<>(map);
        map.clear();

        for (Map.Entry<String, Map<String, List<String>>> entry : copy.entrySet()) {
            Map<String, List<String>> value = entry.getValue();
            map.computeIfAbsent(entry.getKey(), (k) -> new HashMap<>());

            for (Map.Entry<String, List<String>> listEntry : value.entrySet()) {
                String modId = listEntry.getKey();
                Pair<String, String> commonStartAndEnd = findCommonStartAndEnd(listEntry.getValue());

                Set<String> uniqueEntries = new LinkedHashSet<>();
                for (String compatBlock : listEntry.getValue()) {
                    String processed = compatBlock.replace(commonStartAndEnd.getFirst(), "")
                            .replace(commonStartAndEnd.getSecond(), "")
                            .trim();
                    uniqueEntries.add(processed);
                }

                map.get(entry.getKey())
                        .computeIfAbsent(modId, (k) -> new ArrayList<>())
                        .addAll(uniqueEntries);
            }
        }
    }

    private static Pair<String, String> findCommonStartAndEnd(List<String> list) {
        if (list == null || list.isEmpty()) {
            return Pair.of("", "");
        }
        if (list.size() == 1) {
            return Pair.of("", "");
        }

        String commonPrefix = findCommonPrefix(list);
        String commonSuffix = findCommonSuffix(list);

        return Pair.of(commonPrefix, commonSuffix);
    }

    private static String findCommonPrefix(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }

        String first = list.get(0);
        int prefixLength = first.length();

        for (String str : list) {
            prefixLength = Math.min(prefixLength, str.length());
            for (int i = 0; i < prefixLength; i++) {
                if (first.charAt(i) != str.charAt(i)) {
                    prefixLength = i;
                    break;
                }
            }
            if (prefixLength == 0) {
                break;
            }
        }

        return first.substring(0, prefixLength);
    }

    private static String findCommonSuffix(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }

        String first = list.get(0);
        int suffixStart = 0;

        int maxPossibleLength = first.length();
        for (String str : list) {
            maxPossibleLength = Math.min(maxPossibleLength, str.length());
        }

        for (int i = 1; i <= maxPossibleLength; i++) {
            char targetChar = first.charAt(first.length() - i);
            boolean allMatch = true;

            for (String str : list) {
                if (str.charAt(str.length() - i) != targetChar) {
                    allMatch = false;
                    break;
                }
            }

            if (!allMatch) {
                break;
            }
            suffixStart = first.length() - i;
        }

        return first.substring(suffixStart);
    }

    private static String findModuleType(Field taskField ) {
        for (Annotation annotation : taskField.getAnnotations()) {
            if (annotation.annotationType().toString().contains("Module")) {
                return annotation.annotationType().getSimpleName().replace("Module", "");
            }
        }
        throw new RuntimeException("task " + taskField.getName() + " no module annotation found");
    }
}
