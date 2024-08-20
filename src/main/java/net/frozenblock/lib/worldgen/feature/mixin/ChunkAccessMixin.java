/*
 * Copyright (C) 2024 FrozenBlock
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.lib.worldgen.feature.mixin;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.frozenblock.lib.worldgen.feature.impl.saved.FeatureAccess;
import net.frozenblock.lib.worldgen.feature.impl.saved.FeatureStart;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import java.util.Map;

@Mixin(ChunkAccess.class)
public class ChunkAccessMixin implements FeatureAccess {

	@Shadow
	protected volatile boolean unsaved;
	@Unique
	private final Map<ConfiguredFeature<?, ?>, FeatureStart> featureStarts = Maps.<ConfiguredFeature<?, ?>, FeatureStart>newHashMap();
	@Unique
	private final Map<ConfiguredFeature<?, ?>, LongSet> featureRefences = Maps.<ConfiguredFeature<?, ?>, LongSet>newHashMap();

	@Override
	public @Nullable StructureStart getStartForStructure(Structure structure) {
		return null;
	}

	@Override
	public void setStartForStructure(Structure structure, StructureStart start) {

	}

	@Override
	public LongSet getReferencesForStructure(Structure structure) {
		return null;
	}

	@Override
	public void addReferenceForFeature(ConfiguredFeature<?, ?> configuredFeature, long reference) {
		this.featureRefences.computeIfAbsent(configuredFeature, key -> new LongOpenHashSet()).add(reference);
		this.unsaved = true;
	}

	@Override
	public Map<Structure, LongSet> getAllReferences() {
		return Map.of();
	}

	@Override
	public void setAllReferences(Map<Structure, LongSet> structureReferences) {

	}
}
