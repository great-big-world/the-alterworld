package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.client.ScreenShakeManager;
import dev.creoii.greatbigworld.registry.GBWRegistries;
import dev.creoii.greatbigworld.thealterworld.block.AncientPedestalBlock;
import dev.creoii.greatbigworld.thealterworld.block.entity.AncientPedestalBlockEntity;
import dev.creoii.greatbigworld.thealterworld.world.AlterworldPortal;
import dev.creoii.greatbigworld.util.network.ScreenShakeS2C;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTrigger;
import dev.creoii.greatbigworld.thealterworld.world.AncientPortalTriggerData;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerManager;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public final class TheAlterworldStructureTriggers {
    private static final int[] ANCIENT_PORTAL_OFFSETS = new int[]{-4, -10, 4, 10};

    public static final Identifier ANCIENT_PORTAL_ACTIVATION_TRIGGER = Identifier.of(GreatBigWorld.NAMESPACE, "ancient_portal_activation");

    public static StructureTrigger ANCIENT_PORTAL_ACTIVATION;

    public static void register() {
        ANCIENT_PORTAL_ACTIVATION = Registry.register(GBWRegistries.STRUCTURE_TRIGGERS, ANCIENT_PORTAL_ACTIVATION_TRIGGER, new StructureTrigger(ANCIENT_PORTAL_ACTIVATION_TRIGGER, TheAlterworldStructureTriggerDataTypes.ANCIENT_PORTAL, (world, pos, state, structureStart, group) -> {
            if (group != null && group.data() instanceof AncientPortalTriggerData(BlockPos.Mutable portalPos, Map<BlockPos, Boolean> positions)) {
                Direction.Axis searchAxis = Direction.Axis.X;
                if (portalPos.equals(BlockPos.ORIGIN)) { // portal pos should never equal 0,0,0 since an Ancient Portal always generates at y=-162 in the mod
                    Pair<BlockPos, Direction.Axis> pair = getPortalPosAndAxis(world, pos);

                    if (pair == null)
                        return true; // if this happens someone is going to be sad when the portal doesn't activate. but it shouldn't happen!

                    portalPos.set(pair.getLeft());
                    searchAxis = pair.getRight();

                    for (int i : ANCIENT_PORTAL_OFFSETS) {
                        positions.put(pos.offset(searchAxis, i).down(4), false);
                    }

                    StructureTriggerManager.getServerState(world).markDirty();
                }

                positions.entrySet().stream().filter(entry -> !entry.getValue()).forEach(entry -> {
                    BlockState state1 = world.getBlockState(entry.getKey());
                    if (state1.isOf(TheAlterworldBlocks.ANCIENT_PEDESTAL) && world.getBlockEntity(entry.getKey()) instanceof AncientPedestalBlockEntity pedestal && pedestal.getRelic() != null) {
                        entry.setValue(true);
                        StructureTriggerManager.getServerState(world).markDirty();
                    }
                });

                if (positions.values().stream().filter(Boolean::booleanValue).count() >= 4) {
                    Optional<AlterworldPortal> optional = AlterworldPortal.getNewPortal(world, portalPos, searchAxis);
                    optional.ifPresent(portal -> {
                        world.playSound(null, portalPos, TheAlterworldSoundEvents.STRUCTURE_ANCIENT_CITY_PORTAL_OPEN, SoundCategory.AMBIENT, 2f, .75f);

                        positions.keySet().forEach(pos1 -> {
                            BlockState state1 = world.getBlockState(pos1);
                            if (state1.isOf(TheAlterworldBlocks.ANCIENT_PEDESTAL)) {
                                BlockEntity blockEntity = world.getBlockEntity(pos1);
                                if (blockEntity instanceof AncientPedestalBlockEntity ancientPedestalBlockEntity) {
                                    ancientPedestalBlockEntity.setRelic(null);
                                }
                                world.setBlockState(pos1, state1.with(AncientPedestalBlock.LIT, true));
                                world.updateNeighbor(pos1.down(), state1.getBlock(), null);
                            }
                        });

                        PlayerLookup.tracking(world, portalPos).forEach(serverPlayerEntity -> ServerPlayNetworking.send(serverPlayerEntity, new ScreenShakeS2C(2.5f, 320, ScreenShakeManager.Easing.IN_OUT)));

                        portal.createPortal(world);
                    });
                    StructureTriggerManager.getServerState(world).markDirty();
                    return true;
                }
                return false;
            }
            return true;
        }));
    }

    @Nullable
    private static Pair<BlockPos, Direction.Axis> getPortalPosAndAxis(ServerWorld world, BlockPos source) {
        for (int i = -15; i <= 15; i += 30) { // pos offsets of -15 & 15
            BlockPos x = source.offset(Direction.Axis.X, i);
            BlockPos z = source.offset(Direction.Axis.Z, i);

            if (world.getBlockState(x.down()).isOf(TheAlterworldBlocks.REINFORCED_DEEPSLATE)) {
                return new Pair<>(x, Direction.Axis.Z);
            } else if (world.getBlockState(z.down()).isOf(TheAlterworldBlocks.REINFORCED_DEEPSLATE)) {
                return new Pair<>(z, Direction.Axis.X);
            }
        }

        return null;
    }
}
