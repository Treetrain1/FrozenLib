package net.frozenblock.lib.worldgen.biome.api.parameters;

import java.util.List;
import net.minecraft.world.level.biome.Climate;

public final class FrozenBiomeParameters {
	private FrozenBiomeParameters() {
		throw new UnsupportedOperationException("FrozenBiomeParameters contains only static declarations.");
	}

	public static void addWeirdness(BiomeRunnable runnable, List<Climate.Parameter> weirdnesses) {
		for (Climate.Parameter weirdness : weirdnesses) {
			runnable.run(weirdness);
		}
	}

	@FunctionalInterface
	public interface BiomeRunnable {
		void run(Climate.Parameter weirdness);
	}
}
