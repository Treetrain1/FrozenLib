package net.frozenblock.lib.common.mixin.server;

import net.frozenblock.lib.common.FrozenMain;
import net.minecraft.server.Bootstrap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bootstrap.class)
public class BootstrapMixin {

    @Shadow
    private static void wrapStreams() {
        throw new IllegalStateException("Failed to inject mixin.");
    }

    @Inject(method = "bootStrap", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;freezeBuiltins()V"))
    private static void bootStrap(CallbackInfo ci) {
        //wrapStreams();
        //FrozenMain.init();
    }
}
