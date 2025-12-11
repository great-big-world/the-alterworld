package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.effect.PlanarFractureEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

public final class TheAlterworldStatusEffects {
    public static Holder<MobEffect> PLANAR_FRACTURE;

    public static void register() {
        PLANAR_FRACTURE = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "planar_fracture"), new PlanarFractureEffect());
    }
}
