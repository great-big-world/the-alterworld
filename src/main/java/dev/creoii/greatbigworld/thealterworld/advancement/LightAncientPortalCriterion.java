package dev.creoii.greatbigworld.thealterworld.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.creoii.greatbigworld.thealterworld.registry.TheAlterworldCriteria;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class LightAncientPortalCriterion extends AbstractCriterion<LightAncientPortalCriterion.Conditions> {
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, Conditions::test);
    }

    public record Conditions(Optional<LootContextPredicate> player) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player)).apply(instance, Conditions::new));

        public static AdvancementCriterion<Conditions> create() {
            return TheAlterworldCriteria.LIGHT_ANCIENT_PORTAL.create(new Conditions(Optional.empty()));
        }

        public boolean test() {
            return true;
        }
    }
}
