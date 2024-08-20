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

import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Map;
import net.frozenblock.lib.worldgen.feature.impl.saved.ChunkAccessFeatureInterface;
import net.frozenblock.lib.worldgen.feature.impl.saved.FeatureAccess;
import net.frozenblock.lib.worldgen.feature.impl.saved.FeatureStart;
import net.frozenblock.lib.worldgen.feature.impl.saved.SavedFeature;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ImposterProtoChunk.class)
public class ImposterProtoChunkMixin implements FeatureAccess, ChunkAccessFeatureInterface {
	@Shadow
	@Final
	private LevelChunk wrapped;

	@Unique
	@Nullable
	@Override
	public FeatureStart getStartForFeature(SavedFeature feature) {
		return ((FeatureAccess)this.wrapped).getStartForFeature(feature);
	}

	@Unique
	@Override
	public void setStartForFeature(SavedFeature feature, FeatureStart structureStart) {
	}

	@Unique
	@Override
	public Map<SavedFeature, FeatureStart> getAllStarts() {
		return ((ChunkAccessFeatureInterface)this.wrapped).getAllStarts();
	}

	@Unique
	@Override
	public void setAllStarts(Map<SavedFeature, FeatureStart> map) {
	}

	@Unique
	@Override
	public LongSet getReferencesForFeature(SavedFeature feature) {
		return ((FeatureAccess)this.wrapped).getReferencesForFeature(feature);
	}

	@Unique
	@Override
	public void addReferenceForFeature(SavedFeature feature, long l) {
	}

	@Unique
	@Override
	public Map<SavedFeature, LongSet> getAllReferences() {
		return ((FeatureAccess)this.wrapped).getAllReferences();
	}

	@Unique
	@Override
	public void setAllReferences(Map<SavedFeature, LongSet> map) {
	}
}
