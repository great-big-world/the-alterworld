package dev.creoii.greatbigworld.thealterworld.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStructureTriggerDataTypes;
import dev.creoii.greatbigworld.util.Codecs;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerData;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerDataType;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class AncientPortalTriggerData extends StructureTriggerData {
    public static final MapCodec<AncientPortalTriggerData> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(Codecs.BLOCK_POS_STRING_CODEC.fieldOf("portal").forGetter(data -> {
            return data.portalPos;
        }), Codec.unboundedMap(Codecs.BLOCK_POS_STRING_CODEC, Codec.BOOL).fieldOf("positions").forGetter(data -> {
            return data.positions;
        })).apply(instance, AncientPortalTriggerData::new);
    });
    private BlockPos portalPos;
    private final Map<BlockPos, Boolean> positions;

    public AncientPortalTriggerData(BlockPos portalPos, Map<BlockPos, Boolean> positions) {
        this.portalPos = portalPos;
        this.positions = positions;
    }

    public BlockPos getPortalPos() {
        return portalPos;
    }

    public void setPortalPos(BlockPos portalPos) {
        this.portalPos = portalPos;
    }

    public Map<BlockPos, Boolean> getPositions() {
        return positions;
    }

    @Override
    public StructureTriggerDataType<?, ?> getType() {
        return TheAlterworldStructureTriggerDataTypes.ANCIENT_PORTAL;
    }
}
