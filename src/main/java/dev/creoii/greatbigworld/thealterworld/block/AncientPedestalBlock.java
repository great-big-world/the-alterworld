package dev.creoii.greatbigworld.thealterworld.block;

import dev.creoii.greatbigworld.swordsandshields.util.EnchantmentUtil;
import dev.creoii.greatbigworld.thealterworld.block.entity.AncientPedestalBlockEntity;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class AncientPedestalBlock extends Block implements EntityBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty HAS_OFFERING = BooleanProperty.create("has_offering");
    public static final VoxelShape TOP_SHAPE = Block.box(4f, 6f, 4f, 12f, 12f, 12f);
    public static final VoxelShape BASE_SHAPE = Block.box(0f, 0f, 0f, 16f, 6f, 16f);
    public static final VoxelShape SHAPE = Shapes.or(BASE_SHAPE, TOP_SHAPE);

    public AncientPedestalBlock(Properties settings) {
        super(settings);
        registerDefaultState(getStateDefinition().any().setValue(LIT, false).setValue(HAS_OFFERING, false));
    }

    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AncientPedestalBlockEntity(pos, state);
    }

    @Override
    protected void entityInside(BlockState state, Level world, BlockPos pos, Entity entity, InsideBlockEffectApplier handler, boolean bl) {
        if (state.getValue(LIT) && entity instanceof LivingEntity && !world.isClientSide()) {
            entity.hurtServer((ServerLevel) world, world.damageSources().inFire(), 2f);
        }
    }

    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(LIT) && random.nextInt(10) == 0) {
            world.playLocalSound(pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, .5f + random.nextFloat(), random.nextFloat() * .7f + .6f, false);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (state.getValueOrElse(LIT, false))
            return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(player.getUsedItemHand());
        if (level.getBlockEntity(pos) instanceof AncientPedestalBlockEntity pedestalBlockEntity) {
            Vec3 vec3d = hit.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());

            if (vec3d.x >= .25d && vec3d.x <= .75d && vec3d.z >= .25d && vec3d.z <= .75d) {
                if (pedestalBlockEntity.getStack().isEmpty())
                    return InteractionResult.PASS;

                ItemStack relic = pedestalBlockEntity.getStack();
                if (stack.is(relic.getItem()) || stack.isEmpty()) {
                    level.setBlock(pos, state.setValue(HAS_OFFERING, false), 3);
                    if (!level.isClientSide()) {
                        player.addItem(relic);
                        level.playSound(player, pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d, TheAlterworldSoundEvents.BLOCK_ANCIENT_PEDESTAL_PLACE, SoundSource.BLOCKS, .8f, .5f);
                    }
                    pedestalBlockEntity.setStack(ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (isOfferable(stack) && state.is(TheAlterworldBlocks.ANCIENT_PEDESTAL) && !state.getValue(BlockStateProperties.LIT)) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AncientPedestalBlockEntity ancientPedestalBlockEntity && ancientPedestalBlockEntity.getStack().isEmpty()) {
                ancientPedestalBlockEntity.setStack(stack);
                level.setBlock(pos, state.setValue(HAS_OFFERING, true), 3);
                level.updateNeighbourForOutputSignal(pos, TheAlterworldBlocks.ANCIENT_PEDESTAL);
                stack.consume(1, player);
                if (!level.isClientSide()) {
                    level.playSound(player, pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d, TheAlterworldSoundEvents.BLOCK_ANCIENT_PEDESTAL_PLACE, SoundSource.BLOCKS, 1f, 1f);
                    level.levelEvent(1503, pos, 0);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, interactionHand, blockHitResult);
    }

    private static boolean isOfferable(ItemStack stack) {
        return EnchantmentUtil.getEnchantmentPower(stack) >= 11;
    }

    @Override
    public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (world.getBlockEntity(pos) instanceof AncientPedestalBlockEntity ancientPedestalBlock && ancientPedestalBlock.getStack() != null && !state.getValue(LIT)) {
            ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), ancientPedestalBlock.getStack());
            itemEntity.setDefaultPickUpDelay();
            world.addFreshEntity(itemEntity);
        }
        return super.playerWillDestroy(world, pos, state, player);
    }

    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    protected int getComparatorOutput(BlockState state, Level world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof AncientPedestalBlockEntity pedestalBlockEntity) {
            return pedestalBlockEntity.getStack().isEmpty() ? 0 : 15;
        }
        return 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, HAS_OFFERING);
    }
}
