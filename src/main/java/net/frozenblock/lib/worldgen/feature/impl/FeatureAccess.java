package net.frozenblock.lib.worldgen.feature.impl;

import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.jetbrains.annotations.Nullable;
import java.util.Map;

public interface FeatureAccess {
	@Nullable
	StructureStart getStartForStructure(Structure structure);

	void setStartForStructure(Structure structure, StructureStart start);

	LongSet getReferencesForStructure(Structure structure);

	void addReferenceForFeature(ConfiguredFeature<?, ?> configuredFeature, long reference);

	Map<Structure, LongSet> getAllReferences();

	void setAllReferences(Map<Structure, LongSet> structureReferences);
}
