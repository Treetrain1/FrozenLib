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

import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.NotNull;

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
}
