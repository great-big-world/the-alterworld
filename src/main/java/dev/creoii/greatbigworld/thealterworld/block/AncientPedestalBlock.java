package dev.creoii.greatbigworld.thealterworld.block;

import dev.creoii.greatbigworld.relicsandruins.item.RelicItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
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
            }
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
