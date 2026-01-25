package com.github.wallev.maidsoulkitchen.command.subcommand;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.asm.AsmAnnotationUtil2;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz.ASMClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz.ClassAnalyzerTool;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager.BaseClazzCheckManager;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskModClazzManager;
import com.github.wallev.maidsoulkitchen.vhelper.client.chat.VComponent;
import com.google.common.collect.Sets;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClazzAsmAnalzerCommand {
    public static final Path ROOT_FOLDER = FMLPaths.GAMEDIR.get().resolve("logs");
    private static final String ROOT_NAME = "clazz_asm_analyzer";
    private static final String WRITE_NAME = "write";
    private static final String READ_NAME = "read";

    private static final String ANALYZE_NAME = "analyze";
    private static final String ANALYZE_CLAZZ_PATH = "analyze_clazz_path";

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        LiteralArgumentBuilder<CommandSourceStack> root = LiteralArgumentBuilder.literal(ROOT_NAME);
        LiteralArgumentBuilder<CommandSourceStack> write = LiteralArgumentBuilder.literal(WRITE_NAME);
        root.then(write.executes(ClazzAsmAnalzerCommand::write));
        LiteralArgumentBuilder<CommandSourceStack> read = LiteralArgumentBuilder.literal(READ_NAME);
        root.then(read.executes(ClazzAsmAnalzerCommand::read));
        LiteralArgumentBuilder<CommandSourceStack> analyze = LiteralArgumentBuilder.literal(ANALYZE_NAME);
        root.then(analyze.then(Commands.argument(ANALYZE_CLAZZ_PATH, StringArgumentType.string())
                .executes(ClazzAsmAnalzerCommand::analyze)));
        return root;
    }

    public static int analyze(CommandContext<CommandSourceStack> context) {
        try {
            String clazzPath = StringArgumentType.getString(context, ANALYZE_CLAZZ_PATH);

            HashSet<String> targetClassNames = Sets.newHashSet(clazzPath);
            BaseClazzCheckManager<?, ?> checkManager = TaskModClazzManager.getCheckManager();

            ASMClassAnalyzer.ClazzInfoRuntime clazzInfoRuntime0 = ASMClassAnalyzer.analyze(targetClassNames, checkManager);
            AsmAnnotationUtil2.ClassAllAnnotation classAllAnnotation = AsmAnnotationUtil2.parseClassAllAnnotation(targetClassNames.iterator().next());

            Set<Class<?>> collect = targetClassNames.stream().map(clazz -> {
                try {
                    return (Class<?>) Class.forName(clazz, false, ClazzAsmAnalzerCommand.class.getClassLoader());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toSet());
            ClassAnalyzerTool.ClazzInfoRuntime clazzInfoRuntime1 = ClassAnalyzerTool.analyze(collect, checkManager);
            clazzInfoRuntime1.compare(clazzInfoRuntime0, TaskInfo.NONE);

            context.getSource().sendSuccess(() -> VComponent.literal("Successfully analyze clazz asm."), true);
            return Command.SINGLE_SUCCESS;
        } catch (Exception e) {
            context.getSource().sendFailure(VComponent.literal("Failed to analyze clazz asm."));
            e.printStackTrace();
            return 0;
        }
    }


    private static int write(CommandContext<CommandSourceStack> context) {
        try {
            TaskModClazzManager.writeModTaskClazzFile(ROOT_FOLDER);
            context.getSource().sendSuccess(() -> VComponent.literal("Successfully write clazz asm analyzer file."), true);
            return Command.SINGLE_SUCCESS;
        } catch (Exception e) {
            context.getSource().sendFailure(VComponent.literal("Failed to write clazz asm analyzer file."));
            e.printStackTrace();
            return 0;
        }
    }

    private static int read(CommandContext<CommandSourceStack> context) {
        try {
            TaskModClazzManager.init();
            context.getSource().sendSuccess(() -> VComponent.literal("Successfully read clazz asm analyzer file."), true);
            return Command.SINGLE_SUCCESS;
        } catch (Exception e) {
            context.getSource().sendFailure(VComponent.literal("Failed to read clazz asm analyzer file."));
            e.printStackTrace();
            return 0;
        }
    }


}
