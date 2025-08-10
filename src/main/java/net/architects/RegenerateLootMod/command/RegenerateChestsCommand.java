package net.architects.RegenerateLootMod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.architects.RegenerateLootMod.RegenerateLootMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

import java.util.Random;

public class RegenerateChestsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("chests")
                                    .then(Commands.literal("regenerate")
                                                  .executes(RegenerateChestsCommand::run))
                                    .requires((source) -> source.hasPermission(2)));
    }

    public static int run(CommandContext<CommandSourceStack> context) {
        for (int i = 0; i < RegenerateLootMod.worldChestsPositions.size(); i++) {
            Random rand = new Random(context.getSource().getLevel().getSeed() + i);
            BlockPos position = (RegenerateLootMod.worldChestsPositions.get(i));
            if (context.getSource().getLevel().getBlockEntity(position) != null) {
                BlockEntity chest = context.getSource().getLevel().getBlockEntity(position);
                assert chest != null;
                if (chest instanceof RandomizableContainerBlockEntity chestEntity) {
                    chestEntity.clearContent();
                    chestEntity.setLootTable(RegenerateLootMod.worldChestsLootTableIDs.get(i), rand.nextLong(100000000) + rand.nextLong(100000000));
                }
            }

        }

        RegenerateLootMod.worldChestsPositions.clear();
        RegenerateLootMod.worldChestsLootTableIDs.clear();
        RegenerateLootMod.chestWorlds.remove(context.getSource().getLevel().dimension());

        return 1;
    }

}
