package net.frozenblock.lib.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.frozenblock.lib.common.FrozenClient;
import net.frozenblock.lib.common.entrypoints.FrozenClientEntrypoint;

public final class FrozenClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FrozenClient.init();

        FabricLoader.getInstance().getEntrypointContainers("frozenlib:client", FrozenClientEntrypoint.class).forEach(entrypoint -> {
            try {
                FrozenClientEntrypoint clientPoint = entrypoint.getEntrypoint();
                clientPoint.init();
                if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
                    clientPoint.initDevOnly();
                }
            } catch (Throwable ignored) {

            }
        });
    }

}
