package net.architects.RegenerateLootMod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.architects.RegenerateLootMod.RegenerateLootMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class RemoveChestsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("chests")
                                    .then(Commands.literal("remove")
                                                  .executes(RemoveChestsCommand::run)));
    }

    public static int run(CommandContext<CommandSourceStack> context) {
        if (!RegenerateLootMod.worldChestsPositions.isEmpty()) {
            double x = context.getSource().getPosition().x;
            int intX = (int) x - 1;
            double y = context.getSource().getPosition().y;
            int intY = (int) y;
            double z = context.getSource().getPosition().z;
            int intZ = (int) z;
            BlockPos position = new BlockPos(intX, intY, intZ);
            int index = RegenerateLootMod.worldChestsPositions.indexOf(position);
            context.getSource().sendSuccess(() -> Component.literal("Chest index " + index + " deleted"), false);
            RegenerateLootMod.worldChestsPositions.remove(index);
            RegenerateLootMod.worldChestsLootTableIDs.remove(index);
            RegenerateLootMod.chestWorlds.remove(index);
        }

        return 1;
    }

}
