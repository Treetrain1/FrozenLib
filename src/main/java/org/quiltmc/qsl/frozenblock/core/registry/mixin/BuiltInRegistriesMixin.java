package org.quiltmc.qsl.frozenblock.core.registry.mixin;

import net.fabricmc.fabric.mixin.registry.sync.DebugChunkGeneratorAccessor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesMixin {

	@Inject(method = "freeze", at = @At("RETURN"))
	private static void onFreezeBuiltins(CallbackInfo ci) {
		//region Fix MC-197259
		final List<BlockState> states = BuiltInRegistries.BLOCK.stream()
				.flatMap(block -> block.getStateDefinition().getPossibleStates().stream())
				.toList();

		final int xLength = Mth.ceil(Mth.sqrt(states.size()));
		final int zLength = Mth.ceil(states.size() / (float) xLength);

		DebugChunkGeneratorAccessor.setBLOCK_STATES(states);
		DebugChunkGeneratorAccessor.setX_SIDE_LENGTH(xLength);
		DebugChunkGeneratorAccessor.setZ_SIDE_LENGTH(zLength);
		//endregion
	}
}
