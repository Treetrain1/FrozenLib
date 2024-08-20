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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkAccess.class)
public class ChunkAccessMixin implements FeatureAccess, ChunkAccessFeatureInterface {
	@Final
	@Shadow
	private static LongSet EMPTY_REFERENCE_SET;

	@Shadow
	protected volatile boolean unsaved;
	@Unique
	private Map<SavedFeature, FeatureStart> frozenLib$featureStarts = Maps.newHashMap();
	@Unique
	private Map<SavedFeature, LongSet> frozenLib$featureRefences = Maps.newHashMap();

	@Inject(method = "<init>", at = @At("TAIL"))
	public void frozenLib$init(CallbackInfo info) {
		this.frozenLib$featureStarts = Maps.newHashMap();
		this.frozenLib$featureRefences = Maps.newHashMap();
	}

	@Unique
	@Override
	public @Nullable FeatureStart frozenLib$getStartForFeature(SavedFeature feature) {
		return this.frozenLib$featureStarts.get(feature);
	}

	@Unique
	@Override
	public void frozenLib$setStartForFeature(SavedFeature structure, FeatureStart start) {
		this.frozenLib$featureStarts.put(structure, start);
		this.unsaved = true;
	}

	@Unique
	@Override
	public LongSet frozenLib$getReferencesForFeature(SavedFeature feature) {
		return this.frozenLib$featureRefences.getOrDefault(feature, EMPTY_REFERENCE_SET);
	}

	@Unique
	@Override
	public void frozenLib$addReferenceForFeature(SavedFeature feature, long reference) {
		LongSet references = this.frozenLib$featureRefences.computeIfAbsent(feature, key -> new LongOpenHashSet());
		if (!references.contains(reference)) {
			references.add(reference);
		}
		this.unsaved = true;
	}

	@Unique
	@Override
	public Map<SavedFeature, LongSet> frozenLib$getAllReferences() {
		return Collections.unmodifiableMap(this.frozenLib$featureRefences);
	}

	@Unique
	@Override
	public void frozenLib$setAllReferences(Map<SavedFeature, LongSet> map) {
		this.frozenLib$featureRefences.clear();
		this.frozenLib$featureRefences.putAll(map);
		this.unsaved = true;
	}

	@Unique
	@Override
	public Map<SavedFeature, FeatureStart> frozenLib$getAllStarts() {
		return Collections.unmodifiableMap(this.frozenLib$featureStarts);
	}

	@Unique
	@Override
	public void frozenLib$setAllStarts(Map<SavedFeature, FeatureStart> map) {
		this.frozenLib$featureStarts.clear();
		this.frozenLib$featureStarts.putAll(map);
		this.unsaved = true;
	}
}
