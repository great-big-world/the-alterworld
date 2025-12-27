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
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimal {
    protected WolfMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(method = "finalizeSpawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/variant/VariantUtils;selectVariantToSpawn(Lnet/minecraft/world/entity/variant/SpawnContext;Lnet/minecraft/resources/ResourceKey;)Ljava/util/Optional;"))
    private Optional<Holder.Reference<WolfVariant>> gbw$defaultVariantInOverworld(SpawnContext spawnContext, ResourceKey<Registry<WolfVariant>> resourceKey, Operation<Optional<Holder.Reference<WolfVariant>>> original, @Local(argsOnly = true) EntitySpawnReason entitySpawnReason) {
        if (level().dimension().equals(Level.OVERWORLD) && TheAlterworld.disableVariantsInOverworld(entitySpawnReason)) {
            return Optional.empty();
        } return original.call(spawnContext, resourceKey);
    }
}
