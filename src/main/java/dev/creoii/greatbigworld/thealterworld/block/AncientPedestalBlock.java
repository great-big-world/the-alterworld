package dev.creoii.greatbigworld.thealterworld.block;

import dev.creoii.greatbigworld.thealterworld.block.entity.AncientPedestalBlockEntity;
import dev.creoii.greatbigworld.thealterworld.item.RelicItem;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AncientPedestalBlock extends Block implements BlockEntityProvider {
    public static final BooleanProperty LIT = Properties.LIT;
    public static final VoxelShape TOP_SHAPE = Block.createCuboidShape(4f, 6f, 4f, 12f, 12f, 12f);
    public static final VoxelShape BASE_SHAPE = Block.createCuboidShape(0f, 0f, 0f, 16f, 6f, 16f);
    public static final VoxelShape SHAPE = VoxelShapes.union(BASE_SHAPE, TOP_SHAPE);

    public AncientPedestalBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(LIT, false));
    }

    protected boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AncientPedestalBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof RelicItem relicItem && !state.get(LIT)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AncientPedestalBlockEntity ancientPedestalBlockEntity && ancientPedestalBlockEntity.getStack().isEmpty()) {
                ancientPedestalBlockEntity.setStack(stack);
                stack.decrementUnlessCreative(1, player);
                world.playSound(player, pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d, TheAlterworldSoundEvents.BLOCK_ANCIENT_PEDESTAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                return ActionResult.SUCCESS;
            }
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    protected boolean emitsRedstonePower(BlockState state) {
        return state.get(LIT);
    }

    @Override
    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (state.get(LIT) && direction == Direction.DOWN) {
            return 15;
        }
        return super.getStrongRedstonePower(state, world, pos, direction);
    }

    protected boolean hasComparatorOutput(BlockState state) {
        return state.get(LIT);
    }

    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return state.get(LIT) ? 15 : 0;
    }
}
