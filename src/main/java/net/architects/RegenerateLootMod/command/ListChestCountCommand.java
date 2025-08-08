package net.architects.RegenerateLootMod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.architects.RegenerateLootMod.RegenerateLootMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ListChestCountCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("chests")
                                    .then(Commands.literal("list")
                                                  .executes(ListChestCountCommand::run))
                                    .requires((source) -> source.hasPermission(2)));
    }

    public static int run(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Component.literal(("Number of Looted Chests: " + RegenerateLootMod.worldChestsPositions.size())), false);
        return 1;
    }
}
