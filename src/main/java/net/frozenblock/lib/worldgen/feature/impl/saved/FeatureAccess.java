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

import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.jetbrains.annotations.Nullable;
import java.util.Map;

public interface FeatureAccess {
	@Nullable
	FeatureStart getStartForFeature(SavedFeature savedFeature);

	void setStartForFeature(SavedFeature savedFeature, FeatureStart start);

	LongSet getReferencesForFeature(SavedFeature savedFeature);

	void addReferenceForFeature(SavedFeature savedFeature, long reference);

	Map<SavedFeature, LongSet> getAllReferences();

	void setAllReferences(Map<SavedFeature, LongSet> map);
}
