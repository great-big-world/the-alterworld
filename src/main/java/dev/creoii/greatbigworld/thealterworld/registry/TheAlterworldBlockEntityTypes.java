package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.block.entity.AncientPedestalBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class TheAlterworldBlockEntityTypes {
    public static BlockEntityType<AncientPedestalBlockEntity> ANCIENT_PEDESTAL;

    public static void register() {
        ANCIENT_PEDESTAL = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "ancient_pedestal"), FabricBlockEntityTypeBuilder.create(AncientPedestalBlockEntity::new, TheAlterworldBlocks.ANCIENT_PEDESTAL).build());
    }
}
