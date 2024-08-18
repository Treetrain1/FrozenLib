package net.frozenblock.lib.worldgen.feature.api;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public abstract class SaveableConfiguredFeature<FC extends FeatureConfiguration> extends Feature<FC> {

	public SaveableConfiguredFeature(Codec<FC> configCodec) {
		super(configCodec);
	}

	@Override
	protected void setBlock(LevelWriter world, BlockPos pos, BlockState state) {
		if (this.ensureCanWrite(world, pos)) {
			world.setBlock(pos, state, Block.UPDATE_ALL);
		} else {
			featuremanager.addfeaturereference()grhjdbk
		}
	}

	private void

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
