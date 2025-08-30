package dev.creoii.greatbigworld.thealterworld.registry;

import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.registry.GBWRegistries;
import dev.creoii.greatbigworld.thealterworld.world.AncientPortalTriggerData;
import dev.creoii.greatbigworld.world.structuretrigger.data.StructureTriggerDataType;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public final class TheAlterworldStructureTriggerDataTypes {
    public static final StructureTriggerDataType<AncientPortalTriggerData, AncientPortalTriggerData> ANCIENT_PORTAL = new StructureTriggerDataType<>() {
        @Override
        public MapCodec<AncientPortalTriggerData> codec() {
            return AncientPortalTriggerData.CODEC;
        }

        @Override
        public AncientPortalTriggerData create() {
            return new AncientPortalTriggerData(null, new HashMap<>());
        }
    };

    public static void register() {
        Registry.register(GBWRegistries.STRUCTURE_TRIGGER_DATA_TYPES, Identifier.of(GreatBigWorld.NAMESPACE, "ancient_portal"), ANCIENT_PORTAL);
    }
}
