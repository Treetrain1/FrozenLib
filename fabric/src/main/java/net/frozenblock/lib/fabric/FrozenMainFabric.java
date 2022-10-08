package net.frozenblock.lib.fabric;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.frozenblock.lib.common.FrozenMain;
import net.frozenblock.lib.common.entrypoints.FrozenMainEntrypoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.frozenblock.lib.common.events.FrozenLifecycleEvents;
import net.frozenblock.lib.common.worldgen.surface.FrozenSurfaceRules;
import net.frozenblock.lib.fabric.events.FrozenEvents;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.frozenblock.common.worldgen.surface_rule.api.SurfaceRuleContext;
import org.quiltmc.qsl.frozenblock.common.worldgen.surface_rule.api.SurfaceRuleEvents;

public final class FrozenMainFabric implements ModInitializer, SurfaceRuleEvents.OverworldModifierCallback, SurfaceRuleEvents.NetherModifierCallback {

    @Override
    public void onInitialize() {
        FrozenMain.init();

        FabricLoader.getInstance().getEntrypointContainers("frozenlib:main", FrozenMainEntrypoint.class).forEach(entrypoint -> {
            try {
                FrozenMainEntrypoint mainPoint = entrypoint.getEntrypoint();
                mainPoint.init();
                if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                    mainPoint.initDevOnly();
                }
            } catch (Throwable ignored) {

            }
        });
        FrozenEvents.register(SurfaceRuleEvents.MODIFY_OVERWORLD, SurfaceRuleEvents.OverworldModifierCallback.class);
        FrozenEvents.register(SurfaceRuleEvents.MODIFY_NETHER, SurfaceRuleEvents.NetherModifierCallback.class);
        FrozenEvents.register(SurfaceRuleEvents.MODIFY_THE_END, SurfaceRuleEvents.TheEndModifierCallback.class);
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((instance, resourceManager) -> {
            FrozenLifecycleEvents.START_DATA_PACK_RELOAD.invoker().onStartDataPackReload(instance, resourceManager);
            FrozenMain.log("DATAPACKS STARTING RELOAD", true);
        });
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            FrozenLifecycleEvents.END_DATA_PACK_RELOAD.invoker().onEndDataPackReload(server, resourceManager,  success);
            FrozenMain.log("DATAPACKS ENDING RELOAD", true);
        });

    }

    @Override
    public void modifyOverworldRules(SurfaceRuleContext.@NotNull Overworld context) {
        context.materialRules().add(0, SurfaceRules.ifTrue(
                SurfaceRules.isBiome(Biomes.DESERT),
                FrozenSurfaceRules.COARSE_DIRT
        ));
        context.materialRules().add(SurfaceRules.ifTrue(
                SurfaceRules.isBiome(Biomes.DESERT),
                FrozenSurfaceRules.COARSE_DIRT
        ));
    }

    @Override
    public void modifyNetherRules(SurfaceRuleContext.@NotNull Nether context) {
        context.materialRules().add(0, SurfaceRules.ifTrue(
                SurfaceRules.isBiome(Biomes.DESERT),
                FrozenSurfaceRules.COARSE_DIRT
        ));
        context.materialRules().add(SurfaceRules.ifTrue(
                SurfaceRules.isBiome(Biomes.BASALT_DELTAS),
                FrozenSurfaceRules.COARSE_DIRT
        ));
    }
}
