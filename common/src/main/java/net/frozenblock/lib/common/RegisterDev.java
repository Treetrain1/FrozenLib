package net.frozenblock.lib.common;

import net.frozenblock.lib.common.item.Camera;
import net.frozenblock.lib.common.item.LootTableWhacker;
import net.frozenblock.lib.common.registry.FrozenRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import static net.frozenblock.lib.common.FrozenMain.id;

public class RegisterDev {

    public static final Camera CAMERA = new Camera(new Item.Properties().tab(CreativeModeTab.TAB_MISC));
    public static final LootTableWhacker LOOT_TABLE_WHACKER = new LootTableWhacker(new Item.Properties().tab(CreativeModeTab.TAB_MISC));

    public static void init() {
        FrozenRegistry.ITEMS.register(id("camera"), () -> CAMERA);
        FrozenRegistry.ITEMS.register(id("loot_table_whacker"), () -> LOOT_TABLE_WHACKER);
    }

}
