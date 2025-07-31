package dev.creoii.greatbigworld.thealterworld.block;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStatusEffects;
import dev.creoii.greatbigworld.thealterworld.world.AlterworldPortal;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.*;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class AlterworldPortalBlock extends Block implements Portal {
    public static final MapCodec<AlterworldPortalBlock> CODEC = createCodec(AlterworldPortalBlock::new);
    public static final BooleanProperty FRACTURED = BooleanProperty.of("fractured");
    private static final Map<Direction.Axis, VoxelShape> SHAPES_BY_AXIS = VoxelShapes.createHorizontalAxisShapeMap(Block.createColumnShape(4f, 16f, 0f, 16f));

    public MapCodec<AlterworldPortalBlock> getCodec() {
        return CODEC;
    }

    public AlterworldPortalBlock(Settings settings) {
        super(settings);
        setDefaultState(stateManager.getDefaultState().with(Properties.HORIZONTAL_AXIS, Direction.Axis.X).with(FRACTURED, false));
    }

    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES_BY_AXIS.get(state.get(Properties.HORIZONTAL_AXIS));
    }

    protected VoxelShape getInsideCollisionShape(BlockState state, BlockView world, BlockPos pos, Entity entity) {
        return state.getOutlineShape(world, pos);
    }

    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        Direction.Axis axis = direction.getAxis();
        Direction.Axis axis2 = state.get(Properties.HORIZONTAL_AXIS);
        boolean bl = axis2 != axis && axis.isHorizontal();
        return !bl && !neighborState.isOf(this) && !AlterworldPortal.getOnAxis(world, pos, axis2).wasAlreadyValid() ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
        if (entity.canUsePortals(false)) {
            if (entity instanceof LivingEntity living && living.hasStatusEffect(TheAlterworldStatusEffects.PLANAR_FRACTURE) && state.get(FRACTURED))
                entity.dismountVehicle();
            entity.tryUsePortal(this, pos);
        }
    }

    @Override
    public int getPortalDelay(ServerWorld world, Entity entity) {
        if (entity instanceof PlayerEntity playerEntity) {
            return Math.max(0, world.getGameRules().getInt(playerEntity.getAbilities().invulnerable ? GameRules.PLAYERS_NETHER_PORTAL_CREATIVE_DELAY : GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY));
        } else {
            return 0;
        }
    }

    @Override
    public @Nullable TeleportTarget createTeleportTarget(ServerWorld world, Entity entity, BlockPos pos) {
        RegistryKey<World> registryKey = world.getRegistryKey() == GreatBigWorld.ALTERWORLD_KEY ? World.OVERWORLD : GreatBigWorld.ALTERWORLD_KEY;
        ServerWorld serverWorld = world.getServer().getWorld(registryKey);
        if (serverWorld == null) {
            return null;
        } else {
            WorldBorder worldBorder = serverWorld.getWorldBorder();
            BlockPos blockPos = worldBorder.clampFloored(entity.getX(), entity.getY(), entity.getZ());
            Vec3d vec3d = findOpenPosition(blockPos.toBottomCenterPos(), serverWorld, entity, entity.getDimensions(entity.getPose()));
            BlockPos foundPos = new BlockPos((int) vec3d.x, (int) vec3d.y, (int) vec3d.z);
            if (!world.getBlockState(pos).get(FRACTURED)) {
                Optional<AlterworldPortal> optional2 = AlterworldPortal.getNewPortal(serverWorld, pos, Direction.Axis.X);
                optional2.ifPresent(portal -> {
                    portal.createPortal(serverWorld);
                });
            } else if (serverWorld.getBlockState(foundPos).isSolidBlock(serverWorld, foundPos)) {
                EntityDimensions dimensions = entity.getDimensions(entity.getPose());
                vec3d = BlockPos.findClosest(foundPos, 16, 64, blockPos1 -> {
                    Box box = dimensions.getBoxAt(Vec3d.ofBottomCenter(blockPos1));
                    return serverWorld.isSpaceEmpty(entity, box, false);
                }).orElse(serverWorld.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, foundPos)).toBottomCenterPos();
            }
            return new TeleportTarget(serverWorld, vec3d, Vec3d.ZERO, 0f, 0f, PositionFlag.combine(PositionFlag.DELTA, PositionFlag.ROT), TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET.then(entityx -> entityx.addPortalChunkTicketAt(blockPos)));
        }
    }

    public static Vec3d findOpenPosition(Vec3d fallback, ServerWorld world, Entity entity, EntityDimensions dimensions) {
        if (!(dimensions.width() > 4f) && !(dimensions.height() > 4f)) {
            double d = (double)dimensions.height() / (double)2f;
            Vec3d vec3d = fallback.add(0f, d, 0f);
            VoxelShape voxelShape = VoxelShapes.cuboid(Box.of(vec3d, dimensions.width(), 0f, dimensions.width()).stretch(0f, 1f, 0f).expand(1e-6));
            Optional<Vec3d> optional = world.findClosestCollision(entity, voxelShape, vec3d, dimensions.width(), dimensions.height(), dimensions.width());
            Optional<Vec3d> optional2 = optional.map((pos) -> pos.subtract(0f, d, 0f));
            return optional2.orElse(fallback);
        } else {
            return fallback;
        }
    }

    @Override
    public Effect getPortalEffect() {
        return Effect.CONFUSION;
    }

    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        return ItemStack.EMPTY;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_AXIS, FRACTURED);
    }
}
