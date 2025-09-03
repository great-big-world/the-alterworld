package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;

public final class TheAlterworldDataComponentTypes {
    public static ComponentType<Unit> RELIC;

    public static void register() {
        RELIC = Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "relic"), ComponentType.<Unit>builder().codec(Unit.CODEC).packetCodec(Unit.PACKET_CODEC).build());
    }
}
