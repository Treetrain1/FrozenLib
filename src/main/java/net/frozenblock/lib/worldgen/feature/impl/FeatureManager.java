package net.frozenblock.lib.worldgen.feature.impl;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.StructureAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FeatureManager {
	private final LevelAccessor level;

	public FeatureManager(LevelAccessor level) {
		this.level = level;
	}

	public FeatureManager forWorldGenRegion(@NotNull WorldGenRegion region) {
		if (region.getLevel() != this.level) {
			throw new IllegalStateException("Using invalid feature manager (source level: " + region.getLevel() + ", region: " + region);
		} else {
			return new FeatureManager(region);
		}
	}

	public List<StructureStart> startsForStructure(ChunkPos pos, Predicate<Structure> structurePredicate) {
		Map<Structure, LongSet> map = this.level.getChunk(pos.x, pos.z, ChunkStatus.STRUCTURE_REFERENCES).getAllReferences();
		ImmutableList.Builder<StructureStart> builder = ImmutableList.builder();

		for (Map.Entry<Structure, LongSet> entry : map.entrySet()) {
			Structure structure = entry.getKey();
			if (structurePredicate.test(structure)) {
				this.fillStartsForStructure(structure, entry.getValue(), builder::add);
			}
		}

		return builder.build();
	}

	public List<StructureStart> startsForStructure(SectionPos sectionPos, Structure structure) {
		LongSet longSet = this.level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES).getReferencesForStructure(structure);
		ImmutableList.Builder<StructureStart> builder = ImmutableList.builder();
		this.fillStartsForStructure(structure, longSet, builder::add);
		return builder.build();
	}

	public void fillStartsForStructure(Structure structure, LongSet references, Consumer<StructureStart> consumer) {
		LongIterator var4 = references.iterator();

		while (var4.hasNext()) {
			long l = (Long)var4.next();
			SectionPos sectionPos = SectionPos.of(new ChunkPos(l), this.level.getMinSection());
			StructureStart structureStart = this.getStartForStructure(
				sectionPos, structure, this.level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_STARTS)
			);
			if (structureStart != null && structureStart.isValid()) {
				consumer.accept(structureStart);
			}
		}
	}

	@Nullable
	public StructureStart getStartForStructure(SectionPos sectionPos, Structure structure, StructureAccess holder) {
		return holder.getStartForStructure(structure);
	}

	public void setStartForStructure(SectionPos sectionPos, Structure structure, StructureStart start, StructureAccess holder) {
		holder.setStartForStructure(structure, start);
	}

	public void addReferenceForFeature(SectionPos sectionPos, ConfiguredFeature<?, ?> configuredFeature, long reference, FeatureAccess holder) {
		holder.addReferenceForFeature(configuredFeature, reference);
	}

	public boolean shouldGenerateStructures() {
		return this.worldOptions.generateStructures();
	}

	public StructureStart getStructureAt(BlockPos pos, Structure structure) {
		for (StructureStart structureStart : this.startsForStructure(SectionPos.of(pos), structure)) {
			if (structureStart.getBoundingBox().isInside(pos)) {
				return structureStart;
			}
		}

		return StructureStart.INVALID_START;
	}

	public StructureStart getStructureWithPieceAt(BlockPos pos, TagKey<Structure> structures) {
		return this.getStructureWithPieceAt(pos, holder -> holder.is(structures));
	}

	public StructureStart getStructureWithPieceAt(BlockPos pos, HolderSet<Structure> obj) {
		return this.getStructureWithPieceAt(pos, obj::contains);
	}

	public StructureStart getStructureWithPieceAt(BlockPos pos, Predicate<Holder<Structure>> predicate) {
		Registry<Structure> registry = this.registryAccess().registryOrThrow(Registries.STRUCTURE);

		for (StructureStart structureStart : this.startsForStructure(
			new ChunkPos(pos), structure -> (Boolean)registry.getHolder(registry.getId(structure)).map(predicate::test).orElse(false)
		)) {
			if (this.structureHasPieceAt(pos, structureStart)) {
				return structureStart;
			}
		}

		return StructureStart.INVALID_START;
	}

	public StructureStart getStructureWithPieceAt(BlockPos pos, Structure structure) {
		for (StructureStart structureStart : this.startsForStructure(SectionPos.of(pos), structure)) {
			if (this.structureHasPieceAt(pos, structureStart)) {
				return structureStart;
			}
		}

		return StructureStart.INVALID_START;
	}

	public boolean structureHasPieceAt(BlockPos pos, StructureStart start) {
		for (StructurePiece structurePiece : start.getPieces()) {
			if (structurePiece.getBoundingBox().isInside(pos)) {
				return true;
			}
		}

		return false;
	}

	public boolean hasAnyStructureAt(BlockPos pos) {
		SectionPos sectionPos = SectionPos.of(pos);
		return this.level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES).hasAnyStructureReferences();
	}

	public Map<Structure, LongSet> getAllStructuresAt(BlockPos pos) {
		SectionPos sectionPos = SectionPos.of(pos);
		return this.level.getChunk(sectionPos.x(), sectionPos.z(), ChunkStatus.STRUCTURE_REFERENCES).getAllReferences();
	}

	public StructureCheckResult checkStructurePresence(ChunkPos pos, Structure structure, StructurePlacement placement, boolean skipExistingChunks) {
		return this.structureCheck.checkStart(pos, structure, placement, skipExistingChunks);
	}

	public void addReference(StructureStart start) {
		start.addReference();
		this.structureCheck.incrementReference(start.getChunkPos(), start.getStructure());
	}

	public RegistryAccess registryAccess() {
		return this.level.registryAccess();
	}
}
