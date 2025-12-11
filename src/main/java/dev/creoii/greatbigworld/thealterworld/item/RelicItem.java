package dev.creoii.greatbigworld.thealterworld.item;

import dev.creoii.greatbigworld.thealterworld.block.entity.AncientPedestalBlockEntity;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RelicItem extends Item {
    private final int cooldown;

    public RelicItem(Properties settings, int cooldown) {
        super(settings);
        this.cooldown = cooldown;
    }

    public void activate(Level world, Player player, ItemStack stack) {
        player.getCooldowns().addCooldown(stack, cooldown);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide()) {
            ItemStack stack = user.getItemInHand(hand);
            if (!stack.nextDamageWillBreak()) {
                stack.hurtAndBreak(1, user, hand);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        BlockState state = world.getBlockState(pos);
        Player player = context.getPlayer();
        if (state.is(TheAlterworldBlocks.ANCIENT_PEDESTAL) && !state.getValue(BlockStateProperties.LIT)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AncientPedestalBlockEntity ancientPedestalBlockEntity && ancientPedestalBlockEntity.getRelic() == null) {
                ancientPedestalBlockEntity.setRelic(stack.getItem());
                world.updateNeighbourForOutputSignal(pos, TheAlterworldBlocks.ANCIENT_PEDESTAL);
                stack.consume(1, player);
                if (!world.isClientSide()) {
                    world.playSound(player, pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d, TheAlterworldSoundEvents.BLOCK_ANCIENT_PEDESTAL_PLACE, SoundSource.BLOCKS, 1f, 1f);
                    world.levelEvent(1503, pos, 0);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }
}
