package dev.creoii.greatbigworld.thealterworld.block.entity;

import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

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
    }

    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        NbtCompound nbtCompound = super.toInitialChunkDataNbt(registries);
        if (!stack.isEmpty()) {
            nbtCompound.put("stack", ItemStack.CODEC, registries.getOps(NbtOps.INSTANCE), stack);
        }

        return nbtCompound;
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        stack = nbt.get("stack", ItemStack.CODEC, registries.getOps(NbtOps.INSTANCE)).orElse(ItemStack.EMPTY);
    }

    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        if (!stack.isEmpty()) {
            nbt.put("stack", ItemStack.CODEC, registries.getOps(NbtOps.INSTANCE), stack);
        }
    }
}
