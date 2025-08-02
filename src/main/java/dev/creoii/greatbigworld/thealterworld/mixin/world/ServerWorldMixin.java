package dev.creoii.greatbigworld.thealterworld.mixin.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {
    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Inject(method = "spawnEntity", at = @At("HEAD"))
    private void gbw$onSpawnEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (getRegistryKey().equals(World.OVERWORLD)) {
            if (entity instanceof CowEntity cow) {
                Optional<RegistryEntry.Reference<CowVariant>> defaultVariant = getRegistryManager().getOptionalEntry(CowVariants.DEFAULT);
                defaultVariant.ifPresent(cow::setVariant);
            } else if (entity instanceof PigEntity pig) {
                Optional<RegistryEntry.Reference<PigVariant>> defaultVariant = getRegistryManager().getOptionalEntry(PigVariants.DEFAULT);
                defaultVariant.ifPresent(pig::setVariant);
            } else if (entity instanceof ChickenEntity chicken) {
                Optional<RegistryEntry.Reference<ChickenVariant>> defaultVariant = getRegistryManager().getOptionalEntry(ChickenVariants.DEFAULT);
                defaultVariant.ifPresent(chicken::setVariant);
            } else if (entity instanceof WolfEntity wolf) {
                Optional<RegistryEntry.Reference<WolfVariant>> defaultVariant = getRegistryManager().getOptionalEntry(WolfVariants.DEFAULT);
                defaultVariant.ifPresent(wolf::setVariant);
            }
        }
    }
}
