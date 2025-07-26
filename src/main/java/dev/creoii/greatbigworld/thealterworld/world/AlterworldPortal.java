package dev.creoii.greatbigworld.thealterworld.world;

import dev.creoii.greatbigworld.thealterworld.TheAlterworld;
import dev.creoii.greatbigworld.thealterworld.block.AlterworldPortalBlock;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.BlockView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.poi.PointOfInterestStorage;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class AlterworldPortal {
    private static final AbstractBlock.ContextPredicate IS_VALID_FRAME_BLOCK = (state, world, pos) -> state.isOf(Blocks.REINFORCED_DEEPSLATE);
    private final Direction.Axis axis;
    private final Direction negativeDir;
    private final int foundPortalBlocks;
    private final BlockPos lowerCorner;
    private final int height;
    private final int width;

    private AlterworldPortal(Direction.Axis axis, int foundPortalBlocks, Direction negativeDir, BlockPos lowerCorner, int width, int height) {
        this.axis = axis;
        this.foundPortalBlocks = foundPortalBlocks;
        this.negativeDir = negativeDir;
        this.lowerCorner = lowerCorner;
        this.width = width;
        this.height = height;
    }

    public static Optional<AlterworldPortal> getNewPortal(WorldAccess world, BlockPos pos, Direction.Axis firstCheckedAxis) {
        return getOrEmpty(world, pos, (areaHelper) -> areaHelper.isValid() && areaHelper.foundPortalBlocks == 0, firstCheckedAxis);
    }

    public static Optional<AlterworldPortal> getOrEmpty(WorldAccess world, BlockPos pos, Predicate<AlterworldPortal> validator, Direction.Axis firstCheckedAxis) {
        Optional<AlterworldPortal> optional = Optional.of(getOnAxis(world, pos, firstCheckedAxis)).filter(validator);
        if (optional.isPresent()) {
            return optional;
        } else {
            Direction.Axis axis = firstCheckedAxis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            return Optional.of(getOnAxis(world, pos, axis)).filter(validator);
        }
    }

    public static AlterworldPortal getOnAxis(BlockView world, BlockPos pos, Direction.Axis axis) {
        Direction direction = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        BlockPos blockPos = getLowerCorner(world, direction, pos);
        if (blockPos == null) {
            return new AlterworldPortal(axis, 0, direction, pos, 0, 0);
        } else {
            int i = getValidatedWidth(world, blockPos, direction);
            if (i == 0) {
                return new AlterworldPortal(axis, 0, direction, blockPos, 0, 0);
            } else {
                MutableInt mutableInt = new MutableInt();
                int j = getHeight(world, blockPos, direction, i, mutableInt);
                return new AlterworldPortal(axis, mutableInt.getValue(), direction, blockPos, i, j);
            }
        }
    }

    @Nullable
    private static BlockPos getLowerCorner(BlockView world, Direction direction, BlockPos pow) {
        for(int i = Math.max(world.getBottomY(), pow.getY() - 21); pow.getY() > i && validStateInsidePortal(world.getBlockState(pow.down())); pow = pow.down()) {
        }

        Direction direction2 = direction.getOpposite();
        int j = getWidth(world, pow, direction2) - 1;
        return j < 0 ? null : pow.offset(direction2, j);
    }

    private static int getValidatedWidth(BlockView world, BlockPos lowerCorner, Direction negativeDir) {
        int i = getWidth(world, lowerCorner, negativeDir);
        return i >= 2 && i <= 21 ? i : 0;
    }

    private static int getWidth(BlockView world, BlockPos lowerCorner, Direction negativeDir) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for(int i = 0; i <= 21; ++i) {
            mutable.set(lowerCorner).move(negativeDir, i);
            BlockState blockState = world.getBlockState(mutable);
            if (!validStateInsidePortal(blockState)) {
                if (IS_VALID_FRAME_BLOCK.test(blockState, world, mutable)) {
                    return i;
                }
                break;
            }

            BlockState blockState2 = world.getBlockState(mutable.move(Direction.DOWN));
            if (!IS_VALID_FRAME_BLOCK.test(blockState2, world, mutable)) {
                break;
            }
        }

        return 0;
    }

    private static int getHeight(BlockView world, BlockPos lowerCorner, Direction negativeDir, int width, MutableInt foundPortalBlocks) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int i = getPotentialHeight(world, lowerCorner, negativeDir, mutable, width, foundPortalBlocks);
        return i >= 3 && i <= 21 && isHorizontalFrameValid(world, lowerCorner, negativeDir, mutable, width, i) ? i : 0;
    }

    private static boolean isHorizontalFrameValid(BlockView world, BlockPos lowerCorner, Direction direction, BlockPos.Mutable pos, int width, int height) {
        for(int i = 0; i < width; ++i) {
            BlockPos.Mutable mutable = pos.set(lowerCorner).move(Direction.UP, height).move(direction, i);
            if (!IS_VALID_FRAME_BLOCK.test(world.getBlockState(mutable), world, mutable)) {
                return false;
            }
        }

        return true;
    }

    private static int getPotentialHeight(BlockView world, BlockPos lowerCorner, Direction negativeDir, BlockPos.Mutable pos, int width, MutableInt foundPortalBlocks) {
        for(int i = 0; i < 21; ++i) {
            pos.set(lowerCorner).move(Direction.UP, i).move(negativeDir, -1);
            if (!IS_VALID_FRAME_BLOCK.test(world.getBlockState(pos), world, pos)) {
                return i;
            }

            pos.set(lowerCorner).move(Direction.UP, i).move(negativeDir, width);
            if (!IS_VALID_FRAME_BLOCK.test(world.getBlockState(pos), world, pos)) {
                return i;
            }

            for(int j = 0; j < width; ++j) {
                pos.set(lowerCorner).move(Direction.UP, i).move(negativeDir, j);
                BlockState blockState = world.getBlockState(pos);
                if (!validStateInsidePortal(blockState)) {
                    return i;
                }

                if (blockState.isOf(TheAlterworldBlocks.ALTERWORLD_PORTAL)) {
                    foundPortalBlocks.increment();
                }
            }
        }

        return 21;
    }

    public static Optional<BlockPos> getPortalPos(ServerWorld world, BlockPos pos, WorldBorder worldBorder) {
        PointOfInterestStorage pointOfInterestStorage = world.getPointOfInterestStorage();
        pointOfInterestStorage.preloadChunks(world, pos, 16);
        return pointOfInterestStorage.getPosition(poiType -> poiType.matchesKey(TheAlterworld.ALTERWORLD_PORTAL_POI_KEY), pos1 -> true, PointOfInterestStorage.OccupationStatus.ANY, pos, 16, world.random);
    }

    public static Optional<BlockLocating.Rectangle> createPortal(ServerWorld world, BlockPos pos, Direction.Axis axis) {
        Direction direction = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        double d = -1f;
        BlockPos blockPos = null;
        double e = -1f;
        BlockPos blockPos2 = null;
        WorldBorder worldBorder = world.getWorldBorder();
        int i = Math.min(world.getTopYInclusive(), world.getBottomY() + world.getLogicalHeight() - 1);
        BlockPos.Mutable mutable = pos.mutableCopy();

        for(BlockPos.Mutable mutable2 : BlockPos.iterateInSquare(pos, 16, Direction.EAST, Direction.SOUTH)) {
            int k = Math.min(i, world.getTopY(Heightmap.Type.MOTION_BLOCKING, mutable2.getX(), mutable2.getZ()));
            if (worldBorder.contains(mutable2) && worldBorder.contains(mutable2.move(direction, 1))) {
                mutable2.move(direction.getOpposite(), 1);

                for(int l = k; l >= world.getBottomY(); --l) {
                    mutable2.setY(l);
                    if (isBlockStateValid(world, mutable2)) {
                        int m;
                        for(m = l; l > world.getBottomY() && isBlockStateValid(world, mutable2.move(Direction.DOWN)); --l) {
                        }

                        if (l + 4 <= i) {
                            int n = m - l;
                            if (n <= 0 || n >= 3) {
                                mutable2.setY(l);
                                if (isValidPortalPos(world, mutable2, mutable, direction, 0)) {
                                    double f = pos.getSquaredDistance(mutable2);
                                    if (isValidPortalPos(world, mutable2, mutable, direction, -1) && isValidPortalPos(world, mutable2, mutable, direction, 1) && (d == (double)-1.0F || d > f)) {
                                        d = f;
                                        blockPos = mutable2.toImmutable();
                                    }

                                    if (d == (double)-1f && (e == (double)-1f || e > f)) {
                                        e = f;
                                        blockPos2 = mutable2.toImmutable();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (d == (double)-1f && e != (double)-1f) {
            blockPos = blockPos2;
            d = e;
        }

        if (d == (double)-1f) {
            int o = Math.max(world.getBottomY() + 1, 70);
            int p = i - 9;
            if (p < o) {
                return Optional.empty();
            }

            blockPos = (new BlockPos(pos.getX() - direction.getOffsetX(), MathHelper.clamp(pos.getY(), o, p), pos.getZ() - direction.getOffsetZ() * 1)).toImmutable();
            blockPos = worldBorder.clampFloored(blockPos);
            Direction direction2 = direction.rotateYClockwise();

            for(int l = -1; l < 2; ++l) {
                for(int m = 0; m < 2; ++m) {
                    for(int n = -1; n < 3; ++n) {
                        BlockState blockState = n < 0 ? Blocks.REINFORCED_DEEPSLATE.getDefaultState() : Blocks.AIR.getDefaultState();
                        mutable.set(blockPos, m * direction.getOffsetX() + l * direction2.getOffsetX(), n, m * direction.getOffsetZ() + l * direction2.getOffsetZ());
                        world.setBlockState(mutable, blockState);
                    }
                }
            }
        }

        for(int o = -1; o < 21; ++o) {
            for(int p = -1; p < 7; ++p) {
                if (o == -1 || o == 20 || p == -1 || p == 6) {
                    mutable.set(blockPos, o * direction.getOffsetX(), p, o * direction.getOffsetZ());
                    world.setBlockState(mutable, Blocks.REINFORCED_DEEPSLATE.getDefaultState(), 3);
                }
            }
        }

        BlockState blockState2 = TheAlterworldBlocks.ALTERWORLD_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, axis).with(AlterworldPortalBlock.FRACTURED, false);

        for(int p = 0; p < 20; ++p) {
            for(int k = 0; k < 6; ++k) {
                mutable.set(blockPos, p * direction.getOffsetX(), k, p * direction.getOffsetZ());
                world.setBlockState(mutable, blockState2, 18);
            }
        }

        return Optional.of(new BlockLocating.Rectangle(blockPos.toImmutable(), 20, 6));
    }

    private static boolean validStateInsidePortal(BlockState state) {
        return state.isAir() || state.isOf(Blocks.SCULK_VEIN) || state.isIn(BlockTags.FIRE) || state.isOf(TheAlterworldBlocks.ALTERWORLD_PORTAL);
    }

    public boolean isValid() {
        return width >= 2 && width <= 21 && height >= 3 && height <= 21;
    }

    public void createPortal(WorldAccess world) {
        BlockState blockState = TheAlterworldBlocks.ALTERWORLD_PORTAL.getDefaultState().with(Properties.HORIZONTAL_AXIS, axis).with(AlterworldPortalBlock.FRACTURED, false);
        BlockPos.iterate(lowerCorner, lowerCorner.offset(Direction.UP, height - 1).offset(negativeDir, width - 1)).forEach(pos -> world.setBlockState(pos, blockState, 18));
    }

    public boolean wasAlreadyValid() {
        return isValid() && foundPortalBlocks == width * height;
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

    private static boolean isBlockStateValid(ServerWorld world, BlockPos.Mutable pos) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.isReplaceable() && blockState.getFluidState().isEmpty();
    }

    private static boolean isValidPortalPos(ServerWorld world, BlockPos pos, BlockPos.Mutable temp, Direction portalDirection, int distanceOrthogonalToPortal) {
        Direction direction = portalDirection.rotateYClockwise();

        for(int i = -1; i < 20; ++i) {
            for(int j = -1; j < 6; ++j) {
                temp.set(pos, portalDirection.getOffsetX() * i + direction.getOffsetX() * distanceOrthogonalToPortal, j, portalDirection.getOffsetZ() * i + direction.getOffsetZ() * distanceOrthogonalToPortal);
                if (j < 0 && !world.getBlockState(temp).isSolid()) {
                    return false;
                }

                if (j >= 0 && !isBlockStateValid(world, temp)) {
                    return false;
                }
            }
        }

        return true;
    }
}
