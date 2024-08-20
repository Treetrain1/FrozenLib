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
import net.frozenblock.lib.worldgen.feature.impl.saved.FeatureAccess;
import net.frozenblock.lib.worldgen.feature.impl.saved.SavedFeature;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ImposterProtoChunk.class)
public class ImposterProtoChunkMixin implements FeatureAccess {
	@Shadow
	@Final
	private LevelChunk wrapped;

	@Unique
	@Override
	public LongSet frozenLib$getReferencesForFeature(SavedFeature feature) {
		return ((FeatureAccess)this.wrapped).frozenLib$getReferencesForFeature(feature);
	}

	@Unique
	@Override
	public void frozenLib$addReferenceForFeature(SavedFeature feature, long l) {
	}

	@Unique
	@Override
	public Map<SavedFeature, LongSet> frozenLib$getAllReferences() {
		return ((FeatureAccess)this.wrapped).frozenLib$getAllReferences();
	}

	@Unique
	@Override
	public void frozenLib$setAllReferences(Map<SavedFeature, LongSet> map) {
	}
}
