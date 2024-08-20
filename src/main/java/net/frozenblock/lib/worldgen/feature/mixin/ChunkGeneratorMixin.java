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

import com.llamalad7.mixinextras.sugar.Local;
import net.frozenblock.lib.worldgen.feature.impl.saved.ChunkGeneratorSavableFeatureInterface;
import net.frozenblock.lib.worldgen.feature.impl.saved.FeatureManager;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin implements ChunkGeneratorSavableFeatureInterface {

	@Unique
	private FeatureManager frozenLib$featureManager;

	@Inject(
		method = "applyBiomeDecoration",
		at = @At(
			value = "JUMP",
			opcode = Opcodes.IF_ICMPGE,
		//	opcode = Opcodes.IF_ICMPLT,
			ordinal = 0
		)
	)
	public void frozenLib$applyBiomeDecoration(
		WorldGenLevel worldGenLevel, ChunkAccess chunkAccess, StructureManager structureManager, CallbackInfo info,
		@Local SectionPos sectionPos
	) {
		try {
			this.frozenLib$featureManager.getAllStarts(sectionPos).forEach(
				featureStart -> featureStart.placeInChunk(worldGenLevel, ChunkGenerator.class.cast(this), sectionPos.chunk()));
		} catch (Exception exception) {
			CrashReport crashReport = CrashReport.forThrowable(exception, "Savable Feature placement");
			crashReport.addCategory("FrozenLib - Savable Feature");
			throw new ReportedException(crashReport);
		}
	}

	@Unique
	@Override
	public void frozenLib$createReferences(WorldGenLevel worldGenLevel, FeatureManager featureManager, @NotNull ChunkAccess chunkAccess) {

	}

	@Unique
	@Override
	public void frozenLib$setFeatureManager(FeatureManager featureManager) {
		this.frozenLib$featureManager = featureManager;
	}
}
