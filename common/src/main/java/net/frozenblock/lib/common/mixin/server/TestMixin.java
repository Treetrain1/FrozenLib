package net.frozenblock.lib.common.mixin.server;

import dev.architectury.platform.Platform;
import net.frozenblock.lib.common.FrozenMain;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TestMixin {

    @Inject(method = "init", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        if (Platform.isDevelopmentEnvironment()) {
            FrozenMain.LOGGER.info("Common Mixins are working");
        }
    }
}
