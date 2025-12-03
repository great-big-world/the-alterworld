package dev.creoii.greatbigworld.thealterworld.world;

import dev.creoii.greatbigworld.thealterworld.block.AlterworldPortalBlock;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.*;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public record AlterworldPortal(Direction.Axis axis, int foundPortalBlocks, Direction negativeDir, BlockPos lowerCorner, int width, int height) {
    private static final AbstractBlock.ContextPredicate IS_VALID_FRAME_BLOCK = (state, world, pos) -> state.isOf(TheAlterworldBlocks.REINFORCED_DEEPSLATE);

    public static Optional<AlterworldPortal> getNewPortal(WorldAccess world, BlockPos pos, Direction.Axis firstCheckedAxis) {
        return getOrEmpty(world, pos, areaHelper -> areaHelper.isValid() && areaHelper.foundPortalBlocks == 0, firstCheckedAxis);
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
        for (int i = Math.max(world.getBottomY(), pow.getY() - 21); pow.getY() > i && validStateInsidePortal(world.getBlockState(pow.down())); pow = pow.down()) {
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

    private static int getHeight(BlockView world, BlockPos lowerCorner, Direction negativeDir, int width, MutableInt foundPortalBlocks) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int i = getPotentialHeight(world, lowerCorner, negativeDir, mutable, width, foundPortalBlocks);
        return i >= 3 && i <= 21 && isHorizontalFrameValid(world, lowerCorner, negativeDir, mutable, width, i) ? i : 0;
    }

    private static boolean isHorizontalFrameValid(BlockView world, BlockPos lowerCorner, Direction direction, BlockPos.Mutable pos, int width, int height) {
        for (int i = 0; i < width; ++i) {
            BlockPos.Mutable mutable = pos.set(lowerCorner).move(Direction.UP, height).move(direction, i);
            if (!IS_VALID_FRAME_BLOCK.test(world.getBlockState(mutable), world, mutable)) {
                return false;
            }
        }

        return true;
    }

    private static int getPotentialHeight(BlockView world, BlockPos lowerCorner, Direction negativeDir, BlockPos.Mutable pos, int width, MutableInt foundPortalBlocks) {
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

                if (blockState.isOf(TheAlterworldBlocks.ALTERWORLD_PORTAL)) {
                    foundPortalBlocks.increment();
                }
            }
        }

        return 21;
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
}
