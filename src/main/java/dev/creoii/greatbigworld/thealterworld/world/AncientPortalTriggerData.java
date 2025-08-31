package dev.creoii.greatbigworld.thealterworld.world;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStructureTriggerDataTypes;
import dev.creoii.greatbigworld.util.Codecs;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerData;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerDataType;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public record AncientPortalTriggerData(BlockPos.Mutable portalPos, Map<BlockPos, Boolean> positions) implements StructureTriggerData {
    public static final MapCodec<AncientPortalTriggerData> CODEC = RecordCodecBuilder.mapCodec(instance -> {
        return instance.group(Codecs.BLOCK_POS_STRING_CODEC.fieldOf("portal").forGetter(data -> {
            return data.portalPos.toImmutable();
        }), Codec.unboundedMap(Codecs.BLOCK_POS_STRING_CODEC, Codec.BOOL).fieldOf("positions").forGetter(data -> {
            return data.positions;
        })).apply(instance, (pos, map) -> new AncientPortalTriggerData(pos.mutableCopy(), Maps.newHashMap(map)));
    });

    @Override
    public StructureTriggerDataType<?> getType() {
        return TheAlterworldStructureTriggerDataTypes.ANCIENT_PORTAL;
    }
}
