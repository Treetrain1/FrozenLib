package net.frozenblock.lib.core.tags;

import net.frozenblock.lib.core.FrozenMain;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class FrozenBlockTags {
    public static final TagKey<Block> DRIPSTONE_CAN_DRIP_ON = bind("dripstone_can_drip");

    private FrozenBlockTags() {
    }

    private static TagKey<Block> bind(String path) {
        return TagKey.create(Registry.BLOCK_REGISTRY, FrozenMain.id(path));
    }
}
