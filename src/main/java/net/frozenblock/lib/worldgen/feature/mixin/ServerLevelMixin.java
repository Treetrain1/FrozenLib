package net.frozenblock.lib.worldgen.feature.mixin;

import net.frozenblock.lib.worldgen.feature.impl.saved.FeatureManager;
import net.frozenblock.lib.worldgen.feature.impl.saved.ServerLevelInterface;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerLevel.class)
public class ServerLevelMixin implements ServerLevelInterface {

	@Unique
	private FeatureManager featureManager;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void setFeatureManager(MinecraftServer server, Executor executor, LevelStorageSource.LevelStorageAccess session, ServerLevelData worldProperties, ResourceKey registryKey, LevelStem dimensionOptions, ChunkProgressListener worldgenProgressListener, boolean debug, long seed, List spawners, boolean shouldTickTime, RandomSequences randomSequences, CallbackInfo ci) {
		this.featureManager = new FeatureManager(ServerLevel.class.cast(this));
	}

	@Override
	public FeatureManager frozenLib$featureManager() {
		return this.featureManager;
	}
}
