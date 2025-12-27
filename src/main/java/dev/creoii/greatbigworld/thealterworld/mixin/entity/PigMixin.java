package dev.creoii.greatbigworld.thealterworld.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.thealterworld.TheAlterworld;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.pig.PigVariant;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(Pig.class)
public abstract class PigMixin extends Animal {
    protected PigMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(method = "finalizeSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/variant/VariantUtils;selectVariantToSpawn(Lnet/minecraft/world/entity/variant/SpawnContext;Lnet/minecraft/resources/ResourceKey;)Ljava/util/Optional;"))
    private Optional<Holder.Reference<PigVariant>> gbw$defaultVariantInOverworld(SpawnContext spawnContext, ResourceKey<Registry<PigVariant>> resourceKey, Operation<Optional<Holder.Reference<PigVariant>>> original, @Local(argsOnly = true) EntitySpawnReason entitySpawnReason) {
        if (level().dimension().equals(Level.OVERWORLD) && TheAlterworld.disableVariantsInOverworld(entitySpawnReason)) {
            return Optional.empty();
        } return original.call(spawnContext, resourceKey);
    }
}
