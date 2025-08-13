package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.advancement.LightAncientPortalCriterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class TheAlterworldCriteria {
    public static LightAncientPortalCriterion LIGHT_ANCIENT_PORTAL;

    public static void register() {
        LIGHT_ANCIENT_PORTAL = Registry.register(Registries.CRITERION, Identifier.of(GreatBigWorld.NAMESPACE, "light_ancient_portal"), new LightAncientPortalCriterion());
    }
}
