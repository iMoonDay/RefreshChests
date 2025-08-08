package net.architects.RegenerateLootMod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import net.architects.RegenerateLootMod.command.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Random;

@Mod(RegenerateLootMod.MODID)
public class RegenerateLootMod {

    public static final String MODID = "regeneratelootmod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceKey<Level> key;
    public static ArrayList<ResourceKey<Level>> chestWorlds = new ArrayList<>();

    public static ArrayList<ResourceLocation> worldChestsLootTableIDs = new ArrayList<>();

    public static ArrayList<BlockPos> worldChestsPositions = new ArrayList<>();

    public RegenerateLootMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        ListChestCountCommand.register(dispatcher);
        ListChestLocationsCommand.register(dispatcher);
        RegenerateChestsCommand.register(dispatcher);
        RegenerateChestsStatusCommand.register(dispatcher);
        RemoveChestsCommand.register(dispatcher);
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.RightClickBlock event) {
        Level world = event.getLevel();
        BlockHitResult hitResult = event.getHitVec();
        RegenerateLootMod.key = world.dimension();
        BlockPos position = hitResult.getBlockPos();
        if (world.getBlockEntity(position) != null) {
            BlockEntity chest = world.getBlockEntity(position);
            assert chest != null;
            if (chest instanceof ChestBlockEntity chestEntity) {
                ResourceLocation lootTableID = (chestEntity.lootTable);
                if (lootTableID != null && !RegenerateLootMod.worldChestsPositions.contains(position)) {
                    RegenerateLootMod.worldChestsLootTableIDs.add(lootTableID);
                    RegenerateLootMod.worldChestsPositions.add(position);
                    RegenerateLootMod.chestWorlds.add(world.dimension());
                }
            }

            if (chest instanceof BarrelBlockEntity chestEntity) {
                ResourceLocation lootTableID = (chestEntity.lootTable);
                if (lootTableID != null && !RegenerateLootMod.worldChestsPositions.contains(position)) {
                    RegenerateLootMod.worldChestsLootTableIDs.add(lootTableID);
                    RegenerateLootMod.worldChestsPositions.add(position);
                    RegenerateLootMod.chestWorlds.add(world.dimension());
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(LevelEvent.Unload event) {
        LevelAccessor level = event.getLevel();
        if (!(level instanceof ServerLevel world)) {
            return;
        }

        for (int i = 0; i < RegenerateLootMod.worldChestsPositions.size(); i++) {
            if (RegenerateLootMod.chestWorlds.get(i) == world.dimension()) {
                Random rand = new Random(world.getSeed() + i);
                BlockPos position = (RegenerateLootMod.worldChestsPositions.get(i));
                if (world.getBlockEntity(position) != null) {
                    BlockEntity chest = world.getBlockEntity(position);
                    assert chest != null;
                    if (chest instanceof ChestBlockEntity chestEntity) {
                        world.setBlockAndUpdate(position, world.getBlockState(position));
                        chestEntity.setLootTable(RegenerateLootMod.worldChestsLootTableIDs.get(i), rand.nextLong(100000000) + rand.nextLong(100000000));
                    }
                    if (chest instanceof BarrelBlockEntity chestEntity) {
                        world.setBlockAndUpdate(position, world.getBlockState(position));
                        chestEntity.setLootTable(RegenerateLootMod.worldChestsLootTableIDs.get(i), rand.nextLong(100000000) + rand.nextLong(100000000));
                    }
                }
            }
        }

        RegenerateLootMod.worldChestsPositions.clear();
        RegenerateLootMod.worldChestsLootTableIDs.clear();
        RegenerateLootMod.chestWorlds.remove(world.dimension());
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        MinecraftServer server = event.getServer();

        for (int i = 0; i < RegenerateLootMod.worldChestsPositions.size(); i++) {
            Random rand = new Random(server.getLevel(RegenerateLootMod.chestWorlds.get(i)).getSeed() + i);
            BlockPos position = (RegenerateLootMod.worldChestsPositions.get(i));
            if (server.getLevel(RegenerateLootMod.chestWorlds.get(i)).getBlockEntity(position) != null) {
                BlockEntity chest = server.getLevel(RegenerateLootMod.chestWorlds.get(i)).getBlockEntity(position);
                assert chest != null;
                if (chest instanceof ChestBlockEntity chestEntity) {
                    chestEntity.setLootTable(RegenerateLootMod.worldChestsLootTableIDs.get(i), rand.nextLong(100000000) + rand.nextLong(100000000));
                }
                if (chest instanceof BarrelBlockEntity chestEntity) {
                    chestEntity.setLootTable(RegenerateLootMod.worldChestsLootTableIDs.get(i), rand.nextLong(100000000) + rand.nextLong(100000000));
                }
            }
        }

        RegenerateLootMod.worldChestsPositions.clear();
        RegenerateLootMod.worldChestsLootTableIDs.clear();
        RegenerateLootMod.chestWorlds.clear();
    }
}
