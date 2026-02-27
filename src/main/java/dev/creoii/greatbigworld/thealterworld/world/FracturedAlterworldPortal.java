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
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record FracturedAlterworldPortal(Direction direction, int foundPortalBlocks, BlockPos lowerCorner, int width, int height) {
    private static final BlockBehaviour.StatePredicate IS_VALID_FRAME_BLOCK = (state, world, pos) -> state.is(TheAlterworldBlocks.REINFORCED_DEEPSLATE) || state.is(Blocks.REINFORCED_DEEPSLATE);

    public static Optional<FracturedAlterworldPortal> getNewPortal(LevelAccessor world, BlockPos pos, Direction direction) {
        return Optional.of(getOnAxis(world, pos, direction)).filter(areaHelper -> areaHelper.isValid() && areaHelper.foundPortalBlocks == 0);
    }

    public static FracturedAlterworldPortal getOnAxis(BlockGetter world, BlockPos pos, Direction direction) {
        BlockPos blockPos = getLowerCorner(world, pos);
        if (blockPos == null) {
            return new FracturedAlterworldPortal(direction, 0, pos, 0, 0);
        } else {
            BlockPos o1 = blockPos.relative(direction);
            BlockPos o2 = blockPos.relative(direction, 2);
            boolean validWidth = validStateInsidePortal(world.getBlockState(o1)) && IS_VALID_FRAME_BLOCK.test(world.getBlockState(o2), world, o2);
            if (validWidth) {
                BlockPos h1 = blockPos.above();
                BlockPos h2 = blockPos.above(2);
                BlockPos h3 = blockPos.above(3);
                boolean validHeight = validStateInsidePortal(world.getBlockState(h1)) && validStateInsidePortal(world.getBlockState(h2)) && IS_VALID_FRAME_BLOCK.test(world.getBlockState(h3), world, h3);
                if (validHeight) {
                    return new FracturedAlterworldPortal(direction, 0, blockPos, 2, 3);
                }
            }
            return new FracturedAlterworldPortal(direction, 0, blockPos, 0, 0);
        }
    }

    @Nullable
    private static BlockPos getLowerCorner(BlockGetter world, BlockPos start) {
        BlockPos.MutableBlockPos mutable = start.mutable();
        for (int y = start.getY(); y >= Math.max(world.getMinY(), start.getY() - 2); --y) {
            mutable.setY(y);

            if (IS_VALID_FRAME_BLOCK.test(world.getBlockState(mutable.below()), world, start.below())) {
                return mutable;
            }

            if (!validStateInsidePortal(world.getBlockState(mutable))) {
                return null;
            }
        }
        return null;
    }

    private static boolean validStateInsidePortal(BlockState state) {
        return state.isAir() || state.is(BlockTags.FIRE) || state.is(TheAlterworldBlocks.ALTERWORLD_PORTAL) || state.canBeReplaced();
    }

    public boolean isValid() {
        return width == 2 && height == 3;
    }

    public void createPortal(LevelAccessor world) {
        BlockState blockState = TheAlterworldBlocks.ALTERWORLD_PORTAL.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_AXIS, direction.getAxis()).setValue(AlterworldPortalBlock.FRACTURED, true);
        BlockPos.betweenClosed(lowerCorner, lowerCorner.relative(Direction.UP, height - 1).relative(direction, width - 1)).forEach(pos -> {
            world.setBlock(pos, blockState, 18);
        });
    }
}
