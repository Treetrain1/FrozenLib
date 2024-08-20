package net.frozenblock.lib.worldgen.feature.impl.saved;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.NotNull;

public interface ChunkGeneratorSavableFeatureInterface {
	void frozenLib$setFeatureManager(FeatureManager featureManager);

	void frozenLib$createReferences(WorldGenLevel worldGenLevel, FeatureManager featureManager, @NotNull ChunkAccess chunkAccess);
}
