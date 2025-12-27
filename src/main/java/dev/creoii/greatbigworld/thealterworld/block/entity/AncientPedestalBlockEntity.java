package dev.creoii.greatbigworld.thealterworld.block.entity;

import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class AncientPedestalBlockEntity extends BlockEntity {
    private ItemStack stack;

    public AncientPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(TheAlterworldBlockEntityTypes.ANCIENT_PEDESTAL, pos, state);
        stack = ItemStack.EMPTY;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
        setChanged();
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        stack = view.read("stack", ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        if (!stack.isEmpty()) {
            view.store("stack", ItemStack.CODEC, stack);
        }
    }
}
