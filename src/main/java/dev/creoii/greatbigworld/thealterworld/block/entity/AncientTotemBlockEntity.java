package dev.creoii.greatbigworld.thealterworld.block.entity;

import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;

public class AncientTotemBlockEntity extends BlockEntity {
    private ItemStack stack;

    public AncientTotemBlockEntity(BlockPos pos, BlockState state) {
        super(TheAlterworldBlockEntityTypes.ANCIENT_TOTEM, pos, state);
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

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        stack = view.read("stack", ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        if (!stack.isEmpty()) {
            view.put("stack", ItemStack.CODEC, stack);
        }
    }
}
