package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.world.AlterworldPortal;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTrigger;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerGroup;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registry;
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
                BlockState state2 = world.getBlockState(pos.up());
                if (state1.isOf(Blocks.SOUL_SAND)) {
                    if (state2.isOf(Blocks.SOUL_FIRE) && !mutableObject.getValue().contains(pos)) {
                        System.out.println("add pos " + pos.toShortString());
                        mutableObject.getValue().add(pos);
                    } else if (!state2.isOf(Blocks.SOUL_FIRE) && mutableObject.getValue().contains(pos)) {
                        System.out.println("remove pos " + pos.toShortString());
                        mutableObject.getValue().remove(pos);
                    }
                    return mutableObject.getValue().size() < 4;
                }
            }
            return false;
        }));
        ANCIENT_PORTAL_ACTIVATION = Registry.register(GreatBigWorld.STRUCTURE_TRIGGERS, Identifier.of(GreatBigWorld.NAMESPACE, "ancient_portal_activation"), new StructureTrigger(Identifier.of(GreatBigWorld.NAMESPACE, "ancient_portal_lighter"), StructureTriggerGroup.DataType.LIST, (world, pos, state, structureStart, group) -> {
            System.out.println("activation");
            if (group != null && group.getDataType() == StructureTriggerGroup.DataType.LIST) {
                @SuppressWarnings("unchecked")
                MutableObject<List<BlockPos>> mutableObject = (MutableObject<List<BlockPos>>) group.getData();
                System.out.println("size: " + mutableObject.getValue().size());
                if (mutableObject.getValue().size() >= 4) {
                    world.breakBlock(pos, false);
                    Optional<AlterworldPortal> optional = AlterworldPortal.getNewPortal(world, pos, Direction.Axis.X);
                    optional.ifPresent(portal -> {
                        portal.createPortal(world);
                    });
                    System.out.println("activate");
                    return true;
                }
            }
            return false;
        }));
    }
}
