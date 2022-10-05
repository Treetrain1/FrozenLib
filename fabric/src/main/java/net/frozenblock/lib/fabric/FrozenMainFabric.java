package net.frozenblock.lib.fabric;

import net.frozenblock.lib.common.FrozenMain;
import net.frozenblock.lib.fabric.entrypoints.FrozenMainEntrypoint;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public final class FrozenMainFabric implements ModInitializer {

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
    }

}
