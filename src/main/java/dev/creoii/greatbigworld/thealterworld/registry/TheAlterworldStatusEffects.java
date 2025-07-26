package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.effect.FracturedRealmEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class TheAlterworldStatusEffects {
    public static RegistryEntry<StatusEffect> FRACTURED_REALM;

    public static void register() {
        FRACTURED_REALM = Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(GreatBigWorld.NAMESPACE, "fractured_realm"), new FracturedRealmEffect());
    }
}
