package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.effect.PlanarFractureEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class TheAlterworldStatusEffects {
    public static RegistryEntry<StatusEffect> PLANAR_FRACTURE;

    public static void register() {
        PLANAR_FRACTURE = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(GreatBigWorld.NAMESPACE, "planar_fracture"), new PlanarFractureEffect());
    }
}
