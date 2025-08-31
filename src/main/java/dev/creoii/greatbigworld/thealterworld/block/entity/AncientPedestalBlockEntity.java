package dev.creoii.greatbigworld.thealterworld.block.entity;

import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;

public class AncientPedestalBlockEntity extends BlockEntity {
    private Item relic;

    public AncientPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(TheAlterworldBlockEntityTypes.ANCIENT_PEDESTAL, pos, state);
        relic = null;
    }

    public Item getRelic() {
        return relic;
    }

    public void setRelic(Item relic) {
        this.relic = relic;
        markDirty();
    }

    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        NbtCompound nbt = new NbtCompound();
        if (relic != null) {
            nbt.put("relic", Registries.ITEM.getCodec(), registries.getOps(NbtOps.INSTANCE), relic);
        }
        return nbt;
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    protected void readData(ReadView view) {
        relic = view.read("relic", Registries.ITEM.getCodec()).orElse(null);
    }

    @Override
    protected void writeData(WriteView view) {
        if (relic != null) {
            view.put("relic", Registries.ITEM.getCodec(), relic);
        }
    }
}
