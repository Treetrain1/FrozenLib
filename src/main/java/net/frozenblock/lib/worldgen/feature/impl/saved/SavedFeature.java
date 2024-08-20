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
