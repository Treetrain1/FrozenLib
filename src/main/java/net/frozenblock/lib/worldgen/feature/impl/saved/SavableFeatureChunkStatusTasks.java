package net.frozenblock.lib.worldgen.feature.impl.saved;

import java.util.concurrent.CompletableFuture;
import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.StaticCache2D;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStep;
import net.minecraft.world.level.chunk.status.WorldGenContext;
import org.jetbrains.annotations.NotNull;

public class SavableFeatureChunkStatusTasks {

	public static @NotNull CompletableFuture<ChunkAccess> generateFeatureReferences(
		@NotNull WorldGenContext worldGenContext, ChunkStep chunkStep, StaticCache2D<GenerationChunkHolder> staticCache2D, ChunkAccess chunkAccess
	) {
		ServerLevel serverLevel = worldGenContext.level();
		WorldGenRegion worldGenRegion = new WorldGenRegion(serverLevel, staticCache2D, chunkStep, chunkAccess);
		((ChunkGeneratorSavableFeatureInterface)worldGenContext.generator()).frozenLib$createReferences(
			worldGenRegion,
			((ServerLevelFeatureManagerInterface)serverLevel).frozenLib$featureManager().forWorldGenRegion(worldGenRegion),
			chunkAccess
		);
		return CompletableFuture.completedFuture(chunkAccess);
	}

}
