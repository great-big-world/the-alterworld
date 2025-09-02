package dev.creoii.greatbigworld.thealterworld.block;

import dev.creoii.greatbigworld.thealterworld.block.entity.AncientPedestalBlockEntity;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldSoundEvents;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
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
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
        if (state.get(LIT) && entity instanceof LivingEntity && !world.isClient) {
            entity.damage((ServerWorld) world, world.getDamageSources().inFire(), 2f);
        }
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LIT) && random.nextInt(10) == 0) {
            world.playSoundClient(pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, .5f + random.nextFloat(), random.nextFloat() * .7f + .6f, false);
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(player.getActiveHand());
        if (hit.getSide() == Direction.UP && world.getBlockEntity(pos) instanceof AncientPedestalBlockEntity pedestalBlockEntity) {
            Vec3d vec3d = hit.getPos().subtract(pos.getX(), pos.getY(), pos.getZ());

            if (vec3d.x >= .25d && vec3d.x <= .75d && vec3d.z >= .25d && vec3d.z <= .75d) {
                if (pedestalBlockEntity.getRelic() == null)
                    return ActionResult.PASS;

                ItemStack relic = pedestalBlockEntity.getRelic().getDefaultStack();
                if (stack.isOf(relic.getItem()) || stack.isEmpty()) {
                    if (!world.isClient) {
                        player.giveItemStack(relic);
                        world.playSound(player, pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d, TheAlterworldSoundEvents.BLOCK_ANCIENT_PEDESTAL_PLACE, SoundCategory.BLOCKS, .8f, .5f);
                    }
                    pedestalBlockEntity.setRelic(null);
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (world.getBlockEntity(pos) instanceof AncientPedestalBlockEntity ancientPedestalBlock && ancientPedestalBlock.getRelic() != null) {
            ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), ancientPedestalBlock.getRelic().getDefaultStack());
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity);
        }
        return super.onBreak(world, pos, state, player);
    }

    protected boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof AncientPedestalBlockEntity pedestalBlockEntity) {
            return pedestalBlockEntity.getRelic() == null ? 0 : 15;
        }
        return 0;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
