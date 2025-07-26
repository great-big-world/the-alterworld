package dev.creoii.greatbigworld.thealterworld.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;

public class FracturedRealmEffect extends StatusEffect {
    public FracturedRealmEffect() {
        super(StatusEffectCategory.NEUTRAL, 10433183, ParticleTypes.PORTAL);
    }
}
