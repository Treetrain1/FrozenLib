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

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public record SavedFeature(BlockPos origin, ConfiguredFeature<?, ?> configuredFeature, long seed) {
	public static final Codec<SavedFeature> CODEC = RecordCodecBuilder.create((instance) ->
		instance.group(
			BlockPos.CODEC.fieldOf("origin").forGetter((config) -> config.origin),
			ConfiguredFeature.DIRECT_CODEC.fieldOf("configured_feature").forGetter((config) -> config.configuredFeature),
			Codec.LONG.fieldOf("seed").forGetter((config) -> config.seed)
		).apply(instance, SavedFeature::new));
}
