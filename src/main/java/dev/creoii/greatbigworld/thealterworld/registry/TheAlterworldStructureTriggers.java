package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.registry.GBWRegistries;
import dev.creoii.greatbigworld.thealterworld.block.AncientPedestalBlock;
import dev.creoii.greatbigworld.thealterworld.block.entity.AncientPedestalBlockEntity;
import dev.creoii.greatbigworld.thealterworld.world.AlterworldPortal;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTrigger;
import dev.creoii.greatbigworld.thealterworld.world.AncientPortalTriggerData;
import dev.creoii.greatbigworld.world.structuretrigger.StructureTriggerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public final class TheAlterworldStructureTriggers {
    private static final int[] ANCIENT_PORTAL_OFFSETS = new int[]{-4, -10, 4, 10};
    private static final TargetingConditions PORTAL_ACTIVATION_TARGETING_CONDITIONS = TargetingConditions.forNonCombat().range(128d).ignoreLineOfSight().ignoreInvisibilityTesting();

    public static final Identifier ANCIENT_PORTAL_ACTIVATION_TRIGGER = Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "ancient_portal_activation");

    public static StructureTrigger ANCIENT_PORTAL_ACTIVATION;

    public static void register() {
        ANCIENT_PORTAL_ACTIVATION = Registry.register(GBWRegistries.STRUCTURE_TRIGGERS, ANCIENT_PORTAL_ACTIVATION_TRIGGER, new StructureTrigger(ANCIENT_PORTAL_ACTIVATION_TRIGGER, TheAlterworldStructureTriggerDataTypes.ANCIENT_PORTAL, (world, pos, state, structureStart, group) -> {
            if (group != null && group.data() instanceof AncientPortalTriggerData(BlockPos.MutableBlockPos portalPos, Map<BlockPos, Boolean> positions)) {
                boolean markDirty = false;

                Direction.Axis searchAxis = Direction.Axis.X;
                if (portalPos.equals(BlockPos.ZERO)) { // portal pos should never equal 0,0,0 since an Ancient Portal always generates at y=-162 in the mod
                    Tuple<BlockPos, Direction.Axis> pair = getPortalPosAndAxis(world, pos);

                    if (pair == null)
                        return true; // if this happens someone is going to be sad when the portal doesn't activate. but it shouldn't happen!

                    portalPos.set(pair.getA());
                    searchAxis = pair.getB();

                    for (int i : ANCIENT_PORTAL_OFFSETS) {
                        positions.put(pos.relative(searchAxis, i).below(4), false);
                    }

                    markDirty = true;
                }

                for (Map.Entry<BlockPos, Boolean> entry : positions.entrySet()) {
                    if (entry.getValue())
                        continue;

                    BlockState pedestal = world.getBlockState(entry.getKey());

                    if (pedestal.is(TheAlterworldBlocks.ANCIENT_PEDESTAL) && pedestal.getValue(AncientPedestalBlock.HAS_OFFERING)) {
                        entry.setValue(true);
                        markDirty = true;
                    }
                }

                if (positions.values().stream().allMatch(Boolean::booleanValue)) {
                    Optional<AlterworldPortal> optional = AlterworldPortal.getNewPortal(world, portalPos, searchAxis);
                    optional.ifPresent(portal -> {
                        world.playSound(null, portalPos, TheAlterworldSoundEvents.STRUCTURE_ANCIENT_CITY_PORTAL_OPEN, SoundSource.AMBIENT, 2f, .75f);

                        Player player = world.getNearestPlayer(PORTAL_ACTIVATION_TARGETING_CONDITIONS, portalPos.getX(), portalPos.getY(), portalPos.getZ());
                        if (player != null && player.isAlive()) {
                            world.gameEvent(TheAlterworldGameEvents.ANCIENT_PORTAL_ACTIVATED, portalPos, GameEvent.Context.of(player));
                        }

                        positions.keySet().forEach(pos1 -> {
                            BlockState state1 = world.getBlockState(pos1);
                            if (state1.is(TheAlterworldBlocks.ANCIENT_PEDESTAL)) {
                                BlockEntity blockEntity = world.getBlockEntity(pos1);
                                if (blockEntity instanceof AncientPedestalBlockEntity ancientPedestalBlockEntity) {
                                    ancientPedestalBlockEntity.setStack(ItemStack.EMPTY);
                                }
                                world.setBlockAndUpdate(pos1, state1.setValue(AncientPedestalBlock.LIT, true));
                                world.neighborChanged(pos1.below(), state1.getBlock(), null);
                            }
                        });

                        //PlayerLookup.tracking(world, portalPos).forEach(serverPlayerEntity -> ServerPlayNetworking.send(serverPlayerEntity, new ScreenShakeS2C(2.5f, 320, ScreenShakeManager.Easing.IN_OUT)));

                        portal.createPortal(world);
                    });
                    markDirty = true;
                    return true;
                }

                if (markDirty)
                    StructureTriggerManager.getServerState(world);
                return false;
            }
            return true;
        }));
    }

    @Nullable
    private static Tuple<BlockPos, Direction.Axis> getPortalPosAndAxis(ServerLevel world, BlockPos source) {
        for (int i : new int[]{-15, 15}) {
            BlockPos x = source.relative(Direction.Axis.X, i);
            BlockPos z = source.relative(Direction.Axis.Z, i);

            if (world.getBlockState(x.below()).is(TheAlterworldBlocks.REINFORCED_DEEPSLATE)) {
                return new Tuple<>(x, Direction.Axis.Z);
            } else if (world.getBlockState(z.below()).is(TheAlterworldBlocks.REINFORCED_DEEPSLATE)) {
                return new Tuple<>(z, Direction.Axis.X);
            }
        }

        return null;
    }
}
