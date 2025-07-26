package dev.creoii.greatbigworld.thealterworld.block;

import dev.creoii.greatbigworld.thealterworld.world.AlterworldPortal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Optional;

public class AncientMosaicBlock extends Block {
    public AncientMosaicBlock(Settings settings) {
        super(settings);
    }

    /*@Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {
            ItemStack held = player.getStackInHand(player.getActiveHand());
            if (held.isOf(Items.DIAMOND)) {
                Optional<AlterworldPortal> optional = AlterworldPortal.getNewPortal(world, pos, Direction.Axis.X);
                optional.ifPresent(portal -> portal.createPortal(world, true));
            }
        }
        return super.onUse(state, world, pos, player, hit);
    }*/
}
