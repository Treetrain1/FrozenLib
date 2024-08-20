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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.concurrent.CompletableFuture;
import net.frozenblock.lib.worldgen.feature.impl.saved.ChunkGeneratorSavableFeatureInterface;
import net.frozenblock.lib.worldgen.feature.impl.saved.SavableFeatureChunkStatusTasks;
import net.frozenblock.lib.worldgen.feature.impl.saved.ServerLevelFeatureManagerInterface;
import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StaticCache2D;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.status.ChunkStatusTasks;
import net.minecraft.world.level.chunk.status.ChunkStep;
import net.minecraft.world.level.chunk.status.WorldGenContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkStatusTasks.class)
public class ChunkStatusTasksMixin {

	@Inject(
		method = "generateFeatures",
		at = @At(
			"HEAD"
		)
	)
	private static void frozenLib$generateFeatureReferences(
		WorldGenContext worldGenContext,
		ChunkStep chunkStep,
		StaticCache2D<GenerationChunkHolder> staticCache2D,
		ChunkAccess chunkAccess,
		CallbackInfoReturnable<CompletableFuture<ChunkAccess>> info
	) {
		SavableFeatureChunkStatusTasks.generateFeatureReferences(worldGenContext, chunkStep, staticCache2D, chunkAccess);
	}

	@WrapOperation(
		method = "generateFeatures",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;applyBiomeDecoration(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/world/level/StructureManager;)V"
		)
	)
	private static void frozenLib$addFeatureManager(
		ChunkGenerator instance, WorldGenLevel worldGenLevel, ChunkAccess chunkAccess, StructureManager structureManager, Operation<Void> original,
		@Local ServerLevel serverLevel
		) {
		if (instance instanceof ChunkGeneratorSavableFeatureInterface savableFeatureInterface) {
			savableFeatureInterface.frozenLib$setFeatureManager(((ServerLevelFeatureManagerInterface)serverLevel).frozenLib$featureManager());
		}
		original.call(instance, worldGenLevel, chunkAccess, structureManager);
	}
}
