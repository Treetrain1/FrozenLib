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

package net.frozenblock.lib.worldgen.feature.impl.saved;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FeatureManager {
	private final LevelAccessor level;

	public FeatureManager(LevelAccessor level) {
		this.level = level;
	}

	public FeatureManager forWorldGenRegion(@NotNull WorldGenRegion region) {
		if (region.getLevel() != this.level) {
			throw new IllegalStateException("Using invalid feature manager (source level: " + region.getLevel() + ", region: " + region);
		} else {
			return new FeatureManager(region);
		}
	}

	public List<FeatureStart> getAllStarts(@NotNull SectionPos sectionPos) {
		Map<SavedFeature, LongSet> references = ((FeatureAccess)this.level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.FEATURES)).frozenLib$getAllReferences();
		ImmutableList.Builder<FeatureStart> builder = ImmutableList.builder();
		references.forEach((feature, longSet) -> this.fillStartsForFeature(feature, longSet, builder::add));
		return builder.build();
	}

	public List<FeatureStart> startsForFeature(@NotNull SectionPos sectionPos, SavedFeature feature) {
		LongSet longSet = ((FeatureAccess)this.level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.FEATURES)).frozenLib$getReferencesForFeature(feature);
		ImmutableList.Builder<FeatureStart> builder = ImmutableList.builder();
		this.fillStartsForFeature(feature, longSet, builder::add);
		return builder.build();
	}

	public void fillStartsForFeature(SavedFeature feature, @NotNull LongSet longSet, Consumer<FeatureStart> consumer) {
		for (long l : longSet) {
			SectionPos sectionPos = SectionPos.of(new ChunkPos(l), this.level.getMinSection());
			FeatureStart featureStart = this.getStartForFeature(
				sectionPos, feature, (FeatureAccess) this.level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.FEATURES)
			);
			if (featureStart != null) {
				consumer.accept(featureStart);
			}
		}
	}

	@Nullable
	public FeatureStart getStartForFeature(SectionPos sectionPos, SavedFeature feature, @NotNull FeatureAccess featureAccess) {
		return featureAccess.frozenLib$getStartForFeature(feature);
	}

	public void setStartForStructure(SectionPos sectionPos, SavedFeature feature, FeatureStart featureStart, @NotNull FeatureAccess featureAccess) {
		featureAccess.frozenLib$setStartForFeature(feature, featureStart);
	}

	public void addReferenceForFeature(SectionPos sectionPos, SavedFeature feature, long l, @NotNull FeatureAccess featureAccess) {
		featureAccess.frozenLib$addReferenceForFeature(feature, l);
	}
}
