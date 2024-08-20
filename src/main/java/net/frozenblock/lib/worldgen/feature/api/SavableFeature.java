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
import net.frozenblock.lib.worldgen.feature.impl.saved.FeatureAccess;
import net.frozenblock.lib.worldgen.feature.impl.saved.FeatureManager;
import net.frozenblock.lib.worldgen.feature.impl.saved.SavedFeature;
import net.frozenblock.lib.worldgen.feature.impl.saved.ServerLevelFeatureManagerInterface;
import net.frozenblock.lib.worldgen.feature.impl.saved.WorldgenRandomSeedInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SavableFeature<FC extends FeatureConfiguration> extends Feature<FC> {
	public SavableFeature(Codec<FC> configCodec) {
		super(configCodec);
	}

	@Contract("_ -> new")
	protected final @NotNull SavedFeature createSavedFeature(@NotNull FeaturePlaceContext<FC> featurePlaceContext) {
		long seed = featurePlaceContext.random().nextLong();
		if (featurePlaceContext.random() instanceof WorldgenRandomSeedInterface worldgenRandom) {
			seed = worldgenRandom.frozenLib$getSeed();
		}
		return new SavedFeature(
			featurePlaceContext.origin(),
			new ConfiguredFeature<>(
				this,
				featurePlaceContext.config()
			),
			seed
		);
	}

	@Override
	public final boolean place(FeaturePlaceContext<FC> featurePlaceContext) {
		SavedFeature savedFeature = createSavedFeature(featurePlaceContext);
		return this.place(featurePlaceContext, savedFeature, null);
	}

	public abstract boolean place(FeaturePlaceContext<FC> featurePlaceContext, SavedFeature savedFeature, @Nullable BoundingBox boundingBox);

	public static @NotNull BoundingBox getWritableArea(@NotNull ChunkAccess chunkAccess) {
		ChunkPos chunkPos = chunkAccess.getPos();
		int i = chunkPos.getMinBlockX();
		int j = chunkPos.getMinBlockZ();
		LevelHeightAccessor levelHeightAccessor = chunkAccess.getHeightAccessorForGeneration();
		int k = levelHeightAccessor.getMinBuildHeight() + 1;
		int l = levelHeightAccessor.getMaxBuildHeight() - 1;
		return new BoundingBox(i, k, j, i + 15, l, j + 15);
	}

	protected void safeSetBlock(LevelWriter world, BlockPos pos, BlockState state, SavedFeature savedFeature, @Nullable BoundingBox boundingBox) {
		if (ensureCanWrite(world, pos)) {
			if (boundingBox == null || boundingBox.isInside(pos)) {
				world.setBlock(pos, state, Block.UPDATE_ALL);
			}
		} else {
			if (world instanceof WorldGenRegion worldGenRegion) {
				ServerLevel serverLevel = worldGenRegion.level;
				FeatureManager featureManager = ((ServerLevelFeatureManagerInterface) serverLevel).frozenLib$featureManager();
				ChunkPos chunkPos = new ChunkPos(pos);
				featureManager.addReferenceForFeature(
					SectionPos.of(pos),
					savedFeature,
					chunkPos.toLong(),
					(FeatureAccess) serverLevel.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_STARTS, false)
				);
			}
		}
	}

	protected boolean saveIfOutOfRange(LevelWriter world, BlockPos pos, SavedFeature savedFeature) {
		if (!ensureCanWrite(world, pos)) {
			if (world instanceof WorldGenRegion worldGenRegion) {
				ServerLevel serverLevel = worldGenRegion.level;
				FeatureManager featureManager = ((ServerLevelFeatureManagerInterface) serverLevel).frozenLib$featureManager();
				ChunkPos chunkPos = new ChunkPos(pos);
				featureManager.addReferenceForFeature(
					SectionPos.of(pos),
					savedFeature,
					chunkPos.toLong(),
					(FeatureAccess) serverLevel.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_STARTS, false)
				);
				return true;
			}
		}
		return false;
	}

	private static boolean ensureCanWrite(LevelWriter world, BlockPos pos) {
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
