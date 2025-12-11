package dev.creoii.greatbigworld.thealterworld.mixin.world;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.chicken.ChickenVariants;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.cow.CowVariant;
import net.minecraft.world.entity.animal.cow.CowVariants;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.pig.PigVariant;
import net.minecraft.world.entity.animal.pig.PigVariants;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.animal.wolf.WolfVariants;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin extends Level {
    protected ServerWorldMixin(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Inject(method = "addFreshEntity", at = @At("HEAD"))
    private void gbw$onSpawnEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (dimension().equals(Level.OVERWORLD)) {
            if (entity instanceof Cow cow) {
                Optional<Holder.Reference<CowVariant>> defaultVariant = registryAccess().get(CowVariants.DEFAULT);
                defaultVariant.ifPresent(cow::setVariant);
            } else if (entity instanceof Pig pig) {
                Optional<Holder.Reference<PigVariant>> defaultVariant = registryAccess().get(PigVariants.DEFAULT);
                defaultVariant.ifPresent(pig::setVariant);
            } else if (entity instanceof Chicken chicken) {
                Optional<Holder.Reference<ChickenVariant>> defaultVariant = registryAccess().get(ChickenVariants.DEFAULT);
                defaultVariant.ifPresent(chicken::setVariant);
            } else if (entity instanceof Wolf wolf) {
                Optional<Holder.Reference<WolfVariant>> defaultVariant = registryAccess().get(WolfVariants.DEFAULT);
                defaultVariant.ifPresent(wolf::setVariant);
            }
        }
    }
}
