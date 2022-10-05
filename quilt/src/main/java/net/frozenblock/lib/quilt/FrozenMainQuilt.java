package net.frozenblock.lib.quilt;

import net.frozenblock.lib.common.FrozenMain;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class FrozenMainQuilt implements ModInitializer {

    @Override
    public void onInitialize(ModContainer mod) {
        FrozenMain.init();
    }
}
