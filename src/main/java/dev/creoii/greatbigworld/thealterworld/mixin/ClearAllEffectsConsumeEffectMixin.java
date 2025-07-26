package dev.creoii.greatbigworld.thealterworld.mixin;

import com.google.common.collect.Maps;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ClearAllEffectsConsumeEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ClearAllEffectsConsumeEffect.class)
public class ClearAllEffectsConsumeEffectMixin {
    @Inject(method = "onConsume", at = @At("HEAD"), cancellable = true)
    private void gbw$dontClearPlanarFractureEffectMilk(World world, ItemStack stack, LivingEntity user, CallbackInfoReturnable<Boolean> cir) {
        if (world.isClient) {
            cir.setReturnValue(false);
        } else if (user.getActiveStatusEffects().isEmpty()) {
            cir.setReturnValue(false);
        } else {
            Map<RegistryEntry<StatusEffect>, StatusEffectInstance> map = Maps.newHashMap(user.getActiveStatusEffects());
            map.remove(TheAlterworldStatusEffects.PLANAR_FRACTURE);
            map.forEach((entry, statusEffectInstance) -> {
                user.getActiveStatusEffects().remove(entry);
            });
            user.onStatusEffectsRemoved(map.values());
            cir.setReturnValue(true);
        }
        cir.cancel();
    }
}
