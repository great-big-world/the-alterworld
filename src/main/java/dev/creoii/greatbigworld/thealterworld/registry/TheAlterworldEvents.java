package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.data.Mappings;
import dev.creoii.greatbigworld.registry.GBWRegistries;
import dev.creoii.greatbigworld.thealterworld.util.ExtendedChunkGeneratorSettings;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.util.Optional;

public final class TheAlterworldEvents {
    public static void register() {
        ServerLifecycleEvents.SERVER_STARTING.register(minecraftServer -> {
            Optional<Registry<Mappings>> optionalRegistry = minecraftServer.registryAccess().lookup(GBWRegistries.MAPPINGS_KEY);
            Optional<Registry<NoiseGeneratorSettings>> optionalRegistry2 = minecraftServer.registryAccess().lookup(Registries.NOISE_SETTINGS);
            if (optionalRegistry.isPresent() && optionalRegistry2.isPresent()) {
                Mappings mappings = optionalRegistry.get().getValue(Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "world_lava_heights"));
                if (mappings == null)
                    return;

                optionalRegistry2.get().entrySet().forEach(entry -> {
                    Mappings.Value value = mappings.getValue(minecraftServer.registryAccess(), entry.getKey().identifier());
                    if (value.type() == Mappings.Value.PrimitiveType.STRING)
                        return;
                    ((ExtendedChunkGeneratorSettings) (Object) entry.getValue()).gbw$setLavaHeight(value.getAsNumber().intValue());
                });
            }
        });
    }
}
