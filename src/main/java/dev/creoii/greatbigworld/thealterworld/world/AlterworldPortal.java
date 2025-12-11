package dev.creoii.greatbigworld.thealterworld.world;

import dev.creoii.greatbigworld.thealterworld.block.AlterworldPortalBlock;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public record AlterworldPortal(Direction.Axis axis, int foundPortalBlocks, Direction negativeDir, BlockPos lowerCorner, int width, int height) {
    private static final BlockBehaviour.StatePredicate IS_VALID_FRAME_BLOCK = (state, world, pos) -> state.is(TheAlterworldBlocks.REINFORCED_DEEPSLATE);

    public static Optional<AlterworldPortal> getNewPortal(LevelAccessor world, BlockPos pos, Direction.Axis firstCheckedAxis) {
        return getOrEmpty(world, pos, areaHelper -> areaHelper.isValid() && areaHelper.foundPortalBlocks == 0, firstCheckedAxis);
    }

    public static Optional<AlterworldPortal> getOrEmpty(LevelAccessor world, BlockPos pos, Predicate<AlterworldPortal> validator, Direction.Axis firstCheckedAxis) {
        Optional<AlterworldPortal> optional = Optional.of(getOnAxis(world, pos, firstCheckedAxis)).filter(validator);
        if (optional.isPresent()) {
            return optional;
        } else {
            Direction.Axis axis = firstCheckedAxis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            return Optional.of(getOnAxis(world, pos, axis)).filter(validator);
        }
    }

    public static AlterworldPortal getOnAxis(BlockGetter world, BlockPos pos, Direction.Axis axis) {
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
    private static BlockPos getLowerCorner(BlockGetter world, Direction direction, BlockPos pow) {
        for (int i = Math.max(world.getMinY(), pow.getY() - 21); pow.getY() > i && validStateInsidePortal(world.getBlockState(pow.below())); pow = pow.below()) {
        }

        Direction direction2 = direction.getOpposite();
        int j = getWidth(world, pow, direction2) - 1;
        return j < 0 ? null : pow.relative(direction2, j);
    }

    private static int getValidatedWidth(BlockGetter world, BlockPos lowerCorner, Direction negativeDir) {
        int i = getWidth(world, lowerCorner, negativeDir);
        return i >= 2 && i <= 21 ? i : 0;
    }

    private static int getWidth(BlockGetter world, BlockPos lowerCorner, Direction negativeDir) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int i = 0; i <= 21; ++i) {
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

    private static int getHeight(BlockGetter world, BlockPos lowerCorner, Direction negativeDir, int width, MutableInt foundPortalBlocks) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int i = getPotentialHeight(world, lowerCorner, negativeDir, mutable, width, foundPortalBlocks);
        return i >= 3 && i <= 21 && isHorizontalFrameValid(world, lowerCorner, negativeDir, mutable, width, i) ? i : 0;
    }

    private static boolean isHorizontalFrameValid(BlockGetter world, BlockPos lowerCorner, Direction direction, BlockPos.MutableBlockPos pos, int width, int height) {
        for (int i = 0; i < width; ++i) {
            BlockPos.MutableBlockPos mutable = pos.set(lowerCorner).move(Direction.UP, height).move(direction, i);
            if (!IS_VALID_FRAME_BLOCK.test(world.getBlockState(mutable), world, mutable)) {
                return false;
            }
        }

        return true;
    }

    private static int getPotentialHeight(BlockGetter world, BlockPos lowerCorner, Direction negativeDir, BlockPos.MutableBlockPos pos, int width, MutableInt foundPortalBlocks) {
        for (int i = 0; i < 21; ++i) {
            pos.set(lowerCorner).move(Direction.UP, i).move(negativeDir, -1);
            if (!IS_VALID_FRAME_BLOCK.test(world.getBlockState(pos), world, pos)) {
                return i;
            }

            pos.set(lowerCorner).move(Direction.UP, i).move(negativeDir, width);
            if (!IS_VALID_FRAME_BLOCK.test(world.getBlockState(pos), world, pos)) {
                return i;
            }

            for (int j = 0; j < width; ++j) {
                pos.set(lowerCorner).move(Direction.UP, i).move(negativeDir, j);
                BlockState blockState = world.getBlockState(pos);
                if (!validStateInsidePortal(blockState)) {
                    return i;
                }

                if (blockState.is(TheAlterworldBlocks.ALTERWORLD_PORTAL)) {
                    foundPortalBlocks.increment();
                }
            }
        }

        return 21;
    }

    private static boolean validStateInsidePortal(BlockState state) {
        return state.isAir() || state.is(Blocks.SCULK_VEIN) || state.is(BlockTags.FIRE) || state.is(TheAlterworldBlocks.ALTERWORLD_PORTAL);
    }

    public boolean isValid() {
        return width >= 2 && width <= 21 && height >= 3 && height <= 21;
    }

    public void createPortal(LevelAccessor world) {
        BlockState blockState = TheAlterworldBlocks.ALTERWORLD_PORTAL.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_AXIS, axis).setValue(AlterworldPortalBlock.FRACTURED, false);
        BlockPos.betweenClosed(lowerCorner, lowerCorner.relative(Direction.UP, height - 1).relative(negativeDir, width - 1)).forEach(pos -> world.setBlock(pos, blockState, 18));
    }

    public boolean wasAlreadyValid() {
        return isValid() && foundPortalBlocks == width * height;
    }
}
