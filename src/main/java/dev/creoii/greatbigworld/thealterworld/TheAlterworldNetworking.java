package dev.creoii.greatbigworld.thealterworld;

import dev.creoii.greatbigworld.thealterworld.block.ReinforcedDeepslateBlock;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class TheAlterworldNetworking {
    public static void register() {
        PayloadTypeRegistry.playS2C().register(ReinforcedDeepslateBlock.FractureS2C.PACKET_ID, ReinforcedDeepslateBlock.FractureS2C.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(ReinforcedDeepslateBlock.AncientGlowS2C.PACKET_ID, ReinforcedDeepslateBlock.AncientGlowS2C.PACKET_CODEC);
    }
}
