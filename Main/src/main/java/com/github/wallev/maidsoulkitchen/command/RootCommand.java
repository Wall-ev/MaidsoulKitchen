package com.github.wallev.maidsoulkitchen.command;

import com.github.wallev.maidsoulkitchen.command.subcommand.ClazzAsmAnalzerCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class RootCommand {
    private static final String ROOT_NAME = "maidsoulkitchen";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(ROOT_NAME)
                .requires((source -> source.hasPermission(2)));
        root.then(ClazzAsmAnalzerCommand.get());
        dispatcher.register(root);
    }


}
