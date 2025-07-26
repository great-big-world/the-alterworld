package dev.creoii.greatbigworld.thealterworld.block;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.thealterworld.TheAlterworld;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStatusEffects;
import dev.creoii.greatbigworld.thealterworld.world.AlterworldPortal;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
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
        RegistryKey<World> registryKey = world.getRegistryKey() == TheAlterworld.ALTERWORLD_KEY ? World.OVERWORLD : TheAlterworld.ALTERWORLD_KEY;
        ServerWorld serverWorld = world.getServer().getWorld(registryKey);
        if (serverWorld == null) {
            return null;
        } else {
            WorldBorder worldBorder = serverWorld.getWorldBorder();
            BlockPos blockPos = worldBorder.clampFloored(entity.getX(), entity.getY(), entity.getZ());
            if (world.getBlockState(pos).get(FRACTURED)) {
                Vec3d vec3d = AlterworldPortal.findOpenPosition(blockPos.toBottomCenterPos(), world, entity, entity.getDimensions(entity.getPose()));
                return new TeleportTarget(serverWorld, vec3d, Vec3d.ZERO, 0f, 0f, PositionFlag.combine(PositionFlag.DELTA, PositionFlag.ROT), TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET.then(entityx -> entityx.addPortalChunkTicketAt(blockPos)));
            }
            return getOrCreateExitPortalTarget(serverWorld, entity, pos, blockPos, worldBorder);
        }
    }

    @Nullable
    public static TeleportTarget getOrCreateExitPortalTarget(ServerWorld world, Entity entity, BlockPos pos, BlockPos scaledPos, WorldBorder worldBorder) {
        BlockState state = world.getBlockState(pos);
        if (state.isOf(TheAlterworldBlocks.ALTERWORLD_PORTAL) && state.get(FRACTURED))
            return null;

        Optional<BlockPos> optional = AlterworldPortal.getPortalPos(world, scaledPos, worldBorder);
        BlockLocating.Rectangle rectangle;
        TeleportTarget.PostDimensionTransition postDimensionTransition;
        if (optional.isPresent()) {
            BlockPos blockPos = optional.get();
            BlockState blockState = world.getBlockState(blockPos);
            rectangle = BlockLocating.getLargestRectangle(blockPos, blockState.get(Properties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, posx -> world.getBlockState(posx) == blockState);
            postDimensionTransition = TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET.then(entityx -> entityx.addPortalChunkTicketAt(blockPos));
        } else {
            Direction.Axis axis = entity.getWorld().getBlockState(pos).getOrEmpty(Properties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
            Optional<BlockLocating.Rectangle> optional2 = AlterworldPortal.createPortal(world, scaledPos, axis);
            if (optional2.isEmpty()) {
                return null;
            }

            rectangle = optional2.get();
            postDimensionTransition = TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET.then(TeleportTarget.ADD_PORTAL_CHUNK_TICKET);
        }

        return getExitPortalTarget(entity, pos, rectangle, world, postDimensionTransition);
    }

    private static TeleportTarget getExitPortalTarget(Entity entity, BlockPos pos, BlockLocating.Rectangle exitPortalRectangle, ServerWorld world, TeleportTarget.PostDimensionTransition postDimensionTransition) {
        BlockState blockState = entity.getWorld().getBlockState(pos);
        Direction.Axis axis;
        Vec3d vec3d;
        if (blockState.contains(Properties.HORIZONTAL_AXIS)) {
            axis = blockState.get(Properties.HORIZONTAL_AXIS);
            BlockLocating.Rectangle rectangle = BlockLocating.getLargestRectangle(pos, axis, 21, Direction.Axis.Y, 21, (posx) -> entity.getWorld().getBlockState(posx) == blockState);
            vec3d = entity.positionInPortal(axis, rectangle);
        } else {
            axis = Direction.Axis.X;
            vec3d = new Vec3d(.5f, 0f, 0f);
        }

        return getExitPortalTarget(world, exitPortalRectangle, axis, vec3d, entity, postDimensionTransition);
    }

    private static TeleportTarget getExitPortalTarget(ServerWorld world, BlockLocating.Rectangle exitPortalRectangle, Direction.Axis axis, Vec3d positionInPortal, Entity entity, TeleportTarget.PostDimensionTransition postDimensionTransition) {
        BlockPos blockPos = exitPortalRectangle.lowerLeft;
        BlockState blockState = world.getBlockState(blockPos);
        Direction.Axis axis2 = blockState.getOrEmpty(Properties.HORIZONTAL_AXIS).orElse(Direction.Axis.X);
        double d = exitPortalRectangle.width;
        double e = exitPortalRectangle.height;
        EntityDimensions entityDimensions = entity.getDimensions(entity.getPose());
        int i = axis == axis2 ? 0 : 90;
        double f = (double)entityDimensions.width() / (double)2f + (d - (double)entityDimensions.width()) * positionInPortal.getX();
        double g = (e - (double)entityDimensions.height()) * positionInPortal.getY();
        double h = (double).5f + positionInPortal.getZ();
        boolean bl = axis2 == Direction.Axis.X;
        Vec3d vec3d = new Vec3d((double)blockPos.getX() + (bl ? f : h), (double)blockPos.getY() + g, (double)blockPos.getZ() + (bl ? h : f));
        Vec3d vec3d2 = AlterworldPortal.findOpenPosition(vec3d, world, entity, entityDimensions);

        if (blockState.get(FRACTURED) && entity instanceof LivingEntity living) {
            if (entity.getWorld().getRegistryKey() == TheAlterworld.ALTERWORLD_KEY)
                living.addStatusEffect(new StatusEffectInstance(TheAlterworldStatusEffects.FRACTURED_REALM, 3000));
            else living.removeStatusEffect(TheAlterworldStatusEffects.FRACTURED_REALM);
        }

        return new TeleportTarget(world, vec3d2, Vec3d.ZERO, (float)i, 0f, PositionFlag.combine(PositionFlag.DELTA, PositionFlag.ROT), postDimensionTransition);
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
