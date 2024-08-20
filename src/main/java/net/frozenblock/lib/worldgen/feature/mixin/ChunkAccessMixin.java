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
import java.util.Collections;
import java.util.Map;
import net.frozenblock.lib.worldgen.feature.impl.saved.ChunkAccessFeatureInterface;
import net.frozenblock.lib.worldgen.feature.impl.saved.FeatureAccess;
import net.frozenblock.lib.worldgen.feature.impl.saved.FeatureStart;
import net.frozenblock.lib.worldgen.feature.impl.saved.SavedFeature;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChunkAccess.class)
public class ChunkAccessMixin implements FeatureAccess, ChunkAccessFeatureInterface {
	@Final
	@Shadow
	private static LongSet EMPTY_REFERENCE_SET;

	@Shadow
	protected volatile boolean unsaved;
	@Unique
	private final Map<SavedFeature, FeatureStart> featureStarts = Maps.newHashMap();
	@Unique
	private final Map<SavedFeature, LongSet> featureRefences = Maps.newHashMap();

	@Unique
	@Override
	public @Nullable FeatureStart getStartForFeature(SavedFeature feature) {
		return this.featureStarts.get(feature);
	}

	@Unique
	@Override
	public void setStartForFeature(SavedFeature structure, FeatureStart start) {
		this.featureStarts.put(structure, start);
		this.unsaved = true;
	}

	@Unique
	@Override
	public LongSet getReferencesForFeature(SavedFeature feature) {
		return this.featureRefences.getOrDefault(feature, EMPTY_REFERENCE_SET);
	}

	@Unique
	@Override
	public void addReferenceForFeature(SavedFeature feature, long reference) {
		this.featureRefences.computeIfAbsent(feature, key -> new LongOpenHashSet()).add(reference);
		this.unsaved = true;
	}

	@Unique
	@Override
	public Map<SavedFeature, LongSet> getAllReferences() {
		return Collections.unmodifiableMap(this.featureRefences);
	}

	@Unique
	@Override
	public void setAllReferences(Map<SavedFeature, LongSet> map) {
		this.featureRefences.clear();
		this.featureRefences.putAll(map);
		this.unsaved = true;
	}

	@Unique
	@Override
	public Map<SavedFeature, FeatureStart> getAllStarts() {
		return Collections.unmodifiableMap(this.featureStarts);
	}

	@Unique
	@Override
	public void setAllStarts(Map<SavedFeature, FeatureStart> map) {
		this.featureStarts.clear();
		this.featureStarts.putAll(map);
		this.unsaved = true;
	}
}
