package net.frozenblock.lib.fabric;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class FrozenPreLaunchFabric implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        /*if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            System.exit(69420);
        }*/
    }
}
