/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.frozenblock.lib.common.worldgen.biome.api.modifications;

import com.google.common.collect.ImmutableSet;
import dev.architectury.registry.level.biome.BiomeModifications;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Provides several biome selectors with additional functionality.
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

    // FROM FABRIC API

    /**
     * Matches all Biomes. Use a more specific selector if possible.
     */
    public static Predicate<BiomeModifications.BiomeContext> all() {
        return context -> true;
    }

    /**
     * Matches Biomes that have not been originally defined in a data pack, but that are defined in code.
     */
    public static Predicate<BiomeModifications.BiomeContext> builtIn() {
        return context -> BuiltinRegistries.BIOME.containsKey(context.getKey().orElseThrow());
    }

    /**
     * Returns a biome selector that will match all biomes from the minecraft namespace.
     */
    public static Predicate<BiomeModifications.BiomeContext> vanilla() {
        return context -> {
            // In addition to the namespace, we also check that it doesn't come from a data pack.
            return context.getKey().orElseThrow().getNamespace().equals("minecraft")
                    && BuiltinRegistries.BIOME.containsKey(context.getKey().orElseThrow());
        };
    }

    /**
     * Returns a biome selector that will match all biomes in the given tag.
     *
     * @see net.minecraft.tags.BiomeTags
     */
    public static Predicate<BiomeModifications.BiomeContext> tag(TagKey<Biome> tag) {
        return context -> context.hasTag(tag);
    }

    /**
     * @see #excludeByKey(Collection)
     */
    @SafeVarargs
    public static Predicate<BiomeModifications.BiomeContext> excludeByKey(ResourceKey<Biome>... keys) {
        return excludeByKey(ImmutableSet.copyOf(keys));
    }

    /**
     * Returns a selector that will reject any biome whose key is in the given collection of keys.
     *
     * <p>This is useful for allowing a list of biomes to be defined in the config file, where
     * a certain feature should not spawn.
     */
    public static Predicate<BiomeModifications.BiomeContext> excludeByKey(Collection<ResourceKey<Biome>> keys) {
        return context -> !keys.contains(BuiltinRegistries.BIOME.getResourceKey(BuiltinRegistries.BIOME.getOptional(context.getKey().orElseThrow()).orElseThrow()).orElseThrow());
    }

    /**
     * @see #includeByKey(Collection)
     */
    @SafeVarargs
    public static Predicate<BiomeModifications.BiomeContext> includeByKey(ResourceKey<Biome>... keys) {
        return includeByKey(ImmutableSet.copyOf(keys));
    }

    /**
     * Returns a selector that will accept only biomes whose keys are in the given collection of keys.
     *
     * <p>This is useful for allowing a list of biomes to be defined in the config file, where
     * a certain feature should spawn exclusively.
     */
    public static Predicate<BiomeModifications.BiomeContext> includeByKey(Collection<ResourceKey<Biome>> keys) {
        return context -> keys.contains(BuiltinRegistries.BIOME.getResourceKey(BuiltinRegistries.BIOME.getOptional(context.getKey().orElseThrow()).orElseThrow()).orElseThrow());
    }

    /**
     * Returns a biome selector that will match biomes in which one of the given entity types can spawn.
     *
     * <p>Matches spawns in all {@link MobCategory spawn groups}.
     */
    public static Predicate<BiomeModifications.BiomeContext> spawnsOneOf(EntityType<?>... entityTypes) {
        return spawnsOneOf(ImmutableSet.copyOf(entityTypes));
    }

    /**
     * Returns a biome selector that will match biomes in which one of the given entity types can spawn.
     *
     * <p>Matches spawns in all {@link MobCategory spawn groups}.
     */
    public static Predicate<BiomeModifications.BiomeContext> spawnsOneOf(Set<EntityType<?>> entityTypes) {
        return context -> {
            MobSpawnSettings spawnSettings = BuiltinRegistries.BIOME.getOptional(context.getKey().orElseThrow()).orElseThrow().getMobSettings();

            for (MobCategory spawnGroup : MobCategory.values()) {
                for (MobSpawnSettings.SpawnerData spawnEntry : spawnSettings.getMobs(spawnGroup).unwrap()) {
                    if (entityTypes.contains(spawnEntry.type)) {
                        return true;
                    }
                }
            }

            return false;
        };
    }
}
