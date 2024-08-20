package net.frozenblock.lib.worldgen.feature.impl.saved;

import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import java.util.Collections;
import java.util.Map;

public interface ChunkAccessFeatureInterface {
	Map<SavedFeature, FeatureStart> getAllStarts();

	void setAllStarts(Map<SavedFeature, FeatureStart> map);
}
