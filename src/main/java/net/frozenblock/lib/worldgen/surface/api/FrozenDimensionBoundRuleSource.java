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

package net.frozenblock.lib.worldgen.surface.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.SurfaceRules;

/**
 * Holds both a {@link ResourceLocation} and {@link SurfaceRules.RuleSource}.
 * The ResourceLocation denotes the dimension to be modified, and the RuleSource are the rules to be applied to it.
 */
public record FrozenDimensionBoundRuleSource(ResourceLocation dimension, SurfaceRules.RuleSource ruleSource) {

	public static final Codec<FrozenDimensionBoundRuleSource> CODEC = RecordCodecBuilder.create(instance ->
		instance.group(
			ResourceLocation.CODEC.fieldOf("dimension").forGetter(FrozenDimensionBoundRuleSource::dimension),
			SurfaceRules.RuleSource.CODEC.fieldOf("rule_source").forGetter(FrozenDimensionBoundRuleSource::ruleSource)
		).apply(instance, FrozenDimensionBoundRuleSource::new)
	);
}
