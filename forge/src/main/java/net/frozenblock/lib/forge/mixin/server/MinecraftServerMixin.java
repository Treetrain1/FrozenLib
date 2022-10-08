package net.frozenblock.lib.forge.mixin.server;

import dev.architectury.patchedmixin.staticmixin.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.frozenblock.lib.common.events.FrozenLifecycleEvents;
import net.frozenblock.lib.common.worldgen.biome.impl.OverworldBiomeData;
import net.frozenblock.lib.forge.worldgen.biome.ForgeOverworldBiomeData;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Shadow @Final
    protected WorldData worldData;

    @Shadow @Final private RegistryAccess.Frozen registryHolder;

    @Inject(method = "createLevels", at = @At("HEAD"))
    private void addOverworldBiomes(ChunkProgressListener chunkProgressListener, CallbackInfo ci) {
        this.worldData.worldGenSettings().dimensions().stream().forEach(dimensionOptions -> ForgeOverworldBiomeData.modifyBiomeSource(this.registryHolder.registryOrThrow(Registry.BIOME_REGISTRY), dimensionOptions.generator().getBiomeSource()));
    }

    @Shadow private MinecraftServer.ReloadableResources resources;

    /*@Inject(method = "reloadResources", at = @At("HEAD"))
    private void startResourceReload(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        FrozenLifecycleEvents.START_DATA_PACK_RELOAD.invoker().onStartDataPackReload((MinecraftServer) (Object) this, this.resources.resourceManager());
    }

    @Inject(method = "reloadResources", at = @At("TAIL"))
    private void endResourceReload(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        cir.getReturnValue().handleAsync((value, throwable) -> {
            FrozenLifecycleEvents.END_DATA_PACK_RELOAD.invoker().onEndDataPackReload((MinecraftServer) (Object) this, this.resources.resourceManager(), throwable == null);
            return value;
        }, (MinecraftServer) (Object) this);
    }*/
}
