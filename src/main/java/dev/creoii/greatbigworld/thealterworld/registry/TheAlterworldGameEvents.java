package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gameevent.GameEvent;

public final class TheAlterworldGameEvents {
    public static Holder<GameEvent> ANCIENT_PORTAL_ACTIVATED;

    public static void register() {
        ANCIENT_PORTAL_ACTIVATED = Registry.registerForHolder(BuiltInRegistries.GAME_EVENT, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "ancient_portal_activated"), new GameEvent(16));
    }
}
