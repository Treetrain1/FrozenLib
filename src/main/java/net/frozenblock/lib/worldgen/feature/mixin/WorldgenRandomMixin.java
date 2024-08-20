package net.frozenblock.lib.worldgen.feature.mixin;

import net.frozenblock.lib.worldgen.feature.impl.saved.WorldgenRandomSeedInterface;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldgenRandom.class)
public class WorldgenRandomMixin implements WorldgenRandomSeedInterface {

	@Unique
	private long frozenLib$seed;

	@Override
	public long frozenLib$getSeed() {
		return this.frozenLib$seed;
	}

	@Inject(method = "setSeed", at = @At("HEAD"))
	public void frozenLib$setSeed(long seed, CallbackInfo info) {
		this.frozenLib$seed = seed;
	}
}
