package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.registry.GBWRegistries;
import dev.creoii.greatbigworld.thealterworld.block.AncientPedestalBlock;
import dev.creoii.greatbigworld.thealterworld.block.entity.AncientPedestalBlockEntity;
import dev.creoii.greatbigworld.thealterworld.world.AlterworldPortal;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTrigger;
import dev.creoii.greatbigworld.thealterworld.world.AncientPortalTriggerData;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public final class TheAlterworldStructureTriggers {
    public static final Identifier ANCIENT_PORTAL_ACTIVATION_TRIGGER = Identifier.of(GreatBigWorld.NAMESPACE, "ancient_portal_activation");

    public static StructureTrigger ANCIENT_PORTAL_ACTIVATION;

    public static void register() {
        ANCIENT_PORTAL_ACTIVATION = Registry.register(GBWRegistries.STRUCTURE_TRIGGERS, ANCIENT_PORTAL_ACTIVATION_TRIGGER, new StructureTrigger(ANCIENT_PORTAL_ACTIVATION_TRIGGER, TheAlterworldStructureTriggerDataTypes.ANCIENT_PORTAL, (world, pos, state, structureStart, group) -> {
            if (group != null && group.data() instanceof AncientPortalTriggerData data) {
                if (data.getPortalPos() == null) {
                    Pair<BlockPos, Direction.Axis> pair = getPortalPosAndAxis(world, pos);
                    data.setPortalPos(pair.getLeft());
                    Direction.Axis searchAxis = pair.getRight();

                    for (int i : new int[]{-4, -10, 4, 10}) {
                        data.getPositions().put(pos.offset(searchAxis, i).down(4), false);
                    }
                }

                data.getPositions().entrySet().stream().filter(entry -> !entry.getValue()).forEach(entry -> {
                    BlockState state1 = world.getBlockState(entry.getKey());
                    if (state1.isOf(TheAlterworldBlocks.ANCIENT_PEDESTAL) && world.getBlockEntity(entry.getKey()) instanceof AncientPedestalBlockEntity pedestal && !pedestal.getStack().isEmpty()) {
                        entry.setValue(true);
                    }
                });

                if (data.getPositions().values().stream().filter(aBoolean -> aBoolean).count() >= 4) {
                    Optional<AlterworldPortal> optional = AlterworldPortal.getNewPortal(world, data.getPortalPos(), Direction.Axis.X);
                    optional.ifPresent(portal -> {
                        world.playSound(null, data.getPortalPos(), TheAlterworldSoundEvents.STRUCTURE_ANCIENT_CITY_PORTAL_OPEN, SoundCategory.AMBIENT, 1.5f, .75f);

                        data.getPositions().keySet().forEach(pos1 -> {
                            BlockState state1 = world.getBlockState(pos1);
                            if (state1.isOf(TheAlterworldBlocks.ANCIENT_PEDESTAL)) {
                                BlockEntity blockEntity = world.getBlockEntity(pos1);
                                if (blockEntity instanceof AncientPedestalBlockEntity ancientPedestalBlockEntity) {
                                    ancientPedestalBlockEntity.setStack(ItemStack.EMPTY);
                                }
                                world.setBlockState(pos1, state1.with(AncientPedestalBlock.LIT, true));
                                world.updateNeighbor(pos1.down(), state1.getBlock(), null);
                            }
                        });

                        portal.createPortal(world);
                    });
                    return false;
                }
                return true;
            }
            return false;
        }));
    }

    private static Pair<BlockPos, Direction.Axis> getPortalPosAndAxis(ServerWorld world, BlockPos source) {
        BlockPos x = source.offset(Direction.Axis.X, 15);
        BlockPos z = source.offset(Direction.Axis.Z, 15);
        return world.getBlockState(x.down()).isOf(Blocks.REINFORCED_DEEPSLATE) ? new Pair<>(x, Direction.Axis.Z) : new Pair<>(z, Direction.Axis.X);
    }
}
