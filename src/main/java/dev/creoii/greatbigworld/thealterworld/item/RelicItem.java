package dev.creoii.greatbigworld.thealterworld.item;

import dev.creoii.greatbigworld.thealterworld.block.entity.AncientPedestalBlockEntity;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RelicItem extends Item {
    private final int cooldown;

    public RelicItem(Settings settings, int cooldown) {
        super(settings);
        this.cooldown = cooldown;
    }

    public void activate(World world, PlayerEntity player, ItemStack stack) {
        player.getItemCooldownManager().set(stack, cooldown);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            ItemStack stack = user.getStackInHand(hand);
            if (!stack.willBreakNextUse()) {
                stack.damage(1, user, hand);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getStack();
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        BlockState state = world.getBlockState(pos);
        PlayerEntity player = context.getPlayer();
        if (state.isOf(TheAlterworldBlocks.ANCIENT_PEDESTAL) && !state.get(Properties.LIT)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AncientPedestalBlockEntity ancientPedestalBlockEntity && ancientPedestalBlockEntity.getRelic() == null) {
                ancientPedestalBlockEntity.setRelic(stack.getItem());
                world.updateComparators(pos, TheAlterworldBlocks.ANCIENT_PEDESTAL);
                stack.decrementUnlessCreative(1, player);
                if (!world.isClient) {
                    world.playSound(player, pos.getX() + .5d, pos.getY() + .5d, pos.getZ() + .5d, TheAlterworldSoundEvents.BLOCK_ANCIENT_PEDESTAL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    world.syncWorldEvent(1503, pos, 0);
                }
                return ActionResult.SUCCESS;
            }
        }
        return super.useOnBlock(context);
    }
}
