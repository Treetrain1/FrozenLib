/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 * Modified to work on Architectury
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

import com.google.common.base.Preconditions;
import dev.architectury.registry.level.biome.BiomeModifications;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Predicate;

/**
 * Provides an API to modify Biomes after they have been loaded and before they are used in the World.
 *
 * <p>Any modifications made to biomes will not be available for use in server.properties (as of 1.16.1),
 * or the demo level.
 *
 * <p><b>Experimental feature</b>, may be removed or changed without further notice.
 * <p>Modified to work on Architectury
 */
public class FrozenBiomeModifications {

    /**
     * Convenience method to add a feature to one or more biomes.
     *
     * @see FrozenBiomeSelectors
     */
    public static void addFeature(Predicate<BiomeModifications.BiomeContext> biomeSelector, GenerationStep.Decoration step, Holder<PlacedFeature> placedFeatureHolder) {
        BiomeModifications.addProperties(biomeSelector, (biomeContext, mutable) -> {
            mutable.getGenerationProperties().addFeature(step, placedFeatureHolder);
        });
    }

    /**
     * Convenience method to add a carver to one or more biomes.
     *
     * @see FrozenBiomeSelectors
     */
    public static void addCarver(Predicate<BiomeModifications.BiomeContext> biomeSelector, GenerationStep.Carving step, Holder<ConfiguredWorldCarver<?>> configuredWorldCarverHolder) {
        BiomeModifications.addProperties(biomeSelector, (biomeContext, mutable) -> {
            mutable.getGenerationProperties().addCarver(step, configuredWorldCarverHolder);
        });
    }

    /**
     * Convenience method to add an entity spawn to one or more biomes.
     *
     * @see FrozenBiomeSelectors
     * @see net.minecraft.world.level.biome.MobSpawnSettings.Builder#addSpawn(MobCategory, MobSpawnSettings.SpawnerData)
     */
    public static void addSpawn(Predicate<BiomeModifications.BiomeContext> biomeSelector,
                                MobCategory spawnGroup, EntityType<?> entityType,
                                int weight, int minGroupSize, int maxGroupSize) {
        // See constructor of MobSpawnSettings.Builder for context
        Preconditions.checkArgument(entityType.getCategory() != MobCategory.MISC,
                "Cannot add spawns for entities with spawnGroup=MISC since they'd be replaced by pigs");

        // We need the entity type to be registered, or we cannot deduce an ID otherwise
        ResourceLocation id = Registry.ENTITY_TYPE.getKey(entityType);
        Preconditions.checkState(id != Registry.ENTITY_TYPE.getDefaultKey(), "Unregistered entity type: %s", entityType);

        BiomeModifications.addProperties(biomeSelector, ((biomeContext, mutable) -> {
            mutable.getSpawnProperties().addSpawn(spawnGroup, new MobSpawnSettings.SpawnerData(entityType, weight, minGroupSize, maxGroupSize));
        }));
    }
}
