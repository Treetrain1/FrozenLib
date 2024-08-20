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

package net.frozenblock.lib.worldgen.feature.impl.saved;

import com.mojang.logging.LogUtils;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class FeatureStart {
	private static final Logger LOGGER = LogUtils.getLogger();

	private final UUID id;
	private final SavedFeature feature;
	private final ChunkPos chunkPos;
	private int references;

	public FeatureStart(SavedFeature feature, ChunkPos chunkPos, int references) {
		this(UUID.randomUUID(), feature, chunkPos, references);
	}

	public FeatureStart(UUID uuid, SavedFeature feature, ChunkPos chunkPos, int references) {
		this.id = uuid;
		this.feature = feature;
		this.chunkPos = chunkPos;
		this.references = references;
	}

	@Nullable
	public static FeatureStart loadStaticStart(@NotNull CompoundTag compoundTag) {
		String uuid = compoundTag.getString("id");
		if (!compoundTag.contains("feature", 9)) {
			LOGGER.error("Unknown feature start detected!");
			return null;
		} else {
			SavedFeature savedFeature = SavedFeature.CODEC
				.parse(NbtOps.INSTANCE, compoundTag.getCompound("feature"))
				.resultOrPartial(LOGGER::error)
				.orElseThrow(() -> null);
			if (savedFeature == null) {
				LOGGER.error("Unknown feature start detected!");
				return null;
			} else {
				ChunkPos chunkPos = new ChunkPos(compoundTag.getInt("ChunkX"), compoundTag.getInt("ChunkZ"));
				int references = compoundTag.getInt("references");
				return new FeatureStart(UUID.fromString(uuid), savedFeature, chunkPos, references);
			}
		}
	}

	public void placeInChunk(
		WorldGenLevel worldGenLevel,
		ChunkGenerator chunkGenerator,
		ChunkPos chunkPos
	) {
		WorldgenRandom worldgenRandom = new WorldgenRandom(new XoroshiroRandomSource(RandomSupport.generateUniqueSeed()));
		worldgenRandom.setSeed(this.feature.seed());
		this.feature.configuredFeature().place(
			worldGenLevel,
			chunkGenerator,
			worldgenRandom,
			this.feature.origin()
		);
	}

	public CompoundTag createTag(@NotNull ChunkPos chunkPos) {
		CompoundTag compoundTag = new CompoundTag();
		compoundTag.putString("UUID", UUID.randomUUID().toString());
		SavedFeature.CODEC.encodeStart(NbtOps.INSTANCE, this.feature)
			.ifSuccess(tag -> compoundTag.put("feature", tag));
		compoundTag.putInt("ChunkX", chunkPos.x);
		compoundTag.putInt("ChunkZ", chunkPos.z);
		compoundTag.putInt("references", this.references);
		return compoundTag;
	}

	public ChunkPos getChunkPos() {
		return this.chunkPos;
	}

	public boolean canBeReferenced() {
		return this.references < this.getMaxReferences();
	}

	public void addReference() {
		this.references++;
	}

	public int getReferences() {
		return this.references;
	}

	protected int getMaxReferences() {
		return 1;
	}

	public SavedFeature getFeature() {
		return this.feature;
	}
}
