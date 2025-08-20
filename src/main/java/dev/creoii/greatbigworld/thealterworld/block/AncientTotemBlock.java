package dev.creoii.greatbigworld.thealterworld.block;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.thealterworld.TheAlterworld;
import dev.creoii.greatbigworld.thealterworld.block.entity.AncientTotemBlockEntity;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AncientTotemBlock extends Block implements BlockEntityProvider {
    public static final MapCodec<AncientTotemBlock> CODEC = createCodec(AncientTotemBlock::new);
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;

    public MapCodec<? extends AncientTotemBlock> getCodec() {
        return CODEC;
    }

    public AncientTotemBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AncientTotemBlockEntity(pos, state);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (hit.getSide() != state.get(FACING))
            return ActionResult.PASS;

        Vec3d hitPos = hit.getPos().subtract(pos.getX(), pos.getY(), pos.getZ());
        boolean center = hitPos.x > .25f && hitPos.x < .75f && hitPos.z > .25f && hitPos.z < .75f;

        if (center) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AncientTotemBlockEntity ancientTotemBlockEntity && !ancientTotemBlockEntity.getStack().isEmpty()) {
                if (!world.isClient) {
                    if (!player.giveItemStack(ancientTotemBlockEntity.getStack())) {
                        player.dropStack((ServerWorld) world, ancientTotemBlockEntity.getStack());
                    }
                }
                ancientTotemBlockEntity.setStack(ItemStack.EMPTY);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public static void place(StructurePiece structurePiece, TheAlterworld.RelicStructureType type, StructureWorldAccess world, int x, int y, int z, BlockBox chunkBox) {
        structurePiece.addBlock(world, TheAlterworldBlocks.ANCIENT_TOTEM.getDefaultState(), x, y, z, chunkBox);
        BlockPos pos = structurePiece.offsetPos(x, y, z);
        if (world.getBlockEntity(pos) instanceof AncientTotemBlockEntity ancientTotemBlockEntity) {
            //System.out.println("found be");
            Item relic = TheAlterworld.getRandomRelic(type, world.getRandom());
            if (relic != null) {
                //System.out.println("relic: " + relic.getTranslationKey());
                ancientTotemBlockEntity.setStack(relic.getDefaultStack());
            }
        }
    }
}
