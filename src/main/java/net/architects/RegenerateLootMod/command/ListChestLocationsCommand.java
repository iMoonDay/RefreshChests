package net.architects.RegenerateLootMod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.architects.RegenerateLootMod.RegenerateLootMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ListChestLocationsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("chests")
                                    .then(Commands.literal("list")
                                                  .then(Commands.literal("locations")
                                                                .executes(ListChestLocationsCommand::run))
                                                  .requires(source -> source.hasPermission(2))));
    }

    public static int run(CommandContext<CommandSourceStack> context) {
        for (int i = 0; i < RegenerateLootMod.worldChestsPositions.size(); i++) {
            final int index = i;
            context.getSource().sendSuccess(() -> Component.literal((("Chest at index " + index + ": " + "X: " + RegenerateLootMod.worldChestsPositions.get(index).getX() + " Y: " + RegenerateLootMod.worldChestsPositions.get(index).getY() + " Z: " + RegenerateLootMod.worldChestsPositions.get(index).getZ()))), false);
        }

        return 1;
    }
}
