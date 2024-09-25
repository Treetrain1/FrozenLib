package net.frozenblock.lib.image_transfer.mixin.client;

import net.frozenblock.lib.image_transfer.client.ServerTexture;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextureManager.class)
public class TextureManagerMixin {

    @Inject(method = "getTexture*", at = @At("RETURN"))
    public void frozenLib$updateServerTextureReferenceTime(CallbackInfoReturnable<AbstractTexture> info) {
        if (info.getReturnValue() instanceof ServerTexture serverTexture) {
            serverTexture.updateReferenceTime();
        }
    }
}
