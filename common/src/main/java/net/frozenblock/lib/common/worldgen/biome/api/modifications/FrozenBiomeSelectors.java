package net.frozenblock.lib.common.worldgen.biome.api.modifications;

import dev.architectury.registry.level.biome.BiomeModifications;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Predicate;

/**
 * Provides several biome selectors with additional functionality.
 *
 * <p>Based on Fabric API's BiomeSelectors
 */
public final class FrozenBiomeSelectors {

    private FrozenBiomeSelectors() {
    }

    /**
     * Returns a biome selector that will match all biomes that would normally spawn in the Overworld,
     * assuming Vanilla's default biome source is used.
     */
    public static Predicate<BiomeModifications.BiomeContext> foundInOverworld() {
        return context -> context.hasTag(BiomeTags.IS_OVERWORLD);
    }

    /**
     * Returns a biome selector that will match all biomes that would normally spawn in the Nether,
     * assuming Vanilla's default multi noise biome source with the nether preset is used.
     */
    public static Predicate<BiomeModifications.BiomeContext> foundInTheNether() {
        return context -> context.hasTag(BiomeTags.IS_NETHER);
    }

    /**
     * Returns a biome selector that will match all biomes that would normally spawn in the End,
     * assuming Vanilla's default End biome source is used.
     */
    public static Predicate<BiomeModifications.BiomeContext> foundInTheEnd() {
        return context -> context.hasTag(BiomeTags.IS_END);
    }

    /**
     * Returns a biome selector that will match all biomes that would normally spawn in the Overworld,
     * assuming Vanilla's default biome source is used, except for biomes in the specified tag.
     */
    public static Predicate<BiomeModifications.BiomeContext> foundInOverworldExcept(TagKey<Biome> except) {
        return context -> (context.hasTag(BiomeTags.IS_OVERWORLD) && !context.hasTag(except));
    }

    /**
     * Returns a biome selector that will match all biomes that would normally spawn in the Nether,
     * assuming Vanilla's default multi noise biome source with the nether preset is used, except for biomes in the specified tag.
     */
    public static Predicate<BiomeModifications.BiomeContext> foundInTheNetherExcept(TagKey<Biome> except) {
        return context -> (context.hasTag(BiomeTags.IS_NETHER)) && !context.hasTag(except);
    }

    /**
     * Returns a biome selector that will match all biomes that would normally spawn in the End,
     * assuming Vanilla's default End biome source is used, except for biomes in the specified tag.
     */
    public static Predicate<BiomeModifications.BiomeContext> foundInTheEndExcept(TagKey<Biome> except) {
        return context -> (context.hasTag(BiomeTags.IS_END)) && !context.hasTag(except);
    }
}
