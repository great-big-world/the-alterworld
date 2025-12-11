package dev.creoii.greatbigworld.thealterworld.world;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStructureTriggerDataTypes;
import dev.creoii.greatbigworld.util.Codecs;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerData;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerDataType;
import java.util.Map;
import net.minecraft.core.BlockPos;

public record AncientPortalTriggerData(BlockPos.MutableBlockPos portalPos, Map<BlockPos, Boolean> positions) implements StructureTriggerData {
    public static final MapCodec<AncientPortalTriggerData> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(Codecs.BLOCK_POS_STRING_CODEC.fieldOf("portal").forGetter(data -> {
            return data.portalPos.immutable();
        }), Codec.unboundedMap(Codecs.BLOCK_POS_STRING_CODEC, Codec.BOOL).fieldOf("positions").forGetter(data -> {
            return data.positions;
        })).apply(instance, (pos, map) -> new AncientPortalTriggerData(pos.mutable(), Maps.newHashMap(map)));
    });

    @Override
    public StructureTriggerDataType<?> getType() {
        return TheAlterworldStructureTriggerDataTypes.ANCIENT_PORTAL;
    }
}
