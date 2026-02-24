package dev.creoii.greatbigworld.thealterworld.util;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class TheAlterworldBlockTags {
    public static final TagKey<Block> ANCIENT_PORTAL_REPLACEABLE = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "ancient_portal_replaceable"));
}
