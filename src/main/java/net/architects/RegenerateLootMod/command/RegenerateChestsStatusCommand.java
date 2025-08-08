package net.architects.RegenerateLootMod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.architects.RegenerateLootMod.RegenerateLootMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import java.util.Random;

public class RegenerateChestsStatusCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("chests")
                                    .then(Commands.literal("regenerate")
                                                  .then(Commands.literal("status")
                                                                .executes(RegenerateChestsStatusCommand::run))
                                                  .requires((source) -> source.hasPermission(2))));
    }

    public static int run(CommandContext<CommandSourceStack> context) {
        int PriorProgress = 0;

        for (int i = 0; i < RegenerateLootMod.worldChestsPositions.size(); i++) {
            Random rand = new Random(context.getSource().getLevel().getSeed() + i);
            int CurrentProgress = 0;
            BlockPos position = (RegenerateLootMod.worldChestsPositions.get(i));
            if (context.getSource().getLevel().getBlockEntity(position) != null) {
                BlockEntity chest = context.getSource().getLevel().getBlockEntity(position);
                assert chest != null;
                if (chest instanceof ChestBlockEntity chestEntity) {
                    chestEntity.setLootTable(RegenerateLootMod.worldChestsLootTableIDs.get(i), rand.nextLong(100000000) + rand.nextLong(100000000));
                }
                if (chest instanceof BarrelBlockEntity chestEntity) {
                    chestEntity.setLootTable(RegenerateLootMod.worldChestsLootTableIDs.get(i), rand.nextLong(100000000) + rand.nextLong(100000000));
                }
            }
            CurrentProgress = ((((i + 1) * 100) / RegenerateLootMod.worldChestsPositions.size()));
            if (CurrentProgress > PriorProgress) {
                final int outputProgress = CurrentProgress;
                context.getSource().sendSuccess(() -> Component.literal(("Progress: " + outputProgress)), false);
            }

        }

        RegenerateLootMod.worldChestsPositions.clear();
        RegenerateLootMod.worldChestsLootTableIDs.clear();
        RegenerateLootMod.chestWorlds.remove(context.getSource().getLevel().dimension());

        return 1;
    }

}
