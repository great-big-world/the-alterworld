package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public final class TheAlterworldSoundEvents {
    public static final SoundEvent STRUCTURE_ANCIENT_CITY_PORTAL_OPEN = SoundEvent.of(Identifier.of(GreatBigWorld.NAMESPACE, "structure.ancient_city.portal_open"));

    public static void register() {
        Registry.register(Registries.SOUND_EVENT, Identifier.of(GreatBigWorld.NAMESPACE, "structure.ancient_city.portal_open"), STRUCTURE_ANCIENT_CITY_PORTAL_OPEN);
    }
}
