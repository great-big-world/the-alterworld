package dev.creoii.greatbigworld.thealterworld.mixin.entity;

import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldBlocks;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.dimension.PortalManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract Random getRandom();
    @Shadow @Nullable public PortalManager portalManager;
    @Shadow private World world;

    @Inject(method = "tickPortalTeleportation", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/entity/Entity;", shift = At.Shift.AFTER))
    private void gbw$applyFracturedRealmEffect(CallbackInfo ci) {
        if (world.getBlockState(portalManager.getPortalPos()).isOf(TheAlterworldBlocks.ALTERWORLD_PORTAL)) {
            if (((Entity) (Object) this) instanceof LivingEntity living) {
                int ticks = getRandom().nextBetween(620, 3620);
                System.out.println("fractured portal entry: " + ticks);
                living.addStatusEffect(new StatusEffectInstance(TheAlterworldStatusEffects.FRACTURED_REALM, ticks /* 30 seconds to 3 minutes */, 0, true, false));
            }
        }
    }
}
