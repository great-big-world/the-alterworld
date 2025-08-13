package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.block.AncientPedestalBlock;
import dev.creoii.greatbigworld.thealterworld.block.entity.AncientPedestalBlockEntity;
import dev.creoii.greatbigworld.thealterworld.world.AlterworldPortal;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTrigger;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerGroup;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.List;
import java.util.Optional;

public final class TheAlterworldStructureTriggers {
    public static StructureTrigger ANCIENT_PORTAL_LIGHTER;
    public static StructureTrigger ANCIENT_PORTAL_ACTIVATION;

    public static void register() {
        ANCIENT_PORTAL_LIGHTER = Registry.register(GreatBigWorld.STRUCTURE_TRIGGERS, Identifier.of(GreatBigWorld.NAMESPACE, "ancient_portal_lighter"), new StructureTrigger(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_portal_lighter"), StructureTriggerGroup.DataType.LIST, (world, pos, state, structureStart, group) -> {
            if (group != null && group.getDataType() == StructureTriggerGroup.DataType.LIST) {
                @SuppressWarnings("unchecked")
                MutableObject<List<BlockPos>> mutableObject = (MutableObject<List<BlockPos>>) group.getData();
                BlockState state1 = world.getBlockState(pos);
                if (state1.isOf(TheAlterworldBlocks.ANCIENT_PEDESTAL)) {
                    BlockEntity blockEntity = world.getBlockEntity(pos);
                    if (blockEntity instanceof AncientPedestalBlockEntity ancientPedestalBlockEntity) {
                        if (!ancientPedestalBlockEntity.getStack().isEmpty() && !mutableObject.getValue().contains(pos)) {
                            mutableObject.getValue().add(pos);
                        } else if (ancientPedestalBlockEntity.getStack().isEmpty() && mutableObject.getValue().contains(pos)) {
                            mutableObject.getValue().remove(pos);
                        }
                        return mutableObject.getValue().size() < 4;
                    }
                }
            }
            return false;
        }));
        ANCIENT_PORTAL_ACTIVATION = Registry.register(GreatBigWorld.STRUCTURE_TRIGGERS, Identifier.of(GreatBigWorld.NAMESPACE, "ancient_portal_activation"), new StructureTrigger(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_portal_lighter"), StructureTriggerGroup.DataType.LIST, (world, pos, state, structureStart, group) -> {
            if (group != null && group.getDataType() == StructureTriggerGroup.DataType.LIST) {
                @SuppressWarnings("unchecked")
                MutableObject<List<BlockPos>> mutableObject = (MutableObject<List<BlockPos>>) group.getData();
                if (mutableObject.getValue().size() >= 4) {
                    Optional<AlterworldPortal> optional = AlterworldPortal.getNewPortal(world, pos, Direction.Axis.X);
                    optional.ifPresent(portal -> {
                        world.playSound(null, pos, TheAlterworldSoundEvents.STRUCTURE_ANCIENT_CITY_PORTAL_OPEN, SoundCategory.AMBIENT, 1.5f, .75f);

                        mutableObject.getValue().forEach(pos1 -> {
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

                        if (!world.isClient) {
                            PlayerLookup.tracking(world, pos).forEach(serverPlayerEntity -> {
                                TheAlterworldCriteria.LIGHT_ANCIENT_PORTAL.trigger(serverPlayerEntity);
                            });
                        }
                    });
                    return false;
                }
                return true;
            }
            return false;
        }));
    }
}
