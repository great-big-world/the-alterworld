package dev.creoii.greatbigworld.thealterworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.thealterworld.block.entity.AncientPedestalBlockEntity;
import dev.creoii.greatbigworld.thealterworld.block.entity.AncientTotemBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class TheAlterworldBlockEntityTypes {
    public static BlockEntityType<AncientTotemBlockEntity> ANCIENT_TOTEM;
    public static BlockEntityType<AncientPedestalBlockEntity> ANCIENT_PEDESTAL;

    public static void register() {
        ANCIENT_TOTEM = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "ancient_totem"), FabricBlockEntityTypeBuilder.create(AncientTotemBlockEntity::new, TheAlterworldBlocks.ANCIENT_TOTEM).build());
        ANCIENT_PEDESTAL = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "ancient_pedestal"), FabricBlockEntityTypeBuilder.create(AncientPedestalBlockEntity::new, TheAlterworldBlocks.ANCIENT_PEDESTAL).build());
    }
}
