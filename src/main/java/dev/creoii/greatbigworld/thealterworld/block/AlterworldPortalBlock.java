package dev.creoii.greatbigworld.thealterworld.block;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStatusEffects;
import dev.creoii.greatbigworld.thealterworld.world.AlterworldPortal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class AlterworldPortalBlock extends Block implements Portal {
    public static final MapCodec<AlterworldPortalBlock> CODEC = simpleCodec(AlterworldPortalBlock::new);
    public static final BooleanProperty FRACTURED = BooleanProperty.create("fractured");
    private static final Map<Direction.Axis, VoxelShape> SHAPES_BY_AXIS = Shapes.rotateHorizontalAxis(Block.column(4f, 16f, 0f, 16f));

    public MapCodec<AlterworldPortalBlock> codec() {
        return CODEC;
    }

    public AlterworldPortalBlock(Properties settings) {
        super(settings);
        registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.X).setValue(FRACTURED, false));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES_BY_AXIS.get(state.getValue(BlockStateProperties.HORIZONTAL_AXIS));
    }

    @Override
    protected VoxelShape getEntityInsideCollisionShape(BlockState state, BlockGetter world, BlockPos pos, Entity entity) {
        return state.getShape(world, pos);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        Direction.Axis axis = direction.getAxis();
        Direction.Axis axis2 = state.getValue(BlockStateProperties.HORIZONTAL_AXIS);
        boolean bl = axis2 != axis && axis.isHorizontal();
        return !bl && !neighborState.is(this) && !AlterworldPortal.getOnAxis(world, pos, axis2).wasAlreadyValid() ? Blocks.AIR.defaultBlockState() : super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier handler, boolean bl) {
        if (entity.canUsePortal(false)) {
            if (entity instanceof LivingEntity living && living.hasEffect(TheAlterworldStatusEffects.PLANAR_FRACTURE) && state.getValue(FRACTURED))
                entity.removeVehicle();
            entity.setAsInsidePortal(this, pos);
        }
    }

    @Override
    public int getPortalTransitionTime(ServerLevel world, Entity entity) {
        if (entity instanceof Player playerEntity) {
            return Math.max(0, world.getGameRules().get(playerEntity.getAbilities().invulnerable ? GameRules.PLAYERS_NETHER_PORTAL_CREATIVE_DELAY : GameRules.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY));
        } else return 0;
    }

    @Override
    public @Nullable TeleportTransition getPortalDestination(ServerLevel world, Entity entity, BlockPos pos) {
        ResourceKey<Level> registryKey = world.dimension() == GreatBigWorld.ALTERWORLD_KEY ? Level.OVERWORLD : GreatBigWorld.ALTERWORLD_KEY;
        ServerLevel serverWorld = world.getServer().getLevel(registryKey);
        if (serverWorld == null) {
            return null;
        } else {
            Vec3 vec3d = serverWorld.getWorldBorder().clampToBounds(entity.getX(), entity.getY(), entity.getZ()).getBottomCenter();
            BlockPos blockPos = new BlockPos((int) vec3d.x, (int) vec3d.y, (int) vec3d.z);
            if (!world.getBlockState(pos).getValue(FRACTURED)) {
                Optional<AlterworldPortal> optional2 = AlterworldPortal.getNewPortal(serverWorld, pos, Direction.Axis.X);
                optional2.ifPresent(portal -> {
                    portal.createPortal(serverWorld);
                });
            } else if (serverWorld.getBlockState(blockPos).isRedstoneConductor(serverWorld, blockPos)) {
                Vec3 vec3d1 = findOpenPosition(serverWorld, entity, blockPos);
                return new TeleportTransition(serverWorld, vec3d1, Vec3.ZERO, 0f, 0f, Relative.union(Relative.DELTA, Relative.ROTATION), TeleportTransition.PLAY_PORTAL_SOUND.then(entityx -> entityx.placePortalTicket(blockPos)));
            }
            return new TeleportTransition(serverWorld, vec3d, Vec3.ZERO, 0f, 0f, Relative.union(Relative.DELTA, Relative.ROTATION), TeleportTransition.PLAY_PORTAL_SOUND.then(entityx -> entityx.placePortalTicket(blockPos)));
        }
    }

    public static Vec3 findOpenPosition(ServerLevel world, Entity entity, BlockPos center) {
        /*EntityDimensions dimensions = entity.getDimensions(entity.getPose());
        if (dimensions.width() <= 4f && dimensions.height() <= 4f) {
            for (BlockPos pos : BlockPos.iterateOutwards(center, 32, 64, 32)) {
                Vec3d vec3d = Vec3d.ofCenter(pos);
                if (world.isSpaceEmpty(entity, dimensions.getBoxAt(vec3d))) {
                    return vec3d;
                }
            }
        }*/
        return world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, center).getBottomCenter();
    }

    @Override
    public Transition getLocalTransition() {
        return Transition.CONFUSION;
    }

    protected ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
        return ItemStack.EMPTY;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_AXIS, FRACTURED);
    }
}
