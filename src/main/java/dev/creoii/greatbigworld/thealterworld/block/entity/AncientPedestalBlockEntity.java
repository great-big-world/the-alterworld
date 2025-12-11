package dev.creoii.greatbigworld.thealterworld.block.entity;

import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
        setChanged();
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag nbt = new CompoundTag();
        if (relic != null) {
            nbt.store("relic", BuiltInRegistries.ITEM.byNameCodec(), registries.createSerializationContext(NbtOps.INSTANCE), relic);
        }
        return nbt;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void loadAdditional(ValueInput view) {
        relic = view.read("relic", BuiltInRegistries.ITEM.byNameCodec()).orElse(null);
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        if (relic != null) {
            view.store("relic", BuiltInRegistries.ITEM.byNameCodec(), relic);
        }
    }
}
