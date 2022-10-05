package net.frozenblock.lib.common;

import dev.architectury.registry.registries.RegistrySupplier;
import net.frozenblock.lib.common.item.Camera;
import net.frozenblock.lib.common.item.LootTableWhacker;
import net.frozenblock.lib.common.registry.FrozenRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import static net.frozenblock.lib.common.FrozenMain.id;

public class RegisterDev {

    public static final RegistrySupplier<Camera> CAMERA = FrozenRegistry.ITEMS.register(
            id("camera"), () -> new Camera(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistrySupplier<LootTableWhacker> LOOT_TABLE_WHACKER = FrozenRegistry.ITEMS.register(
            id("loot_table_whacker"), () -> new LootTableWhacker(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static void init() {
    }

}
