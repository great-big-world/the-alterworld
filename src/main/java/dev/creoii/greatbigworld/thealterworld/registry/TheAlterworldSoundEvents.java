package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public final class TheAlterworldSoundEvents {
    public static final SoundEvent STRUCTURE_ANCIENT_CITY_PORTAL_OPEN = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "structure.ancient_city.portal_open"));
    public static final SoundEvent BLOCK_ANCIENT_PEDESTAL_PLACE = SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "block.ancient_pedestal.place"));

    public static void register() {
        Registry.register(BuiltInRegistries.SOUND_EVENT, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "structure.ancient_city.portal_open"), STRUCTURE_ANCIENT_CITY_PORTAL_OPEN);
        Registry.register(BuiltInRegistries.SOUND_EVENT, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "block.ancient_pedestal.place"), BLOCK_ANCIENT_PEDESTAL_PLACE);
    }
}
