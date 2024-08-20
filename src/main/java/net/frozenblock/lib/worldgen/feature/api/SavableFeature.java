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

package net.frozenblock.lib.worldgen.feature.api;

import com.mojang.serialization.Codec;
import net.frozenblock.lib.worldgen.feature.impl.saved.FeatureManager;
import net.frozenblock.lib.worldgen.feature.impl.saved.ServerLevelInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public abstract class SavableFeature<FC extends FeatureConfiguration> extends Feature<FC> {

	public SavableFeature(Codec<FC> configCodec) {
		super(configCodec);
	}

	@Override
	protected void setBlock(LevelWriter world, BlockPos pos, BlockState state) {
		if (this.ensureCanWrite(world, pos)) {
			world.setBlock(pos, state, Block.UPDATE_ALL);
		} else {
			if (world instanceof ServerLevel level) {
				FeatureManager manager = ((ServerLevelInterface) level).frozenLib$featureManager();
				// TODO: do whatever
			}
		}
	}

	private boolean ensureCanWrite(LevelWriter world, BlockPos pos) {
		if (world instanceof WorldGenRegion worldGenRegion) {
			int i = SectionPos.blockToSectionCoord(pos.getX());
			int j = SectionPos.blockToSectionCoord(pos.getZ());
			ChunkPos chunkPos = worldGenRegion.getCenter();
			int k = Math.abs(chunkPos.x - i);
			int l = Math.abs(chunkPos.z - j);
			if (k <= worldGenRegion.generatingStep.blockStateWriteRadius() && l <= worldGenRegion.generatingStep.blockStateWriteRadius()) {
				if (worldGenRegion.center.isUpgrading()) {
					LevelHeightAccessor levelHeightAccessor = worldGenRegion.center.getHeightAccessorForGeneration();
					if (pos.getY() < levelHeightAccessor.getMinBuildHeight() || pos.getY() >= levelHeightAccessor.getMaxBuildHeight()) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

}
