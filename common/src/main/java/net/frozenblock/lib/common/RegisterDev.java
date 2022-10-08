package net.frozenblock.lib.common;

import com.mojang.datafixers.schemas.Schema;
import dev.architectury.registry.registries.RegistrySupplier;
import net.frozenblock.lib.common.item.Camera;
import net.frozenblock.lib.common.item.LootTableWhacker;
import net.frozenblock.lib.common.registry.FrozenRegistry;
import net.minecraft.core.Registry;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.quiltmc.qsl.frozenblock.common.misc.datafixerupper.api.QuiltDataFixerBuilder;
import org.quiltmc.qsl.frozenblock.common.misc.datafixerupper.api.QuiltDataFixes;
import org.quiltmc.qsl.frozenblock.common.misc.datafixerupper.api.SimpleFixes;

import static net.frozenblock.lib.common.FrozenMain.id;
import static net.frozenblock.lib.common.FrozenMain.log;

public class RegisterDev {

    public static final Camera CAMERA = FrozenRegistry.ITEMS.register(
            id("the_camera"), () -> new Camera(new Item.Properties().tab(CreativeModeTab.TAB_MISC))).getOrNull();
    public static final LootTableWhacker LOOT_TABLE_WHACKER = FrozenRegistry.ITEMS.register(
            id("loot_table_whacker"), () -> new LootTableWhacker(new Item.Properties().tab(CreativeModeTab.TAB_MISC))).getOrNull();

    public static void init() {
        var datafixer = applyTestFixes("frozenblocklib");
    }

    private static final int DATA_VERSION = 5;

    private static QuiltDataFixerBuilder applyTestFixes(String modId) {
        FrozenMain.log("Applying test datafixes", true);
        var builder = new QuiltDataFixerBuilder(DATA_VERSION);
        builder.addSchema(0, QuiltDataFixes.BASE_SCHEMA);
        Schema schemaV1 = builder.addSchema(1, NamespacedSchema::new);
        SimpleFixes.addItemRenameFix(builder, "Rename camera to dev_camera", id("camera"), id("dev_camera"), schemaV1);
        Schema schemaV2 = builder.addSchema(2, NamespacedSchema::new);
        SimpleFixes.addItemRenameFix(builder, "Rename dev_camera to camera", id("dev_camera"), id("camera"), schemaV2);
        Schema schemaV4 = builder.addSchema(4, NamespacedSchema::new);
        SimpleFixes.addItemRenameFix(builder, "Rename camera to sussy_camera", id("camera"), id("sussy_camera"), schemaV4);
        Schema schemaV5 = builder.addSchema(5, NamespacedSchema::new);
        SimpleFixes.addItemRenameFix(builder, "Rename dev_camera to the_camera", id("dev_camera"), id("the_camera"), schemaV2);

        QuiltDataFixes.buildAndRegisterFixer(modId, builder);
        log("Test datafixes for FrozenLib have been applied", true);
        return builder;
    }

}
